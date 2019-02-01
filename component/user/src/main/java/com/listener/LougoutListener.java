package com.listener;

import com.storage.GlobalSessionStorage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
public class LougoutListener implements HttpSessionListener {

    HashMap<String, List<String>> globalTokenMap = GlobalSessionStorage.instance.getMap();

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        String token =  (String) httpSessionEvent.getSession().getAttribute("token");
        if(globalTokenMap.containsKey(token)) {
            List<String> clientList = globalTokenMap.get(token);
            for(String client : clientList){
                this.logoutNotify(client,token);
            }
            globalTokenMap.remove(token);
        }
    }

    public boolean logoutNotify(String client, String token) {
        HttpPost httpPost = new HttpPost(client + "?token=" + token);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
