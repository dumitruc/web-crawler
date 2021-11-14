package com.dumitruc.training.webcrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class PageOrchestrator implements Runnable {
    private final BlockingQueue upcomingWork;
    private final BlockingQueue foundUrls;

    private static Logger logger = LogManager.getLogger(PageOrchestrator.class);

    public PageOrchestrator(BlockingQueue upcomingWork, BlockingQueue foundUrls) {
        this.upcomingWork = upcomingWork;
        this.foundUrls = foundUrls;
    }


    @Override
    public void run() {
        if (upcomingWork.size() > 0) {
            String urlString = getNextUrlToParse(upcomingWork);
            logger.debug(String.format("Parsing page on URL [%s]", urlString));

            PageParser pageParser = getPageParser(urlString);
            List<String> newlyFoundUrls = pageParser.extractUrls();



            addNewlyFoundUrlsToQueue(newlyFoundUrls);
        }
        logger.debug(String.format("\tNo work, upcoming work queue size: %s", upcomingWork.size()));
    }

    private void addNewlyFoundUrlsToQueue(List<String> newlyFoundUrls) {
        newlyFoundUrls.forEach(nfUrl -> {
            foundUrls.add(nfUrl);
            logger.debug(String.format("\tAdded URL to found queue [%s]", nfUrl));
        });
    }

    public PageParser getPageParser(String initUrl) {
        return new PageParser(initUrl);
    }

    private String getNextUrlToParse(BlockingQueue upcomingWork) {
        String nextUrl = null;

        try {
            nextUrl = upcomingWork.take().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return nextUrl;
    }


}
