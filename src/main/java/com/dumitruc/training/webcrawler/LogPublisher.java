package com.dumitruc.training.webcrawler;

import com.dumitruc.training.webcrawler.pageworker.PageUrlDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.stream.Collectors;

public class LogPublisher {

    HashMap<String, PageUrlDetails> completedWork;
    private static final Logger logger = LogManager.getLogger(LogPublisher.class);

    public LogPublisher(HashMap<String, PageUrlDetails> completedWork) {
        this.completedWork = completedWork;
    }

    public void consoleOutAll() {
        completedWork.forEach((k, v) -> {
            System.out.println(k);
            String allUrls = v.getFoundUrls().stream().collect(Collectors.joining(","));
            System.out.println(String.format("\t\t\t[%s]", allUrls));

            logger.info(String.format("============================\n[%s]\t\t\t[%s]", k, allUrls));
        });
    }
}
