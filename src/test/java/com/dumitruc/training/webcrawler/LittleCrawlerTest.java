package com.dumitruc.training.webcrawler;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


public class LittleCrawlerTest {

    @Test
    public void initTakesValidUrls(){
        LittleCrawler littleCrawler = new LittleCrawler();
        String urlString = "https://www.bbc.co.uk";
        littleCrawler.setHome(new String[]{urlString});

        assertThat(littleCrawler.foundUrls.size(),equalTo(1));
        assertThat(littleCrawler.foundUrls.peek(),equalTo(urlString));
    }

    @Test
    public void initIgnoresInvalidUrls(){
        LittleCrawler littleCrawler = new LittleCrawler();
        String urlString = "this is not a url";
        littleCrawler.setHome(new String[]{urlString});

        assertThat(littleCrawler.foundUrls.size(),equalTo(0));
    }

    @Test
    public void initiIgnoresInvalidUrlsRegistersValidOnes(){
        LittleCrawler littleCrawler = new LittleCrawler();
        String urlString = "https://www.bbc.co.uk";
        littleCrawler.setHome(new String[]{"some invalid URL",urlString,"https://"});
        assertThat(littleCrawler.foundUrls.size(),equalTo(1));
        assertThat(littleCrawler.foundUrls.peek(),equalTo(urlString));

    }


}
