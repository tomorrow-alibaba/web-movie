package com.api;

import java.util.HashMap;

public class ClientSessionStorage {
    private static HashMap<String, UserToken> userTokenMap = new HashMap<>();
    public static ClientSessionStorage instance = new ClientSessionStorage();

    private ClientSessionStorage() {

    }

    public HashMap<String, UserToken> getMap() {
        return userTokenMap;
    }
}
