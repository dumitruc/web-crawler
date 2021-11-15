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

public class UrlMaster implements Runnable {

    private final BlockingQueue<PageUrlDetails> foundUrls;
    private final BlockingQueue<String> upcomingWork;
    private final HashMap<String, PageUrlDetails> completedWork;

    private List<String> rootHosts;
    private List<String> rootUrls;
    private UrlAuditor urlAuditor;

    private static final Logger logger = LogManager.getLogger(UrlMaster.class);

    public UrlMaster(BlockingQueue<PageUrlDetails> foundUrls, BlockingQueue<String> upcomingWork, HashMap<String, PageUrlDetails> completedWork) {
        this.foundUrls = foundUrls;
        this.upcomingWork = upcomingWork;
        this.completedWork = completedWork;
        this.rootHosts = new ArrayList<>();
        this.rootUrls = new ArrayList<>();
        this.urlAuditor = new UrlAuditor();

    }

    @Override
    public void run() {
//        while (true) {
        PageUrlDetails pageUrlDetails = foundUrls.peek();
        completedWork.put(pageUrlDetails.getPageUrl(), pageUrlDetails);
        removeProcessed(pageUrlDetails);
        processNewFoundUrls(pageUrlDetails);
//        }

    }

    private void processNewFoundUrls(PageUrlDetails pud) {
        pud
                .getFoundUrls()
                .forEach(sUrl -> processNextFoundUrl(sUrl));
    }

    private void processNextFoundUrl(String urlString) {
        if (!completedWork.containsKey(urlString) &&
                !upcomingWork.contains(urlString) &&
                urlAuditor.isPassingBusinessRuleValidation(urlString, rootHosts)) {
            addUrlToUpcomingWorkQueue(urlString);
        }
    }

    private void addUrlToUpcomingWorkQueue(String urlString) {
        try {
            upcomingWork.put(urlString);
            logger.info(String.format("Url [%s] moved to the upcoming work que to be processed"));
        } catch (InterruptedException e) {
            logger.error(String.format("Could not add url [%s] to the pending work queue\n%s", urlString, e.getMessage()));
        }
    }

    private void removeProcessed(PageUrlDetails pud) {
        if (!foundUrls.remove(pud)) {
            logger.warn(String.format("Could not remove PageUrlDetails url[%s] from found urls queue", pud.getPageUrl()));
        }
    }


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

    private void addStartPoint(String nfUrl) {
        addCrawlRootLocation(nfUrl);
        try {
            foundUrls.put(new PageUrlDetails(CrawlerConstants.INIT_POINT, rootUrls));
        } catch (InterruptedException e) {
            logger.error(String.format("Could not add [%s] to the found URL queue", nfUrl));
            logger.error(e.getMessage());
        }
    }


    public void addCrawlRootLocation(String argUrl) {
        try {
            rootUrls.add(argUrl);
            URL url = new URL(argUrl);
            String host = url.getHost();
            rootHosts.add(host);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
