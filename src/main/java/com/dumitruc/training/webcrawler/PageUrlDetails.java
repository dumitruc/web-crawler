package com.dumitruc.training.webcrawler;

import java.util.List;

public class PageUrlDetails {
    private String pageUrl;
    private List<String> foundUrls;

    public PageUrlDetails(String pageUrl, List<String> foundUrls) {
        this.pageUrl = pageUrl;
        this.foundUrls = foundUrls;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public List<String> getFoundUrls() {
        return foundUrls;
    }

    public void setFoundUrls(List<String> foundUrls) {
        this.foundUrls = foundUrls;
    }
}
