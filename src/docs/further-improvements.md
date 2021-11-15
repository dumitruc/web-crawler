# Intro
This document describes the various ideas and suggestions that are not of immediate concern but will benefit the product.

# Considerations
* Multi threads and locking
* Thread crashing before parse completion
* Display ALL links but crawl only same domain
* Protocol (https/http)
* Page redirects
* Accept cookies banner
* More robust URL parsing capabilities, see [galimatias](https://github.com/smola/galimatias) library and comments
* External library to add unparsable URL types, e.g. media
* Continuous printout of the completed work


# Suggestions
* change to a JRE docker image to minimise footprint
* wrap up the ques and the list
* persistency, persist save state and results
* use browser engine parsing to explore the javascript rendered content, i.e. infinite scrolling


# Decisions
## Single thread object to process URLs
Avoid complex implementation to parsers to handle duplicate URLs. If we go with recursion, at some point we'll have multiple threads trying to add same URL to completed work list, either lock(becomes slow) or endup with duplicates. Feels like for the size (eg on same host), this is better.
## Use of the shelf library to handles web page parsing
Rather than building independent web page parsing mechanism, with all the exceptions and edge case scenario, use an off the shelf solution to handle web page, [jsop](https://jsoup.org/cookbook/input/load-document-from-url) library. On initial inspection quite popular, supports HTML5, very much alive, last commit 12 days ago.
## Go with Apache common
More advanced as from various articles built in java URL is not very good

# Known limitations
## Wasted effort/processing
~~There are cases when same page is parsed by multiple threads.~~
Sorted - was looking at java 7 api docs.
