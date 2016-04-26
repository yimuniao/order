package com.yimuniao.algorithm;

import java.util.HashMap;
import java.util.Map;

public class SundayMine {

    private Map<Character, Integer> map = new HashMap<Character, Integer>();
    String text = null;
    String pattern = null;
    int currentPos = 0;

    public SundayMine(String text, String pattern) {
        this.text = text;
        this.pattern = pattern;
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < pattern.length(); i++) {
            map.put(pattern.charAt(i), i);
        }
    }

    public int sundayMatch() {
        int textLength = text.length();
        int patternLength = pattern.length();
        if (textLength < patternLength) {
            return -1;
        }

        while (currentPos + patternLength <= textLength) {
            String substring = text.substring(currentPos, currentPos + patternLength);
            if (substring.equals(pattern)) {
                return currentPos;
            }
            if (currentPos + patternLength == textLength){
                return -1;
            }
            Character c = text.charAt(currentPos+patternLength); 
            Integer lastIndex = map.get(c);
            if (lastIndex != null){
                currentPos = currentPos +(patternLength - lastIndex);
            }
            else
            {
                currentPos = currentPos + patternLength;
            }
            
        }

        return -1;

    }

    public static void main(String[] args) {

        SundayMine sundySearch = new SundayMine("hello adfsadfklf adf234masdfsdfdsfdsfdsffwerwrewrerwerwersdf2666sdflsdfk", "2666sdf");

        long begin = System.nanoTime();

        begin = System.nanoTime();
        System.out.println("SundayMatch:" + sundySearch.sundayMatch());
        System.out.println("SundayMatch:" + (System.nanoTime() - begin));
        
        int a = -11000;
        String aa = Integer.toString(a);
        System.out.println("aa: "+aa);
        String bb="abcdefg";
        System.out.println("  " + bb.substring(2, 4));
    }

}
