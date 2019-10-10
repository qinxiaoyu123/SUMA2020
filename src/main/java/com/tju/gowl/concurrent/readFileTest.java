package com.tju.gowl.concurrent;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class readFileTest {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("qin", "xiaoyu");
        map.get("qin");
        String[] ss = new String[100];
        System.out.println(ss[10]);
    }

    String[] hashArray;
    int length = 100;

    private int readInHash(String value) {
        int hashInt = hash(value);
        int i;
        if ( hashArray[i = (length - 1) & hashInt] == null) {
            hashArray[i] = value;
            return i;
        }
        while(true){
            i++;
            String ss = hashArray[i];
            if(ss.equals(value)){
                return i;
            }
            if(ss == null){
                break;
            }
        }
        hashArray[i] = value;
        return i;
    }


    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
