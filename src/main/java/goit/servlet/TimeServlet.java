package com.example.time;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String timezone = request.getParameter("timezone");

        //чомусь знак "+" міняється на пробіл
        //зі знаком "-" такої проблеми немає
        if (timezone != null) {
            timezone = timezone.replace(" ", "+");
        }
        System.out.println("time");
        ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");

        String currentTime = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss z")
                .withZone(zoneId)
                .format(Instant.now());

        response.getWriter().write("<html><body>");
        response.getWriter().write("<h1>Current Time:</h1>");
        response.getWriter().write("<p>" + currentTime + "</p>");
        response.getWriter().write("</body></html>");
    }
}
