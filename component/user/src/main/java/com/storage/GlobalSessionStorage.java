package com.storage;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

public class GlobalSessionStorage {
    private  HashMap<String, List<String>> tokenMap = new HashMap();
    public static GlobalSessionStorage instance = new GlobalSessionStorage();

    private GlobalSessionStorage() {

    }
    public HashMap<String, List<String>> getMap(){
        return tokenMap;
    }
}
