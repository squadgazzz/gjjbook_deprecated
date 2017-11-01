package com.gjjbook.servlet;

import java.util.*;

public class Permute<T extends Comparable> {
//    Set<T[]> resultSet = new HashSet<>();
    List<T[]> resultSet = new ArrayList<>();

    private List<T[]> permute(T[] input) {
        permute(input, 0);

        return resultSet;
    }

    private void permute(T[] input, int k) {
        if (k == input.length) {
            for (int i = 0; i < input.length; i++) {
                resultSet.add(input);
//                System.out.print(" [" + a[i] + "] ");
            }
//            System.out.println();
        } else {
            for (int i = k; i < input.length; i++) {
                T temp = input[k];
                input[k] = input[i];
                input[i] = temp;

                permute(input, k + 1);

                temp = input[k];
                input[k] = input[i];
                input[i] = temp;
            }
        }
    }

    public static void main(String[] args) {
        Permute<String> permute = new Permute<>();
        String testStr = "hi hello bye";
        String[] arr = testStr.split(" ");
        System.out.println(Arrays.deepToString(permute.permute(arr).toArray()));
    }
}
