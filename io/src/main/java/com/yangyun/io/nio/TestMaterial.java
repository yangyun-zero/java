package com.yangyun.io.nio;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author yun.Yang
 * @Date 2021/10/24 20:28
 * @Version 1.0
 **/
public class TestMaterial {

    private String getSeqNumToString1(long seqNum) {
        int c = 11;
        String seqNumStr = String.valueOf(seqNum);
        String str = "";
        for (int i = 0; i < c - seqNumStr.length(); i++) {
            str += "0";
        }
        return str.concat(String.valueOf(seqNum));
    }

    private String generationNumberSegment(long nowIncre) {
        String seqNumStr = getSeqNumToString(nowIncre);
        return "B1".concat(seqNumStr).concat(validateAndGet(seqNumStr));
    }

    private String getSeqNumToString(long seqNum) {
        int c = 11;
        String seqNumStr = String.valueOf(seqNum);
        String str = "";
        for (int i = 0; i < c - seqNumStr.length(); i++) {
            str += 0;
        }
        return str.concat(String.valueOf(seqNum));
    }

    private String validateAndGet(String num) {
        int length = 11;
        if (StringUtils.isEmpty(num) || num.length() < length) {
        }
//        num = num.substring(0, num.length() - 1);
        int c = 0;
        for (int i = 0; i < "99321088643".length(); i++) {
            c += Integer.parseInt(String.valueOf("99321088643".charAt(i))) * Integer.parseInt(String.valueOf(num.charAt(i)));
        }

        int d = 0;
        for (int i = 0; i < "99321064388".length(); i++) {
            d += Integer.parseInt(String.valueOf("99321064388".charAt(i))) + Integer.parseInt(String.valueOf(num.charAt(i)));
        }

        String resultTotal = String.valueOf(c + d);

        return resultTotal.substring(resultTotal.length() - 1);

    }

}
