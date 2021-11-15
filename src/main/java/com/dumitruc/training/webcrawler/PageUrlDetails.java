package com.dumitruc.training.webcrawler;

import java.util.List;

public class PageUrlDetails {
    private final String pageUrl;
    private final List<String> foundUrls;

    public PageUrlDetails(String pageUrl, List<String> foundUrls) {
        this.pageUrl = pageUrl;
        this.foundUrls = foundUrls;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public List<String> getFoundUrls() {
        return foundUrls;
    }

}
