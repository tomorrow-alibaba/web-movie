package com.storage;

import java.util.HashMap;

public class ClientSessionStorage {
    private HashMap<String, String> tokenMap = new HashMap();
    public static ClientSessionStorage instance = new ClientSessionStorage();

    private ClientSessionStorage() {

    }
    public HashMap<String, String> getMap(){
        return tokenMap;
    }
}
