package goit.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        templateEngine = new TemplateEngine();


        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(getServletContext().getRealPath("/WEB-INF/templates/"));
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(templateEngine.getTemplateResolvers().size());

        templateEngine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // get arg for timezone
        String timezone = request.getParameter("timezone");

        if (timezone == null) {
            // get from cookies
            String timezoneFromCookie = getTimezoneFromCookie(request);

            if (timezoneFromCookie != null) {
                timezone = timezoneFromCookie;
            }
            else {
                timezone = "UTC";  // if not exist
            }
        }

        //чомусь знак "+" міняється на пробіл
        //зі знаком "-" такої проблеми немає
        if (timezone != null) {
            timezone = timezone.replace(" ", "+");
        }
        // save to cookies if is valid
        if (isValidTimezone(timezone)) {
            setTimezoneInCookie(response, timezone);
        }

        // get the current time for a given time zone
        ZoneId zoneId = ZoneId.of(timezone);
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);

        // formating time
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

        // template options
        Map<String, Object> params = Map.of(
                "time", formattedTime,
                "timezone", timezone
        );

        // use thymeleaf
        Context context = new Context(request.getLocale(), params);
        templateEngine.process("time", context, response.getWriter());
        response.getWriter().close();
    }


    //Checking the correct time zone
    private boolean isValidTimezone(String timezone) {
        try {
            ZoneId.of(timezone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // get timezote from  cookies
    private String getTimezoneFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("lastTimezone".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;  // if cookies do not exist
    }

    // save cookie
    private void setTimezoneInCookie(HttpServletResponse response, String timezone) {
        Cookie cookie = new Cookie("lastTimezone", timezone);
        cookie.setMaxAge(60 * 60 * 5);  // period 5 hours
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
