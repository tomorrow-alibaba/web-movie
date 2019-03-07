package com.filter.client;


import com.api.ClientSessionStorage;
import com.api.User;
import com.api.UserToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.UUID;


@Component
@WebFilter(urlPatterns = "/**")
public class ClientFilter implements Filter {

    public HashMap<String, UserToken> tokenMap = ClientSessionStorage.instance.getMap();

    public void destroy() {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = request.getParameter("token");
        if (token != null && !"".equals(token)) {
            String custSessionId = verifyToken(token);
            if (custSessionId != null) {
                Cookie cookie = new Cookie("CLIENTSESSIONID", custSessionId);
                response.addCookie(cookie);
            }
            response.sendRedirect(request.getServletPath());
            return;
        }
        String url = request.getRequestURI();
        HttpSession session = request.getSession();
        String[] staticFile = {".ico", ".html", ".css", ".js", ".jpg", "png", "index"};
        for (String file : staticFile) {
            if (url.contains(file)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        String[] reqFilter = {"/moviecase/user"};
        for (String file : reqFilter) {
            if (url.contains(file)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        Cookie[] cookies = request.getCookies();
        boolean hasClientSessionId = false;
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if ("CLIENTSESSIONID".equals(cookie.getName())) {
                    hasClientSessionId = true;
                }
            }
        }
        if (!hasClientSessionId) {
            response.setStatus(401);
            response.setHeader("Location", "http://sso.server.com:8088/user/sso");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public String verifyToken(String token) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URIBuilder uriBuilder = new URIBuilder("http://sso.server.com:8088/user/token");
            uriBuilder.addParameter("token", token);
            HttpGet httpget = new HttpGet(uriBuilder.build());
            System.out.println("executing request " + httpget.getURI());
            // 执行get请求.
            CloseableHttpResponse httpResponse = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = httpResponse.getEntity();
                System.out.println("--------------------------------------");
                // 打印响应状态
                System.out.println(httpResponse.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    String a = EntityUtils.toString(entity);
                    System.out.println("Response content: " + a);
                    if (a != null && !a.equals("")) {
                        ObjectMapper mapper = new ObjectMapper();
                        User user = mapper.readValue(a, User.class);
                        UserToken userToken = new UserToken();
                        userToken.setUser(user);
                        String clientSessionId = UUID.randomUUID().toString();
                        tokenMap.put(clientSessionId, userToken);
                        return clientSessionId;
                    }
                }
                System.out.println("------------------------------------");
            } finally {
                httpResponse.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
