package com.tju.gowl.materialization;

import java.util.Map;

public class EncodeOwlThingImpl {
    private static CodeRepository CodeRepository = new CodeRepository();
    private static Map<String, Integer> encodeMap = CodeRepository.getEncode();
    private static Map<Integer, String> decodeMap = CodeRepository.getDecode();
    private static int indexCode = CodeRepository.getIndexEncode();

    public static int encodeRdf(String ss) {
        int ssIndex;
        if (!encodeMap.containsKey(ss)) {
            encodeMap.put(ss, indexCode);
            decodeMap.put(indexCode, ss);
            ssIndex = indexCode;
            CodeRepository.increaseIndexEncode();
        } else {
            ssIndex = encodeMap.get(ss);
        }
        return ssIndex;
    }

}
