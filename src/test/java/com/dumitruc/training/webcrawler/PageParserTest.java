package com.dumitruc.training.webcrawler;

import com.dumitruc.training.webcrawler.pageworker.PageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


public class PageParserTest {


    @Test
    public void checkItCanParsePage() throws IOException {
        PageParser pageParser = new PageParser("http://bbc.co.uk");
        PageParser spiedPageParser = spy(pageParser);

        Document document = Jsoup.parse(generateHtmlDoc("https://www.google.com"));

        when(spiedPageParser.getWebPage()).thenReturn(document);

        assertThat(spiedPageParser.extractUrls(), hasItems("https://www.google.com"));
    }

    @Test
    public void checkItCanExtractMultipleReferences() throws IOException {
        PageParser pageParser = new PageParser("http://bbc.co.uk");
        PageParser spiedPageParser = spy(pageParser);

        Document document = Jsoup.parse(generateHtmlDoc("https://www.google.com", "https://www.bbc.co.uk"));

        when(spiedPageParser.getWebPage()).thenReturn(document);

        assertThat(spiedPageParser.extractUrls(), hasItems("https://www.google.com", "https://www.bbc.co.uk"));
    }

    @Test
    public void skipsBadUrls() throws IOException {
        PageParser pageParser = new PageParser("http://bbc.co.uk");
        PageParser spiedPageParser = spy(pageParser);

        Document document = Jsoup.parse(generateHtmlDoc("https://www.google.com","","h t t p : / /", "https://www.bbc.co.uk"));

        when(spiedPageParser.getWebPage()).thenReturn(document);

        assertThat(spiedPageParser.extractUrls(), hasItems("https://www.google.com", "https://www.bbc.co.uk"));
        assertThat(spiedPageParser.extractUrls().size(), equalTo(2));
    }

    public String generateHtmlDoc(String... urls) {
        String htmlTemplate = "<html>%s</html>";
        String urlPart = "<div><a href=\"%s\"><span>please click me to go to %s</span></a></div>";
        String body = Arrays.stream(urls)
                .map(u -> String.format(urlPart, u, u))
                .collect(Collectors.joining());
        return String.format(htmlTemplate, body);
    }

}
