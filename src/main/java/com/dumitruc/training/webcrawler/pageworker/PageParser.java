package com.dumitruc.training.webcrawler.pageworker;

import com.dumitruc.training.webcrawler.CrawlerConstants;
import com.dumitruc.training.webcrawler.urlutils.UrlAuditor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class PageParser {

    private final String pageUrl;
    private static final Logger logger = LogManager.getLogger(PageParser.class);

    public PageParser(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Document getWebPage() throws IOException {
        return Jsoup.connect(pageUrl).get();
    }

    private Elements extractAllElements() throws IOException {
        Document document = getWebPage();
        return document.body().getAllElements();
    }

    private String urlFromElement(Element el) {
        String urlString = null;
        String validUrl = el.absUrl(CrawlerConstants.URL_PAGE_PARSER_KEY);
        if (UrlAuditor.isValidUrl(validUrl)) {
            urlString = validUrl;
        }
        return urlString;
    }

    private List<String> extractUrlContenders(Elements pageElements) {
        return pageElements
                .stream()
                .map(el -> urlFromElement(el))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<String> extractUrls() {
        List<String> potentialUrls = new ArrayList<>();
        Elements elements;
        try {
            elements = extractAllElements();
            potentialUrls = extractUrlContenders(elements);
        } catch (IOException e) {
            logger.error(String.format("Couldn't parse the page of url [%s]\n\n%s", pageUrl, e.getMessage()));
        }
        return potentialUrls;
    }
}
