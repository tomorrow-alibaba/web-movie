package com.filter.client;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
@WebFilter(urlPatterns = "/**")
public class ClientFilter2 implements Filter {


    public void destroy() {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
//        System.out.println("自定义过滤器->doFilter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //子系统已登录直接登录
        HttpSession session = request.getSession();
        if (session.getAttribute("isLogin") != null && (Boolean) session.getAttribute("isLogin")) {
            session.setMaxInactiveInterval(1 * 60);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String token = request.getParameter("token");
            if (token != null) {  //假设验证有效
                //子系统未登陆 但认证中心已登录
                session.setAttribute("isLogin", true);
                session.setAttribute("token", token);
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                //子系统未登陆 认证中心未登录
                //带着客户端url跳转至登录页面
                response.setStatus(401);
                response.setHeader("Location", "http://10.42.32.154:8080");
                response.setHeader("redirect_url", request.getHeader("Referer"));
            }
        }
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
