package com.yimuniao.algorithm.sumtest;

import java.util.Arrays;

public class AllSort {

    public static void Arrange(char arr[], int st, int ed) {
        if (st == ed)
            System.out.println(Arrays.toString(arr));
        else {
            for (int i = st; i < ed; i++) {
                Swap(arr, st, i);
                Arrange(arr, st + 1, ed);
                Swap(arr, i, st);
            }
        }
    }

    public static void Swap(char arr[], int i, int j) {
        if (i == j)
            return;
        else {
            char temp = arr[j];
            arr[j] = arr[i];
            arr[i] = temp;
        }
    }

    public static void main(String[] args) {
        char array[] = { 'a', 'b', 'c', 'd' };
        Arrange(array, 0, array.length);
    }
}