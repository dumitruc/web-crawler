package com.dumitruc.training.webcrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.stream.Collectors;

public class ResultsPublisher {

    HashMap<String, PageUrlDetails> completedWork;
    private static final Logger logger = LogManager.getLogger(ResultsPublisher.class);

    public ResultsPublisher(HashMap<String, PageUrlDetails> completedWork) {
        this.completedWork = completedWork;
    }

    public void consoleOutAll(){
        completedWork.forEach((k,v)->{
            System.out.println(String.format("[%s]",k));
            String allUrls = v.getFoundUrls().stream().collect(Collectors.joining(","));
            System.out.println(String.format("\t\t\t[%s]",allUrls));

            logger.info(String.format("============================\n[%s]\t\t\t[%s]",k,allUrls));
        });
    }
}
