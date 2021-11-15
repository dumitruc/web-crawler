package com.dumitruc.training.webcrawler;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

public class PageOrchestratorTest {

    @Test
    public void scansUrlsFromList() throws InterruptedException {
        BlockingQueue<String> upcomingWork = new LinkedBlockingDeque<String>();
        BlockingQueue<PageUrlDetails> foundUrls = new LinkedBlockingDeque<PageUrlDetails>();

        String testUrl = "https://www.bbc.co.uk";

        List<String> resultedUrls = new ArrayList<>();
        resultedUrls.add("https://www.yandex.ru");
        resultedUrls.add("https://www.google.ru");

        PageParser mockPageParser = mock(PageParser.class);
        when(mockPageParser.extractUrls()).thenReturn(resultedUrls);


        PageOrchestrator pageOrchestrator = new PageOrchestrator(upcomingWork, foundUrls);
        PageOrchestrator pageOrchestratorSpy = spy(pageOrchestrator);

        when(pageOrchestratorSpy.getPageParser(testUrl)).thenReturn(mockPageParser);

        upcomingWork.add(testUrl);

        pageOrchestratorSpy.run();

        assertThat("Didn't take element from the queue", upcomingWork.size(), equalTo(0));
        assertThat("Didn't take element from the queue", foundUrls.size(), equalTo(1));
        assertThat("Didn't take element from the queue", ((PageUrlDetails)foundUrls.take()).getFoundUrls().size(), equalTo(2));

    }

}
