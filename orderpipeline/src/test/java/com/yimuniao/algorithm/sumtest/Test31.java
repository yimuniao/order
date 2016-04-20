package com.yimuniao.algorithm.sumtest;

public class Test31 {
    public static void main(String[] args) {
        int[] ia = { 1, 2, 3, 4 };
        int n = 4;
        permutation(ia, n);
    }

    public static void permutation(int[] ia, int n) {
        permutation("", ia, n);
    }

    public static void permutation(String s, int[] ia, int n) {
        if (n == 1) {
            for (int i = 0; i < ia.length; i++) {
                System.out.println(s + ia[i]);
            }
        } else {
            for (int i = 0; i < ia.length; i++) {
                String ss = "";
                ss = s + ia[i] + ", ";
                int[] ii = new int[ia.length - 1];
                int index = 0;
                for (int j = 0; j < ia.length; j++) {
                    if (j != i) {
                        ii[index++] = ia[j];
                    }
                }

                permutation(ss, ii, n - 1);
            }
        }
    }

    public static void combination(int[] ia, int n) {

    }
}