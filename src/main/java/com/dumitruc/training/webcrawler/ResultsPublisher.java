package com.dumitruc.training.webcrawler;

import java.util.HashMap;
import java.util.stream.Collectors;

public class ResultsPublisher {

    HashMap<String, PageUrlDetails> completedWork;

    public ResultsPublisher(HashMap<String, PageUrlDetails> completedWork) {
        this.completedWork = completedWork;
    }

    public void consoleOutAll(){
        completedWork.forEach((k,v)->{
            System.out.println(String.format("[%s]",k));
            String allUrls = v.getFoundUrls().stream().collect(Collectors.joining(","));
            System.out.println(String.format("\t\t\t[%s]",allUrls));
        });
    }
}
