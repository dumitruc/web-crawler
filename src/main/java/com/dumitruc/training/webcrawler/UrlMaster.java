package com.dumitruc.training.webcrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class UrlMaster implements Runnable {

    private final BlockingQueue<PageUrlDetails> foundUrls;
    private final BlockingQueue<String> upcomingWork;
    private final HashMap<String, PageUrlDetails> completedWork;

    private static List<String> rootHosts;

    private static final Logger logger = LogManager.getLogger(UrlMaster.class);

    // TODO: 16/11/2021 Ideally insteada of using this in constructor, call these queues from LittleCrawler
    public UrlMaster(BlockingQueue<PageUrlDetails> foundUrls, BlockingQueue<String> upcomingWork, HashMap<String, PageUrlDetails> completedWork) {
        this.foundUrls = foundUrls;
        this.upcomingWork = upcomingWork;
        this.completedWork = completedWork;
        rootHosts = new ArrayList<>();
    }

    @Override
    public void run() {
        PageUrlDetails pageUrlDetails = getPageUrlDetailsFromQueue();
        if(pageUrlDetails!=null){
            completedWork.put(pageUrlDetails.getPageUrl(), pageUrlDetails);
            processNewFoundUrls(pageUrlDetails);
        }
    }

    private PageUrlDetails getPageUrlDetailsFromQueue() {
        PageUrlDetails pageUrlDetails = null;
        try {
            pageUrlDetails = foundUrls.poll(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.info(String.format("Could not retrieve from the found urls queue\n%s", e.getMessage()));
        }
        return pageUrlDetails;
    }

    private void processNewFoundUrls(PageUrlDetails pud) {
        pud
                .getFoundUrls()
                .forEach(sUrl -> processNextFoundUrl(sUrl));
    }

    private void processNextFoundUrl(String urlString) {
        if (!completedWork.containsKey(urlString) &&
                !upcomingWork.contains(urlString) &&
                UrlAuditor.isPassingBusinessRuleValidation(urlString)) {
            addUrlToUpcomingWorkQueue(urlString);
        }
    }

    private void addUrlToUpcomingWorkQueue(String urlString) {
        try {
            upcomingWork.put(urlString);
            logger.info(String.format("Url [%s] moved to the upcoming work que to be processed", urlString));
        } catch (InterruptedException e) {
            logger.error(String.format("Could not add url [%s] to the pending work queue\n%s", urlString, e.getMessage()));
        }
    }

    // TODO: 16/11/2021 Ideally this would be part of constructor
    public void setStartingUrls(String[] args) {
        if (args.length == 0) {
            logger.error("No initial URL provided.");
            System.out.println("Please provided starting URL.");
        }
        Arrays.stream(args).forEach(url -> {
            if (UrlAuditor.isValidUrl(url)) {
                addStartPoint(url);
            } else {
                String initErrorMessage = String.format("Starting point URL [%s] not valid, please provide valid URL, e.g. https://www.bbc.co.uk", url);
                logger.error(initErrorMessage);
                System.out.println(initErrorMessage);
            }

        });
    }

    private void addStartPoint(String initUrl) {
        List<String> rootUrls = new ArrayList<>();
        rootUrls.add(initUrl);
        addCrawlRootLocation(initUrl);
        try {
            foundUrls.put(new PageUrlDetails(CrawlerConstants.INIT_POINT, rootUrls));
        } catch (InterruptedException e) {
            logger.error(String.format("Could not add [%s] to the found URL queue", initUrl));
            logger.error(e.getMessage());
        }
    }


    private void addCrawlRootLocation(String argUrl) {
        try {
            URL url = new URL(argUrl);
            String host = url.getHost();
            rootHosts.add(host);
        } catch (MalformedURLException e) {
            logger.error(String.format("Input argument can not be converted to URL [%s]", e.getMessage()));
        }
    }

    public static List<String> getRootHosts() {
        return rootHosts;
    }

}
