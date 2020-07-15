package Yeet;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Driver;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Bing {

    WebDriver chromeDriver, robinhoodDriver;
    ChromeOptions options;
    List<String> purchasedStocks = new ArrayList<>();
    ArrayList<String> invalidStocks = new ArrayList<>();
    List<String> badBoyStocks = new ArrayList<>();
    List<String> stocksToBuy = new ArrayList<>();
    boolean isPresent;
    String begBingURL = "https://www.bing.com/search?q=";
    String midBingURL = "aapl%20STOCK";
    String endBingURL = "&qs=n&form=QBRE&sp=-1&pq=brk.%20stock&sc=8-10&sk=&cvid=7A2B0F54A28342FB982D06CAD4B73540";
    String finalBingURL;

    public static String getHotStock() {
        return hotStock;
    }

    public static void setHotStock(String hotStock) {
        if (hotStock.startsWith("$")) {
            Bing.hotStock = hotStock.substring(1);
        } else {
            Bing.hotStock = hotStock;
        }
    }

    public static int getStockQuantity() {
        return stockQuantity;
    }

    public static void setStockQuantity(int stockQuantity) {
        Bing.stockQuantity = stockQuantity;
    }

    public static double getStockPrice() {
        return stockPrice;
    }

    public static void setStockPrice(double stockPrice) {
        Bing.stockPrice = stockPrice;
    }

    public static double getBuyingPower() {
        return buyingPower;
    }

    public static void setBuyingPower(double buyingPower) {
        Bing.buyingPower = buyingPower;
    }

    private double bpCalc = 0;

    public void setBpCalc(double buyingPowerCalculation) {
        this.bpCalc = buyingPowerCalculation;
    }

    public double getBpCalc() {
        return bpCalc;
    }

    private static String hotStock;
    private static int stockQuantity;
    private static double stockPrice;
    private static double buyingPower;
    double fixedStockPrice;
    double dollarValue;


    String bingSearchBarXPath = "//*[@id=\"sb_form_q\"]";
    String stockPriceValueXPath = "//*[@id=\"Finance_Quote\"]/div";
    String bingResultsXPath = "//*[@id=\"b_header\"]/nav/ul/li[6]/a";

    void login() {

        try {
            options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
            options.addArguments("--disable-notifications");
            options.addArguments("profile-directory=Profile 1");
            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            chromeDriver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(chromeDriver, 10);
            chromeDriver.get("https://www.bing.com/");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bingSearchBarXPath)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void rhProcess() {
        Collections.shuffle(stocksToBuy);
        List<String> fixedStocksToBuy = stocksToBuy.stream().distinct().collect(Collectors.toList());
        System.out.println(fixedStocksToBuy);
        for (String stock : fixedStocksToBuy) {
            makeRobinhoodPurchase(stock, 1);
        }

    }

    void checkIndices() {
        chromeDriver.get("https://www.bing.com/search?q=dow");
        WebElement trendDirection = chromeDriver.findElement(By.xpath("//*[@id=\"financeAnswer\"]/div[2]/div/span[2]/span/span"));
        if (trendDirection.getAttribute("aria-label").equals("Price decrease")) {
            System.out.println("the market going down");
        }else{
            System.out.println("the market is going up");
        }
    }

    void findStock() throws Exception {
        String midBingURL = "aapl%20STOCK";
        finalBingURL = (begBingURL + getHotStock() + "%20Stock" + endBingURL);
        chromeDriver.get(finalBingURL);
        WebDriverWait wait = new WebDriverWait(chromeDriver, 4);
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bingSearchBarXPath)));
//        WebElement searchBar = chromeDriver.findElement(By.xpath(bingSearchBarXPath));
//        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].value='" + getHotStock() + " Stock" + "'", searchBar);
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"sa_ul\"]")));
//        searchBar.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bingResultsXPath)));

        if (!chromeDriver.getPageSource().contains("Data from Refinitiv") && !chromeDriver.getPageSource().contains("fin_mc") && (!chromeDriver.getPageSource().contains("WORLD MARKET") && !chromeDriver.getPageSource().contains("MY WATCHLIST"))) {
            isPresent = true;
        } else {
            isPresent = false;
        }

        if (isPresent) {
            System.out.println(getHotStock() + " does not exist!");
            invalidStocks.add(getHotStock());
            chromeDriver.get("https://www.bing.com");
        } else {
            chromeDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(stockPriceValueXPath)));
            WebElement stockPriceValue = chromeDriver.findElement(By.xpath(stockPriceValueXPath));
            String stockValueString = stockPriceValue.getText();
            String newNew = stockValueString.replaceAll(",", "");
            System.out.println(getHotStock() + " : $" + newNew);
            setStockPrice(Double.parseDouble(newNew));
            System.out.println("Fixed price : $" + getStockPrice());
            if (getStockPrice() <= getBpCalc()) {
                System.out.println("BUY " + getHotStock() + "!!!!!");
                stocksToBuy.add(getHotStock());
                chromeDriver.get("https://www.bing.com");
            } else {
                System.out.println("not gonna buy " + getHotStock() + " b/c " + getStockPrice() + " is more than " + getBpCalc());
                chromeDriver.get("https://www.bing.com");
            }

        }
        badBoyStocks = invalidStocks.stream().distinct().collect(Collectors.toList());


    }

    void loadRobinhood() {
        try {
            options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
            options.addArguments("profile-directory=Profile 1");
            options.addArguments("--disable-notifications");
            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            chromeDriver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(chromeDriver, 10);
            chromeDriver.get("https://robinhood.com/");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Account")));
            WebElement accountTab = chromeDriver.findElement(By.partialLinkText("Account"));
            accountTab.click();
            //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/header/div/div[2]/div/div[2]/div[2]/div/header/div/div[2]/div/div[1]/h3")));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/header/div/div[2]/div/div[2]/div[2]/div/header/div/div[2]/div/div[1]/h3")));
            WebElement BP = chromeDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/header/div/div[2]/div/div[2]/div[2]/div/header/div/div[2]/div/div[1]/h3"));
            setBuyingPower(Double.parseDouble(BP.getText().substring(1, BP.getText().length())));
            System.out.println("Buying power : $" + getBuyingPower());
            setBpCalc(getBuyingPower() * .25);
            accountTab.click();
            chromeDriver.close();
            chromeDriver.quit();
            System.out.println("RECOMMENDED MAX PRICE PER STOCK : " + getBpCalc());
            login();


        } catch (Exception e) {
        }
    }

    void loginToRobinhood() {
        try {
            String url = ("https://robinhood.com/");
            options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
            options.addArguments("profile-directory=Profile 1");
            options.addArguments("--disable-notifications");
            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            robinhoodDriver = new ChromeDriver(options);
            robinhoodDriver.get(url);
            Thread.sleep(2000);

        } catch (Exception e) {

        }
    }

    void tester() {
        loadRobinhood();
        loginToRobinhood();
        makeRobinhoodPurchase("jcp", 1);
    }

    void makeRobinhoodPurchase(String stockToBuy, int quan) {
        double currentMarketPrice = 0;
        String returnPercentXPath = "//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[1]/header/div[1]/span[1]/span/span[2]";
        String quantBoxXPath = "//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[1]/div[1]/label/div[2]/input";
        String reviewOrderButtonXPath = "//*[contains(text(), 'Review Order')]";
        String submitOrderButtonXPath = "//*[contains(text(), 'Submit Buy')]";
        String marketPriceXPath = "//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[1]/div[2]/span";
        WebDriverWait wait = new WebDriverWait(robinhoodDriver, 10);
        checkBP(robinhoodDriver);
        if (getBuyingPower() > 40) {
            try {
                String url = ("https://robinhood.com/stocks/");
                String fixedUrl = url + stockToBuy;
                //options.setHeadless(true);
                robinhoodDriver.get(fixedUrl);
                if (!robinhoodDriver.getPageSource().contains("Such 404")) {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("5Y")));
                    WebElement marketPrice = robinhoodDriver.findElement(By.xpath(marketPriceXPath));
                    currentMarketPrice = (Double.parseDouble(marketPrice.getText().substring(1, marketPrice.getText().indexOf(".") + 2)));
                    if (currentMarketPrice < getBpCalc()) {
////        WebElement searchBar = robinhoodDriver.findElement(By.xpath("//*[@id=\"react-select-search--value\"]/div[2]"));
////        searchBar.click();
////        Thread.sleep(400);
////        ((JavascriptExecutor) robinhoodDriver).executeScript("arguments[0].value='" + stockToBuy + "'", searchBar);
////        searchBar.sendKeys(stockToBuy);
////        searchBar.sendKeys(Keys.ENTER);
////        robinhoodDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                        WebElement fiveYearView = robinhoodDriver.findElement(By.partialLinkText("5Y"));
                        fiveYearView.click();
                        Thread.sleep(1500);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(returnPercentXPath)));
                        WebElement fiveYearReturnPercent = robinhoodDriver.findElement(By.xpath(returnPercentXPath));
                        System.out.println(fiveYearReturnPercent.getText());
                        double fiveYearReturns = (Double.parseDouble(fiveYearReturnPercent.getText().substring(1, fiveYearReturnPercent.getText().indexOf(".") + 2)));
                        System.out.println("FIVE YEAR RETURNS: " + fiveYearReturns);
                        WebElement oneYearView = robinhoodDriver.findElement(By.partialLinkText("1Y"));
                        oneYearView.click();
                        Thread.sleep(1500);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(returnPercentXPath)));
                        WebElement oneYearReturnPercent = robinhoodDriver.findElement(By.xpath(returnPercentXPath));
                        System.out.println(oneYearReturnPercent.getText());
                        double oneYearReturns = (Double.parseDouble(oneYearReturnPercent.getText().substring(1, oneYearReturnPercent.getText().indexOf(".") + 2)));
                        System.out.println("ONE YEAR RETURNS: " + oneYearReturns);
                        WebElement threeMonthView = robinhoodDriver.findElement(By.partialLinkText("3M"));
                        threeMonthView.click();
                        Thread.sleep(1500);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(returnPercentXPath)));
                        WebElement threeMonthReturnPercent = robinhoodDriver.findElement(By.xpath(returnPercentXPath));
                        System.out.println(threeMonthReturnPercent.getText());
                        double threeMonthReturns = (Double.parseDouble(threeMonthReturnPercent.getText().substring(1, threeMonthReturnPercent.getText().indexOf(".") + 2)));
                        System.out.println("THREE MONTH RETURNS: " + threeMonthReturns);
                        if ((fiveYearReturns <= -50.0) || (oneYearReturns <= -5.0 || threeMonthReturns <= -1.0)) { // || threeMonthReturns <= 0
                            System.out.println("RETURNS FOR " + stockToBuy + " ARE TOO LOW AT 5Y : " + fiveYearReturns + "%\n1Y : " + oneYearReturns + "%" + "%\n3m : " + threeMonthReturns + "%");
                        } else if (oneYearReturns != 0.0) { //else if it passes the above test AND the data loaded correctly (0.0 is not possible)
                            purchasedStocks.add(stockToBuy);
                            System.out.println("BUYING STOCK");
                            //WebElement buyingPowerTab = robinhoodDriver.findElement(By.partialLinkText("Buying Power"));
                            //System.out.println("BUYING POWER FOUND FOR " + stockToBuy);
                            //setBuyingPower(Double.parseDouble(buyingPowerTab.getText().substring(buyingPowerTab.getText().indexOf("$"), buyingPowerTab.getText().indexOf(" "))));
                            WebElement quantBox = robinhoodDriver.findElement(By.xpath(quantBoxXPath));
                            quantBox.sendKeys(Integer.toString(quan));
                            //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[2]/div/div[2]/button")));
                            WebElement reviewOrderButton = robinhoodDriver.findElement(By.xpath(reviewOrderButtonXPath));
                            reviewOrderButton.click();
                            //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[3]/div/div[2]/button[1]/span")));
                            //System.out.println((buyingPowerTab.getText().substring(buyingPowerTab.getText().indexOf("$"), buyingPowerTab.getText().indexOf(" "))));
                            Thread.sleep(2000);
                            WebElement submitPurchase = robinhoodDriver.findElement(By.xpath(submitOrderButtonXPath));
                            //WebElement submitPurchase = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[3]/div/div[2]/button[1]/span"));
                            submitPurchase.click();
                            Thread.sleep(3500);
                            System.out.println(stockToBuy + " BOUGHT");
                            checkBP(robinhoodDriver);

                        } else {
                            System.out.println("sorry, there was an error loading " + stockToBuy + "'s returns");
                        }

                    } else {
                        System.out.println(stockToBuy + " is not a safe buy because " + currentMarketPrice + " is more than " + getBpCalc());
                    }

                } else {
                    invalidStocks.add(stockToBuy);
                    System.out.println(stockToBuy + " is invalid :(");
                }

            } catch (Exception e) {
            }

        } else {
            System.out.println("buying power too low");
        }
        badBoyStocks = invalidStocks.stream().distinct().collect(Collectors.toList());
    }

    void writeBadStocksToFile(String fileName) throws Exception {

        PrintWriter pw = new PrintWriter(new FileWriter(fileName), true);
        for (String badStock : badBoyStocks) {
            pw.println(badStock);
            //pw.println("********************");
        }
        pw.close();
    }

    void checkBP(WebDriver c) {
        JavascriptExecutor js = (JavascriptExecutor) c;
        WebDriverWait wait = new WebDriverWait(c, 10);
        js.executeScript("window.scrollBy(0,-100000)");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Account")));
        WebElement accountTab = c.findElement(By.partialLinkText("Account"));
        accountTab.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/header/div/div[2]/div/div[2]/div[2]/div/header/div/div[2]/div/div[1]/h3")));
        WebElement BP = c.findElement(By.xpath("//5/div/main/div[2]/div/div/header/div/div[2]/div/div[2]/div[2]/div/header/div/div[2]/div/div[1]/h3"));
        setBuyingPower(Double.parseDouble(BP.getText().substring(1, BP.getText().length())));
        System.out.println("Buying power : $" + getBuyingPower());
        setBpCalc(getBuyingPower() * .25);
        System.out.println("RECOMMENDED MAX PRICE PER STOCK : " + getBpCalc());
        accountTab.click();


    }

    /*void checkXPathText(String regex, String strToCheck){ // implement a method to use regex on the dollar amount in the buying power tab of robinhood. Make it so it takes in account COMMAS. ex: (100) -> (1,000,000)
        Pattern checkRegex = Pattern.compile(regex);      // make it so you can use it on tricky XPaths in the future**
        Matcher regexMatcher = checkRegex.matcher(strToCheck);
        lineStorage.add(strToCheck); // fix to problem below
        while (regexMatcher.find()) {
            if (regexMatcher.group().trim().endsWith(",")) {
                stockStorage.add(regexMatcher.group().trim().substring(0, regexMatcher.group().trim().length() - 1));
            } else if (regexMatcher.group().trim().startsWith("$")) {
                stockStorage.add(regexMatcher.group().trim().substring(1, regexMatcher.group().trim().length()));
            } else if (regexMatcher.group().trim().endsWith(".")) {
                stockStorage.add(regexMatcher.group().trim().substring(0, regexMatcher.group().trim().length() - 1));
            } else {
                stockStorage.add(regexMatcher.group().trim());
            }
            //lineStorage.add(strToCheck); // MAJOR PROBLEM : ADDS COMMENT n NUMBER OF TIMES, WHERE n IS THE NUMBER OF STOCKS IN THE COMMENT
            if (regexMatcher.group().length() != 0) {
                //System.out.println(regexMatcher.group().trim());
            }
        }
    }*/

    void completeConfirmationScreen(WebDriver c) {
        JavascriptExecutor js = (JavascriptExecutor) c;
        WebDriverWait wait = new WebDriverWait(c, 10);
    }

}
