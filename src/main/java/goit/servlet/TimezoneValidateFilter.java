package goit.servlet;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.TimeZone;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String timezone = request.getParameter("timezone");

        if (timezone != null) {
            timezone = timezone.replace(" ", "+");

            try {

                //перетворюрюємо часовий пояс на ZoneId
                System.out.println("try");
                System.out.println(timezone);
                ZoneId zone = ZoneId.of(timezone);
                System.out.println(zone.toString());
            } catch (Exception e) {
                // якщо виникає помилка, то повертаємо помилку 400 і відповідь
                ((jakarta.servlet.http.HttpServletResponse) response).setStatus(400);
                response.setContentType("text/html");
                response.getWriter().write("<html><body>");
                response.getWriter().write("<h1>invalid timezone</h1>");
                response.getWriter().write("<p> status is" + ((HttpServletResponse) response).getStatus() +"</p>");
                response.getWriter().write("</body></html>");

                return;
            }
        }

        // якщо часовий пяос коректний,то продовжуємо обробку запиту
        chain.doFilter(request, response);
    }




}