package com.dumitruc.training.webcrawler;

import com.dumitruc.training.webcrawler.pageworker.PageUrlDetails;
import com.dumitruc.training.webcrawler.urlutils.UrlAuditor;
import com.dumitruc.training.webcrawler.urlutils.UrlMaster;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

public class UrlMasterRunTest {

    LittleCrawler littleCrawler;

    @Before
    public void testInit() {
        this.littleCrawler = new LittleCrawler();
    }

    @Test
    public void anyValidUrlIsMovedFromFoundIntoPendingWork() {

        UrlMaster urlMaster = new UrlMaster(littleCrawler.getFoundUrls(),
                littleCrawler.getUpcomingWork(),
                littleCrawler.getCompletedWork());

        PageUrlDetails pud = new PageUrlDetails("about:blank", Arrays.asList("https://bbc.com"));
        littleCrawler.getFoundUrls().add(pud);

        assertThat(littleCrawler.getUpcomingWork().size(), equalTo(0));
        assertThat(littleCrawler.getFoundUrls().size(), equalTo(1));

        try (MockedStatic<UrlAuditor> theMock = Mockito.mockStatic(UrlAuditor.class)) {
            theMock.when(() -> UrlAuditor.isPassingBusinessRuleValidation(anyString())).thenReturn(true);

            urlMaster.run();
        }
        assertThat(littleCrawler.getUpcomingWork().size(), equalTo(1));
        assertThat(littleCrawler.getFoundUrls().size(), equalTo(0));
    }


    @Test
    public void allValidUrlAreMovedFromFoundIntoPendingWork() {

        UrlMaster urlMaster = new UrlMaster(littleCrawler.getFoundUrls(),
                littleCrawler.getUpcomingWork(),
                littleCrawler.getCompletedWork());

        PageUrlDetails pud = new PageUrlDetails("about:blank", Arrays.asList("https://bbc.com", "https://www.google.com"));
        littleCrawler.getFoundUrls().add(pud);

        assertThat(littleCrawler.getUpcomingWork().size(), equalTo(0));
        assertThat(littleCrawler.getFoundUrls().size(), equalTo(1));

        try (MockedStatic<UrlAuditor> theMock = Mockito.mockStatic(UrlAuditor.class)) {
            theMock.when(() -> UrlAuditor.isPassingBusinessRuleValidation(anyString())).thenReturn(true);

            urlMaster.run();
        }
        assertThat(littleCrawler.getUpcomingWork().size(), equalTo(2));
        assertThat(littleCrawler.getFoundUrls().size(), equalTo(0));
    }

    @Test
    public void processedPageIsAddedToCompletedWork() {

        UrlMaster urlMaster = new UrlMaster(littleCrawler.getFoundUrls(),
                littleCrawler.getUpcomingWork(),
                littleCrawler.getCompletedWork());

        PageUrlDetails pud = new PageUrlDetails("about:blank", Arrays.asList("https://bbc.com", "https://www.google.com"));
        littleCrawler.getFoundUrls().add(pud);

        assertThat(littleCrawler.getCompletedWork().size(), equalTo(0));

        try (MockedStatic<UrlAuditor> theMock = Mockito.mockStatic(UrlAuditor.class)) {
            theMock.when(() -> UrlAuditor.isPassingBusinessRuleValidation(anyString())).thenReturn(true);

            urlMaster.run();
        }
        assertThat(littleCrawler.getCompletedWork().size(), equalTo(1));
        assertThat(littleCrawler.getCompletedWork().get(pud.getPageUrl()), equalTo(pud));

    }

    @Test
    public void duplicateUrlsAreNotAddedToCompletedWork() {

        UrlMaster urlMaster = new UrlMaster(littleCrawler.getFoundUrls(),
                littleCrawler.getUpcomingWork(),
                littleCrawler.getCompletedWork());

        PageUrlDetails pud = new PageUrlDetails("about:blank", Arrays.asList("https://bbc.com", "https://www.google.com"));
        littleCrawler.getFoundUrls().add(pud);
        littleCrawler.getCompletedWork().put(pud.getPageUrl(), pud);

        assertThat(littleCrawler.getCompletedWork().size(), equalTo(1));

        try (MockedStatic<UrlAuditor> theMock = Mockito.mockStatic(UrlAuditor.class)) {
            theMock.when(() -> UrlAuditor.isPassingBusinessRuleValidation(anyString())).thenReturn(true);

            urlMaster.run();
        }
        assertThat(littleCrawler.getCompletedWork().size(), equalTo(1));
        assertThat(littleCrawler.getCompletedWork().get(pud.getPageUrl()), equalTo(pud));

    }


    @Test
    public void onlyNonDuplicatesAreAddedToUpcomingWorkQueue() {

        UrlMaster urlMaster = new UrlMaster(littleCrawler.getFoundUrls(),
                littleCrawler.getUpcomingWork(),
                littleCrawler.getCompletedWork());

        PageUrlDetails pud = new PageUrlDetails("https://bbc.com", Arrays.asList("https://bbc.com", "https://www.google.com"));
        littleCrawler.getFoundUrls().add(pud);
        littleCrawler.getCompletedWork().put(pud.getPageUrl(), pud);

        assertThat(littleCrawler.getUpcomingWork().size(), equalTo(0));

        try (MockedStatic<UrlAuditor> theMock = Mockito.mockStatic(UrlAuditor.class)) {
            theMock.when(() -> UrlAuditor.isPassingBusinessRuleValidation(anyString())).thenReturn(true);

            urlMaster.run();
        }
        assertThat(littleCrawler.getUpcomingWork().size(), equalTo(1));

    }


    @Test
    public void onlyPassingBusinessRuleValidationMovedToUwk() {

        UrlMaster urlMaster = new UrlMaster(littleCrawler.getFoundUrls(),
                littleCrawler.getUpcomingWork(),
                littleCrawler.getCompletedWork());

        String failBusinessRulesUrl = "https://www.google.com/some.jpg";
        PageUrlDetails pud = new PageUrlDetails("about:blank", Arrays.asList("https://bbc.com", failBusinessRulesUrl));
        littleCrawler.getFoundUrls().add(pud);
        littleCrawler.getCompletedWork().put(pud.getPageUrl(), pud);

        assertThat(littleCrawler.getUpcomingWork().size(), equalTo(0));

        try (MockedStatic<UrlAuditor> theMock = Mockito.mockStatic(UrlAuditor.class)) {
            theMock.when(() -> UrlAuditor.isPassingBusinessRuleValidation(anyString())).thenReturn(true);
            theMock.when(() -> UrlAuditor.isPassingBusinessRuleValidation(failBusinessRulesUrl)).thenReturn(false);

            urlMaster.run();
        }
        assertThat(littleCrawler.getUpcomingWork().size(), equalTo(1));

    }

}
