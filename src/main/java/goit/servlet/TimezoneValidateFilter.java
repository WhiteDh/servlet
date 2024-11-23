package goit.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneId;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String timezone = request.getParameter("timezone");

        if (timezone != null) {
            timezone = timezone.replace(" ", "+");

            try {
                // Перевірка, чи є валідний часовий пояс
                ZoneId zone = ZoneId.of(timezone);
            } catch (Exception e) {
                // Якщо помилка, відправляємо помилку 400 і відповідь
                ((HttpServletResponse) response).setStatus(400);
                response.setContentType("text/html");
                response.getWriter().write("<html><body>");
                response.getWriter().write("<h1>Invalid Timezone</h1>");
                response.getWriter().write("<p>Status code: " + ((HttpServletResponse) response).getStatus() + "</p>");
                response.getWriter().write("</body></html>");

                return;
            }
        }

        // Якщо часовий пояс правильний, продовжуємо обробку запиту
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
