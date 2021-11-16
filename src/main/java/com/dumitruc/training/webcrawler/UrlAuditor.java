package com.dumitruc.training.webcrawler;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class UrlAuditor {

    private static final Logger logger = LogManager.getLogger(UrlAuditor.class);

    public static boolean isValidUrl(String urlString) {
        return UrlValidator.getInstance().isValid(urlString);
    }

    public boolean isPassingBusinessRuleValidation(String urlString, List<String> rootHosts) {
        logger.debug(String.format("Validating business rules: [%s]", urlString));
        return isSameRoot(urlString, rootHosts)
                && isParsablePage(urlString);
    }

    public boolean isSameRoot(String urlString, List<String> rootHostStrings) {
        String host = convertToUrl(urlString).getHost();

        return rootHostStrings.contains(host);
    }

    // TODO: 15/11/2021 Test logic!!!
    public boolean isParsablePage(String urlString) {
        //Is not a parsable page if is an image or any other media type
        boolean isItParsablePage = !Arrays.stream(CrawlerConstants.NON_PAGE_SUFFIXES)
                .map(endings -> urlString.endsWith(endings))
                .collect(Collectors.toList())
                .contains(true);
        if (!isItParsablePage) {
                logger.debug(String.format("[%s] url is not a parsable page", urlString));
        }
        return isItParsablePage;
    }

    private URL convertToUrl(String stringUrl) {
        URL convertedUrl = null;
        try {
            convertedUrl = new URL(stringUrl);
        } catch (MalformedURLException e) {
            logger.warn(String.format("Could not convert string [%s] to url\n%s", stringUrl, e.getMessage()));
        }
        return convertedUrl;
    }

}
