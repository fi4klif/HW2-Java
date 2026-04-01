package com.hw2;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.time.*;

public class TimezoneValidateFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String timezone = req.getParameter("timezone");
        if (timezone != null) {
            try {
                ZoneId.of(timezone);
            } catch (Exception e) {
                resp.setStatus(400);
                resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();
                out.println("Invalid timezone");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}