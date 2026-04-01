package com.hw2;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class TimeServlet extends HttpServlet {

    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        engine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timezone = req.getParameter("timezone");
        Cookie[] cookies = req.getCookies();
        String lastTimezone = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("lastTimezone".equals(c.getName())) {
                    lastTimezone = c.getValue();
                    break;
                }
            }
        }
        ZoneId zone = ZoneId.of("UTC");
        if (timezone != null) {
            zone = ZoneId.of(timezone);
            Cookie cookie = new Cookie("lastTimezone", timezone);
            resp.addCookie(cookie);
        } else if (lastTimezone != null) {
            zone = ZoneId.of(lastTimezone);
        }
        ZonedDateTime now = ZonedDateTime.now(zone);
        String timeStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ")) + zone;
        Context context = new Context();
        context.setVariable("time", timeStr);
        String html = engine.process("time", context);
        resp.setContentType("text/html");
        resp.getWriter().write(html);
    }
}