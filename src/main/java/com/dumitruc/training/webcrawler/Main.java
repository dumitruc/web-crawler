package com.dumitruc.training.webcrawler;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Arrays.stream(args).forEach(System.out::println);

        LittleCrawler littleCrawler = new LittleCrawler();
        littleCrawler.run(args);
    }
}
