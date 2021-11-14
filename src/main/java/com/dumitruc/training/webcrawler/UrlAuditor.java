package com.dumitruc.training.webcrawler;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UrlAuditor {

    private static Logger logger = LogManager.getLogger(UrlAuditor.class);

    public static boolean isValidUrl(String urlString) {
        return UrlValidator.getInstance().isValid(urlString);
    }
}
