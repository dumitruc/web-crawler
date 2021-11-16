package com.dumitruc.training.webcrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class PageOrchestrator implements Runnable {
    private final BlockingQueue<String> upcomingWork;
    private final BlockingQueue<PageUrlDetails> foundUrls;

    private static final Logger logger = LogManager.getLogger(PageOrchestrator.class);

    public PageOrchestrator(BlockingQueue<String> upcomingWork, BlockingQueue<PageUrlDetails> foundUrls) {
        this.upcomingWork = upcomingWork;
        this.foundUrls = foundUrls;
    }


    @Override
    public void run() {
        String urlString = getNextUrlToParse(upcomingWork);
        if (urlString != null) {
            logger.debug(String.format("Parsing page on URL [%s]", urlString));

            PageParser pageParser = getPageParser(urlString);
            List<String> newlyFoundUrls = pageParser.extractUrls();

            PageUrlDetails pageUrlDetails = new PageUrlDetails(urlString, newlyFoundUrls);

            addNewlyFoundUrlsToQueue(pageUrlDetails);
        }
    }

    private void addNewlyFoundUrlsToQueue(PageUrlDetails newlyFoundUrls) {
        foundUrls.add(newlyFoundUrls);
        logger.debug(String.format("\tAdded URLs found on [%s] to found queue ", newlyFoundUrls.getPageUrl()));
    }

    public PageParser getPageParser(String initUrl) {
        return new PageParser(initUrl);
    }

    private String getNextUrlToParse(BlockingQueue<String> upcomingWork) {
        return upcomingWork.poll();
    }

}
