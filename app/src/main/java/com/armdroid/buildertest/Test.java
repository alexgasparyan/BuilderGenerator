package com.armdroid.buildertest;

public class Test {

    public static void main(String[] args) {

        User user = new UserBuilder("Alex", "Gasparyan")
                .setAge(22)
                .setHeight(180)
                .build();
    }

}
