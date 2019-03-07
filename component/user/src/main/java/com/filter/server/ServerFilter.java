package com.filter.server;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.storage.ClientSessionStorage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Component;


@Component
@WebFilter(urlPatterns = "/**")
public class ServerFilter implements Filter {

    public void destroy() {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
//        System.out.println("自定义过滤器->doFilter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        if (url.contains("/user/register")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpSession session = request.getSession();
//            String clientUrl = request.getParameter("redirect_url");
//            String token = (String) session.getAttribute("token");
//            //客户端发过来的跳转请求
//            if (clientUrl != null && !"".equals(clientUrl)) {
//                // 存储，用于注销
//                response.setStatus(403);
//                response.setHeader("token",token);
//                response.setHeader("Location",clientUrl);
//                return;
//            }

        if (url.contains("/user/status")) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        if (cookie.getValue() != null) { //假设验证有效
                            response.sendRedirect(request.getHeader("Referer") + "?token=" + cookie.getValue());
                            return;
                        }
                    }
                }
            }
            response.sendRedirect(request.getHeader("Referer"));
//            response.setStatus(401);
//            response.setHeader("Location", "http://sso.server.com:8080");
//            response.setHeader("redirect_url", request.getHeader("Referer"));
            return;
        }
        if (url.contains("/user/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    public boolean verifyToken(String ssoServer, String token) {
        HttpPost httpPost = new HttpPost(ssoServer + "?token=" + token);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
