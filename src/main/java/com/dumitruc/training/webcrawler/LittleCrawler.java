package com.dumitruc.training.webcrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class LittleCrawler {

    private static Logger logger = LogManager.getLogger(UrlAuditor.class);

    BlockingQueue upcomingWork = new LinkedBlockingDeque();
    BlockingQueue foundUrls = new LinkedBlockingDeque();
    HashMap<String, PageUrlDetails> completedWork = new HashMap<>();
    final List<String> homePool = new ArrayList<>();



    public void run(String[] args) {

        setHome(args);

        //Thread pools to enable control
        ExecutorService pageParsers = Executors.newCachedThreadPool();
        ExecutorService urlMasterService = Executors.newSingleThreadExecutor();

        // TODO: 14/11/2021 conditions to be decided
        while (true) {
            urlMasterService.submit(new UrlMaster());

            // TODO: 14/11/2021 conditions to be decided
            if(true){
                pageParsers.submit(new PageOrchestrator(upcomingWork, foundUrls));
            }

        }




        // stop in-flight threads

        //Print out the results

    }

    public void setHome(String[] args) {
        Arrays.stream(args).forEach(url -> {
            if (UrlAuditor.isValidUrl(url)) {
                addNewFoundUrl(url);
            }
        });
    }

    private void addNewFoundUrl(String nfUrl) {
        try {
            foundUrls.put(nfUrl);
        } catch (InterruptedException e) {
            logger.error(String.format("Could not add [%s] to the found URL queue", nfUrl));
            logger.error(e.getMessage());
        }
    }

}
