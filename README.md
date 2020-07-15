# Java-Reddit-WallStreetBets-Scraper

![](https://github.com/jarrettdev/Java-Reddit-WallStreetBets-Scraper/blob/master/resources/WSB%20pic.jpg)

## What is this?

-This is my first personal project. 

-I included it because I feel like it's important to reflect on the past and see how far you've come. 



-I started this project in October 2018 in and worked on it until November 2019. 

- This is a Java program that would take the hot stock tickers that users were talking about on Reddit's infamous [WallStreetBets](https://www.reddit.com/r/wallstreetbets/) and research the stocks on Bing to determine whether to buy the stock or not. 

- The research performed on stocks was the change in price for the day.

- After making the decision, the bot would navigate to Investopedia's stock simulator and make a purchase so I could reflect on how the program was doing long term.

- If I could afford the stock (< 30% of my account balance), the program would purchase the stock on RobinHood with real money.

- After each 15 minutes, the program would check Robinhood to see if any shares reached the selling point (+/- 5.0%) and if so, sell. Then, the program would run another Reddit scan to look for new stocks and repeat the process. 

### More info

- There is a blacklist for comment characters that are mistaked for stocks (verified_baddies.txt).

- After each run, there was a list output of invalid stocks. If there wasn't a prompt on bing with the stock ticker info, the characters got added to the list.

- The locators are strictly XPath.


### Conclusion

- There are a lot of beginner mistakes in the code (class names, variable names, poor annotations, not leveraging oop principles, repetitive code), but for me, the best way to learn is to come accross the mistakes by myself. 

- This built the foundation for my web scraping skills and I feel like it was the best thing I did as a beginner programmer. This led me to look into Python and appreciate the libraries that are available (requests, bs4, scrapy, json, etc.). 

- Every couple of weeks, one of the websites (Reddit, Robinhood, Investopedia) would change their xpaths around and most my time was spent updating the locators in the program.

- Robinhood uses React.js and that was extremely frustrating to work with when dealing with locators.

- I was oblivous to the fact that websites have a robots.txt and you can be banned for spamming with Selenium requests.

- I was also oblivious to the fact that Reddit has an API.

- It's embarrassing that I used Selenium for web scraping but you gotta start somewhere.

- I'm not going to accept pull requests because I want this to stay untouched so I can remember where I started.
