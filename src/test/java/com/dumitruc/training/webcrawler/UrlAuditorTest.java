package com.dumitruc.training.webcrawler;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


public class UrlAuditorTest {

    @Test
    public void correctlyAcceptsValidUrls() {
        List<String> validUrls = new ArrayList<>();

        validUrls.add("http://bbc.co.uk");
        validUrls.add("https://bbc.co.uk");
        validUrls.add("HTTPS://BBC.CO.UK");
        validUrls.add("HTTPS://b.com");
        validUrls.add("ftp://b.com");
        validUrls.add("https://ru.wikipedia.org/wiki/%D0%92%D0%B8%D0%BA%D0%B8%D0%BF%D0%B5%D0%B4%D0%B8%D1%8F");

        validUrls.forEach(vu ->
                assertThat(String.format("Invalid url [%s]", vu),
                        UrlAuditor.isValidUrl(vu),
                        equalTo(true)
                )
        );
    }

    @Test
    @Ignore("Find a better URL parser")
    public void correctlyAcceptsValidUrlsDifferentCharSet() {
        List<String> validUrls = new ArrayList<>();

        validUrls.add("https://ru.wikipedia.org/wiki/Компьютер");
        validUrls.add("https://i❤.ws"); //Works in Intellij, but not maven cli

        validUrls.forEach(vu ->
                assertThat(String.format("Invalid url [%s]", vu),
                        UrlAuditor.isValidUrl(vu),
                        equalTo(true)
                )
        );
    }

    @Test
    public void correctlyDetectsInvalidUrls() {
        List<String> invalidUrls = new ArrayList<>();

        invalidUrls.add("-------");
        invalidUrls.add("http:/bbc.co.uk");
        invalidUrls.add("http:///bbc.co.uk");
        invalidUrls.add("http://bbc.c");
        invalidUrls.add("http://----");
        invalidUrls.add("www.bbc.co.uk");
        invalidUrls.add("bbc.co.uk");

        invalidUrls.forEach(vu ->
                assertThat(String.format("Expecting invalid URL, [%s] was considered valid", vu),
                        UrlAuditor.isValidUrl(vu),
                        equalTo(false)
                )
        );
    }

}
