package com.rest;

import com.api.ClientSessionStorage;
import com.api.User;
import com.api.UserToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.UUID;

@Controller
@RequestMapping("/moviecase")
public class Hello {

    public HashMap<String, UserToken> tokenMap = ClientSessionStorage.instance.getMap();


    @RequestMapping(value = "/movies", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String getMovies() {
        String a = "hello client1";
        return a;
    }

    @RequestMapping(value = "/user", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String getUserName(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if ("CLIENTSESSIONID".equals(cookie.getName())) {
                    return tokenMap.get(cookie.getValue()).getUser().getName();
                }
            }
        }
        return null;
    }


    @RequestMapping(value = "/validate", produces = "application/json", method = {RequestMethod.POST})
    @ResponseBody
    public User validateToken(String token, HttpServletRequest request, HttpServletResponse response) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URIBuilder uriBuilder = new URIBuilder("http://sso.server.com:8080/user/token");
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
                        response.sendRedirect("/");
                        return user;
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

    @RequestMapping(value = "/logout", produces = "application/json", method = {RequestMethod.GET})
    @ResponseBody
    public String logout(HttpServletRequest request) {
        String token = request.getParameter("token");
        HttpSession clientSession = request.getSession();
        clientSession.invalidate();
        return "logout success client1";
    }

}
