package com.dumitruc.training.webcrawler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

public class UrlMasterQAccessTest {

    LittleCrawler littleCrawler;

    @Before
    public void testInit() {
        this.littleCrawler = new LittleCrawler();
    }

    @Test
    public void queueAllQuesEmpty() {
        UrlMaster urlMaster = new UrlMaster(littleCrawler.getFoundUrls(),
                littleCrawler.getUpcomingWork(),
                littleCrawler.getCompletedWork());

        assertThat(littleCrawler.getUpcomingWork().size(), equalTo(0));
        assertThat(littleCrawler.getCompletedWork().size(), equalTo(0));
        assertThat(littleCrawler.getFoundUrls().size(), equalTo(0));

        try (MockedStatic<UrlAuditor> theMock = Mockito.mockStatic(UrlAuditor.class)) {
            theMock.when(() -> UrlAuditor.isPassingBusinessRuleValidation(anyString())).thenReturn(true);

            urlMaster.run();
        }
        assertThat(littleCrawler.getUpcomingWork().size(), equalTo(0));
        assertThat(littleCrawler.getCompletedWork().size(), equalTo(0));
        assertThat(littleCrawler.getFoundUrls().size(), equalTo(0));
    }

}
