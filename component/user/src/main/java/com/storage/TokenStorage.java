package com.storage;

import com.api.User;

import java.util.HashMap;
import java.util.List;

public class TokenStorage {
    private HashMap<String, User> tokenMap = new HashMap();
    public static TokenStorage instance = new TokenStorage();

    private TokenStorage() {

    }
    public HashMap<String, User> getMap(){
        return tokenMap;
    }
}
