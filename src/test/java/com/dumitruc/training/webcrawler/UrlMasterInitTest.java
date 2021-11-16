package com.dumitruc.training.webcrawler;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class UrlMasterInitTest {

    UrlMaster urlMaster;
    LittleCrawler littleCrawler;

    @Before
    public void testInit() {
        this.littleCrawler = new LittleCrawler();
        urlMaster = new UrlMaster(littleCrawler.getFoundUrls(),
                littleCrawler.getUpcomingWork(),
                littleCrawler.getCompletedWork());

    }


    @Test
    public void setStartingUrlInitiatesRootHosts() {
        String inArg = "https://bbc.co.uk";
        urlMaster.setStartingUrls(new String[]{inArg});

        assertThat(UrlMaster.getRootHosts().size(), equalTo(1));
        assertThat(UrlMaster.getRootHosts().get(0), equalTo(getHostFromUrlString(inArg)));
    }

    @Test
    public void setStartingUrlInitiatesInitiallyFoundUrlsToProcess() {
        String inArg = "https://bbc.co.uk";
        urlMaster.setStartingUrls(new String[]{inArg});


        int foundUrlSize = littleCrawler.getFoundUrls().size();
        String nextUrl = littleCrawler.getFoundUrls().poll().getFoundUrls().get(0);

        assertThat(foundUrlSize, equalTo(1));
        assertThat(nextUrl, equalTo(inArg));
    }

    @Test
    public void initialUrlIsDeclinedWhenInvalid() {
        String inArg = "I'm bad!";


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //No exception is thrown
        urlMaster.setStartingUrls(new String[]{inArg});

        int foundUrlSize = littleCrawler.getFoundUrls().size();
        assertThat(foundUrlSize, equalTo(0));
    }

    @Test
    public void userNotifiedInitialUrlIsInvalid() {
        String inArg = "I'm bad!";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //No exception is thrown
        urlMaster.setStartingUrls(new String[]{inArg});

        String expectedOutMessage = String.format("Starting point URL [%s] not valid, please provide " +
                "valid URL, e.g. https://www.bbc.co.uk\r\n", inArg);

        assertThat(outContent.toString(),
                equalTo(expectedOutMessage));
    }


    private String getHostFromUrlString(String s) {
        String host = null;
        try {
            host = (new URL(s)).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return host;
    }

}
