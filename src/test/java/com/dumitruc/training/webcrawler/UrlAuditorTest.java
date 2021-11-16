package com.dumitruc.training.webcrawler;

import com.dumitruc.training.webcrawler.urlutils.UrlAuditor;
import com.dumitruc.training.webcrawler.urlutils.UrlMaster;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
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
        invalidUrls.add("gopher://spinaltap.micro.umn.edu/00/Weather/California/Los%20Angeles");
        invalidUrls.add("mailto:mduerst@ifi.unizh.ch");
        invalidUrls.add("news:comp.infosystems.www.servers.unix");
        invalidUrls.add("telnet://melvyl.ucop.edu/");

        invalidUrls.forEach(vu ->
                assertThat(String.format("Expecting invalid URL, [%s] was considered valid", vu),
                        UrlAuditor.isValidUrl(vu),
                        equalTo(false)
                )
        );
    }


    @Test
    public void businessLogicValidation(){

        String host = "www.bbc.com";
        List<String> rootHosts = Collections.singletonList(host);

        String urlString = String.format("https://%s/whatever-next.html", host);

        try (MockedStatic<UrlMaster> theMock = Mockito.mockStatic(UrlMaster.class)) {
            theMock.when(UrlMaster::getRootHosts).thenReturn(rootHosts);

            assertThat(UrlAuditor.isPassingBusinessRuleValidation(urlString), equalTo(true));
            assertThat(UrlAuditor.isPassingBusinessRuleValidation("https://bbc.com"), equalTo(false));
        }

    }

    @Test
    public void unparsablePages() {
        assertThat(UrlAuditor.isParsablePage("https://some-location.com"), equalTo(true));
        assertThat(UrlAuditor.isParsablePage("https://some-location.com/index.html"), equalTo(true));
        assertThat(UrlAuditor.isParsablePage("https://some-location.com/kitty.mp3.info"), equalTo(true));

        assertThat(UrlAuditor.isParsablePage("https://some-location.com/index.png"), equalTo(false));
        assertThat(UrlAuditor.isParsablePage("https://some-location.com/hello.mp3"), equalTo(false));
    }


}
