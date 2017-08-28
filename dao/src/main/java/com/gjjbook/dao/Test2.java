package com.gjjbook.dao;

public class Test2 {
    public static void main(String[] args) {
        System.out.println(method());
    }

    static String method() {
        try {
            return "hi";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("1");
        }
        return "bye";
    }
}
