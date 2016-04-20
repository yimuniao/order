package com.yimuniao.algorithm.sumtest;

import java.util.Stack;

public class GetAllSubsetByStack4 {

    private static final int[] DATA = { 1, 3, 4, 5, 6, 2, 7, 8, 9, 10, 11, 13, 14, 15 };

    public static void main(String[] args) {
        GetAllSubsetByStack4 get = new GetAllSubsetByStack4();
        get.populateSubset(DATA, 0, DATA.length);
        System.out.println("======================================0===================================");
        get.populateSubset1(DATA, 0, DATA.length);
        System.out.println("======================================1===================================");
//        get.populateSubset2(DATA, 0, DATA.length);
//        System.out.println("======================================2===================================");
    }

    /** Set a value for target sum */
    public static final int TARGET_SUM = 15;

    private Stack<Integer> stack = new Stack<Integer>();

    public void populateSubset(int[] data, int fromIndex, int endIndex) {
      
        if(stack.size() == 5)
        {
            print(stack);
            return;
        }

        for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {

                stack.push(data[currentIndex]);
                populateSubset(data, currentIndex + 1, endIndex);
                stack.pop();
        }
    }
    
    public void populateSubset1(int[] data, int fromIndex, int endIndex) {
        
        for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {

                stack.push(data[currentIndex]);
                print(stack);
                populateSubset1(data, currentIndex + 1, endIndex);
                stack.pop();
        }
    }
    
    public void populateSubset2(int[] data, int fromIndex, int endIndex) {
        
        for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {

                stack.push(data[currentIndex]);
                populateSubset2(data, currentIndex + 1, endIndex);
                print(stack);
                stack.pop();
        }
    }

    /**
     * Print satisfied result. i.e. 15 = 4+6+5
     */

    private void print(Stack<Integer> stack) {
        StringBuilder sb = new StringBuilder();
//        sb.append(TARGET_SUM).append(" = ");
        for (Integer i : stack) {
            sb.append(i).append("+");
        }
        System.out.println(sb.deleteCharAt(sb.length() - 1).toString());
    }
}