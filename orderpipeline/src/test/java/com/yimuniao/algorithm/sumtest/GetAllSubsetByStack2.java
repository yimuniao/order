package com.yimuniao.algorithm.sumtest;

import java.util.Arrays;
import java.util.Stack;

public class GetAllSubsetByStack2 {

    private static final int[] DATA = { 1, 3, 4, 5, 6, 2, 7, 8, 9, 10, 11, 13, 14, 15 };

    public static void main(String[] args) {
        GetAllSubsetByStack2 get = new GetAllSubsetByStack2();
        Arrays.sort(DATA);
        get.populateSubset(DATA, 0, DATA.length);
    }

    /** Set a value for target sum */
    public static final int TARGET_SUM = 15;

    private Stack<Integer> stack = new Stack<Integer>();

    /** Store the sum of current elements stored in stack */

    public void populateSubset(int[] data, int fromIndex, int endIndex) {

        int sumInStack0 = sumstack(stack);
        if (sumInStack0 >= TARGET_SUM) {
            if (sumInStack0 == TARGET_SUM)
            {
                print(stack);
            }
            return;
        }
        
        for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {
            int sumInStack = sumstack(stack);
            if (sumInStack + data[currentIndex] <= TARGET_SUM) {

                stack.push(data[currentIndex]);
                sumInStack += data[currentIndex];

                /*
                 * Make the currentIndex +1, and then use recursion to proceed
                 * further.
                 */
                populateSubset(data, currentIndex + 1, endIndex);
                sumInStack -= (Integer) stack.pop();
            }
        }
    }
    
    private int sumstack(Stack<Integer> stack) {
        StringBuilder sb = new StringBuilder();
        sb.append(TARGET_SUM).append(" = ");
        int sum = 0;
        for (Integer i : stack) {
            sum += i;
        }
        return sum;
    }

    /**
     * Print satisfied result. i.e. 15 = 4+6+5
     */

    private void print(Stack<Integer> stack) {
        StringBuilder sb = new StringBuilder();
        sb.append(TARGET_SUM).append(" = ");
        for (Integer i : stack) {
            sb.append(i).append("+");
        }
        System.out.println(sb.deleteCharAt(sb.length() - 1).toString());
    }
}