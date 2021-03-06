package com.dumitruc.training.webcrawler;

import com.dumitruc.training.webcrawler.pageworker.PageOrchestrator;
import com.dumitruc.training.webcrawler.pageworker.PageUrlDetails;
import com.dumitruc.training.webcrawler.urlutils.UrlMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.*;

public class LittleCrawler {

    private static final Logger logger = LogManager.getLogger(LittleCrawler.class);

    private final BlockingQueue<String> upcomingWork;
    private final BlockingQueue<PageUrlDetails> foundUrls;
    private final HashMap<String, PageUrlDetails> completedWork;
    public UrlMaster urlMaster;
    private Integer peakParsersThreads = 0;


    public LittleCrawler() {
        upcomingWork = new LinkedBlockingDeque<>();
        foundUrls = new LinkedBlockingDeque<>();
        completedWork = new HashMap<>();
        urlMaster = new UrlMaster(foundUrls, upcomingWork, completedWork);
    }

    public void run(String[] args) {

        initiateCrawlingRoot(args);

        //Thread pools to enable control
        ExecutorService pageParsers = Executors.newCachedThreadPool();
        ExecutorService urlMasterService = Executors.newFixedThreadPool(1);

        while (isMoreCrawlingNeeded(upcomingWork, foundUrls, pageParsers, urlMasterService)) {
            urlMasterService.submit(urlMaster);
            PageOrchestrator pageOrchestrator = new PageOrchestrator(upcomingWork, foundUrls);
            pageParsers.submit(pageOrchestrator);

            systemMonitor(pageParsers, urlMasterService);
            bePolite();
        }

        closeTheThreads(pageParsers, urlMasterService);

        (new LogPublisher(completedWork)).consoleOutAll();
        systemMonitor(pageParsers, urlMasterService);
    }

    /**
     * This method is to add some sort of delay/control as not to overwhelm the websites being crawled.
     */
    private void bePolite() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void systemMonitor(ExecutorService pageParsers, ExecutorService urlMasterService) {
        int currentParserThreadCount = ((ThreadPoolExecutor) pageParsers).getActiveCount();
        if (currentParserThreadCount > peakParsersThreads) {
            peakParsersThreads = currentParserThreadCount;
        }

        logger.debug("========================================================================================");
        logger.debug("peakParsersThreads = " + peakParsersThreads);
        logger.debug("foundUrls.size() = " + foundUrls.size());
        logger.debug("upcomingWork.size() = " + upcomingWork.size());
        logger.debug("completedWork.size() = " + completedWork.size());
        logger.debug("((ThreadPoolExecutor)pageParsers).getActiveCount() = " + currentParserThreadCount);
        logger.debug("((ThreadPoolExecutor)urlMasterService).getActiveCount() = " + ((ThreadPoolExecutor) urlMasterService).getActiveCount());
    }

    public void initiateCrawlingRoot(String[] args) {
        urlMaster.setStartingUrls(args);
    }

    private void closeTheThreads(ExecutorService pageParsers, ExecutorService urlMasterService) {
        pageParsers.shutdownNow();
        urlMasterService.shutdownNow();
    }

    private boolean isMoreCrawlingNeeded(BlockingQueue<String> upcomingWork,
                                         BlockingQueue<PageUrlDetails> foundUrls,
                                         ExecutorService pageParsers,
                                         ExecutorService urlMasterService) {

        return upcomingWork.size() > 0
                || ((ThreadPoolExecutor) pageParsers).getActiveCount() > 0
                || ((ThreadPoolExecutor) urlMasterService).getActiveCount() > 0
                || foundUrls.size() > 0;
    }

    public BlockingQueue<String> getUpcomingWork() {
        return upcomingWork;
    }

    public BlockingQueue<PageUrlDetails> getFoundUrls() {
        return foundUrls;
    }

    public HashMap<String, PageUrlDetails> getCompletedWork() {
        return completedWork;
    }
}
