# Purpose

This is a small application to do basic web crawling of a domain.

# Requirements

Given a starting URL, the crawler should visit each URL it finds on the same domain.
It should print each URL visited, and a list of links found on that page.
The crawler should be limited to one subdomain - so when you start with *https://monzo.com/*,
    it would crawl all pages on the monzo.com website,
    but not follow external links,
    for example to facebook.com or community.monzo.com.

# Extra considerations
Do not use frameworks like scrapy or go-colly which handle all the crawling behind the scenes or someone else's code.
You are welcome to use libraries to handle things like HTML parsing.

Ideally, write it as you would a production piece of code.
* High priority
 ** software design
 ** program structure
 ** Identify trade-offs
 ** use of concurrency

* Low priority
 ** UI Design
 ** Sitemap

 This means that we care less about a fancy UI or sitemap format, and more about how your
 program is structured: the trade-offs you've made, what behaviour the program exhibits, and your use of concurrency,
 test coverage, and so on.

We'd love it if you could do this within the next week or so, but let me know if you'll need more time. Once you have submitted your task we will then schedule a 45 minute hangout with an engineer during which you'll share your screen and discuss your implementation.

When you're ready, please submit your solution through the link below as a ZIP file.