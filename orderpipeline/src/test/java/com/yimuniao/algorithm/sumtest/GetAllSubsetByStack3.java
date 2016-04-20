package com.yimuniao.algorithm.sumtest;

import java.util.Arrays;
import java.util.Stack;

public class GetAllSubsetByStack3 {

    private static final int[] DATA = { 1, 3, 4, 5, 6, 2, 7, 8, 9, 10, 11, 13, 14, 15 };

    public static void main(String[] args) {
        GetAllSubsetByStack3 get = new GetAllSubsetByStack3();
        Arrays.sort(DATA);
        get.populateSubset(DATA, 0, DATA.length);
//        System.out.println("=============================================================");
//        get.populateSubset1(DATA, 0, DATA.length);

    }

    /** Set a value for target sum */
    public static final int TARGET_SUM = 15;

    private Stack<Integer> stack = new Stack<Integer>();

    public void populateSubset(int[] data, int fromIndex, int endIndex) {
        if (DATA[0] > TARGET_SUM) {
            return;
        }
        int i = 0;
        // for (; i < endIndex;)
        while (i < endIndex) {
            int sumstack = sumstack(stack);
            int sum = DATA[i] + sumstack;

            if (sum == TARGET_SUM) {
                stack.push(i);
                i++;
                print(stack);

                int last = stack.pop();
                if (stack.isEmpty()) {
                    return;
                }

                last = stack.pop();
                i = ++last;

            } else if (sum < TARGET_SUM) {
                stack.push(i);
                i++;
            } else {
                if (!stack.isEmpty()) {
                    int last = stack.pop();
                    i = ++last;
                } else {
                    return;
                }

            }
        }
    }

    public void populateSubset1(int[] data, int fromIndex, int endIndex) {
        if (DATA[0] > TARGET_SUM) {
            return;
        }
        int i = 0;
        // for (; i < endIndex;)
        while (i < endIndex) {
            stack.push(i);
            i++;
            print(stack);
            if (i == endIndex)
            {
                int last = stack.pop();
                if (stack.isEmpty()) {
                    return;
                }
                last = stack.pop();
                i = ++last;
            }
            
        }
    }

    private int sumstack(Stack<Integer> stack) {
        int sum = 0;
        for (Integer i : stack) {
            sum += DATA[i];
        }
        return sum;
    }

    /**
     * Print satisfied result. i.e. 15 = 4+6+5
     */

    private void print(Stack<Integer> stack) {
        StringBuilder sb = new StringBuilder();
        sb.append(TARGET_SUM).append(" == ");
        for (Integer i : stack) {
            sb.append(DATA[i]).append("+");
        }
        System.out.println(sb.deleteCharAt(sb.length() - 1).toString());
    }
}