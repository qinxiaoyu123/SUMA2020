package com.tju.gowl.materialization;

import java.util.Map;

public class EncodeOwlThing {
    private static CodeStorage codeStorage = new CodeStorage();
    private static Map<String, Integer> encodeMap = codeStorage.getEncode();
    private static Map<Integer, String> decodeMap = codeStorage.getDecode();
    private static int indexCode = codeStorage.getIndexEncode();

    public static int encodeRdf(String ss) {
        int ssIndex;
        if (!encodeMap.containsKey(ss)) {
            encodeMap.put(ss, indexCode);
            decodeMap.put(indexCode, ss);
            ssIndex = indexCode;
            codeStorage.increaseIndexEncode();
        } else {
            ssIndex = encodeMap.get(ss);
        }
        return ssIndex;
    }

}
