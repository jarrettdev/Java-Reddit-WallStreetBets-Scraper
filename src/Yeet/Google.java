package Yeet;

// DO NOT USE THIS CLASS!!! google is very smart
// investopedia, bing, and bigcharts are safe

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Google {
    WebDriver chromeDriver, robinhoodDriver;
    ChromeOptions options;
    List<String> purchasedStocks = new ArrayList<>();
    ArrayList<String> invalidStocks = new ArrayList<>();
    List<String> badBoyStocks = new ArrayList<>();
    List<String> stocksToBuy = new ArrayList<>();
    boolean isPresent;

    public static String getHotStock() {
        return hotStock;
    }

    public static void setHotStock(String hotStock) {
        Google.hotStock = hotStock;
    }

    public static int getStockQuantity() {
        return stockQuantity;
    }

    public static void setStockQuantity(int stockQuantity) {
        Google.stockQuantity = stockQuantity;
    }

    public static double getStockPrice() {
        return stockPrice;
    }

    public static void setStockPrice(double stockPrice) {
        Google.stockPrice = stockPrice;
    }

    public static double getBuyingPower() {
        return buyingPower;
    }

    public static void setBuyingPower(double buyingPower) {
        Google.buyingPower = buyingPower;
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


    String googleSearchBarXPath = "//*[@id=\"tsf\"]/div[2]/div/div[1]/div/div[1]/input";
    String stockPriceValueXPath = "//*[@id=\"knowledge-finance-wholepage__entity-summary\"]/div/g-card-section[1]/div/g-card-section/div[2]/span[1]/span/span[1]";
    String googleResultsXPath = "//*[@id=\"resultStats\"]";

    void login() {

        try {
            options = new ChromeOptions();
            options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
            options.addArguments("profile-directory=Profile 1");
            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            chromeDriver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(chromeDriver, 10);
            chromeDriver.get("https://www.google.com/");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(googleSearchBarXPath)));

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

    void findStock() throws Exception {
        WebDriverWait wait = new WebDriverWait(chromeDriver, 4);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(googleSearchBarXPath)));
        WebElement searchBar = chromeDriver.findElement(By.xpath(googleSearchBarXPath));
        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].value='" + getHotStock() + " Stock" + "'", searchBar);
        searchBar.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(googleResultsXPath)));

        if (!chromeDriver.getPageSource().contains("Market Summary")) {
            isPresent = true;
        } else {
            isPresent = false;
        }

        if (isPresent) {
            System.out.println(getHotStock() + " does not exist!");
            invalidStocks.add(getHotStock());
            chromeDriver.get("https://www.google.com");
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
                chromeDriver.get("https://www.google.com");
            } else {
                System.out.println("not gonna buy " + getHotStock() + " b/c " + getStockPrice() + " is more than " + getBpCalc());
                chromeDriver.get("https://www.google.com");
            }

        }
        badBoyStocks = invalidStocks.stream().distinct().collect(Collectors.toList());


    }

    void loadRobinhood() {
        try {
            options = new ChromeOptions();
            options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
            options.addArguments("profile-directory=Profile 1");
            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            chromeDriver = new ChromeDriver(options);
            chromeDriver.get("https://robinhood.com/");
            Thread.sleep(2000);
            WebElement accountTab = chromeDriver.findElement(By.partialLinkText("Account"));
            accountTab.click();
            WebElement BP = chromeDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/header/div/div[2]/div/div[2]/div[2]/div/header/div/div[2]/div/div[1]"));
            setBuyingPower(Double.parseDouble(BP.getText().substring(1, BP.getText().length())));
            System.out.println("Buying power : $" + getBuyingPower());
            chromeDriver.close();
            chromeDriver.quit();
            setBpCalc(getBuyingPower() * .15);
            System.out.println("RECOMMENDED MAX PRICE PER STOCK : " + getBpCalc());

        } catch (Exception e) {
        }
    }

    void loginToRobinhood() {
        try {
            String url = ("https://robinhood.com/stocks/");
            options = new ChromeOptions();
            options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
            options.addArguments("profile-directory=Profile 1");
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
        WebDriverWait wait = new WebDriverWait(robinhoodDriver, 10);
        if (getBuyingPower() > 40) {
            try {
                String url = ("https://robinhood.com/stocks/");
                String fixedUrl = url + stockToBuy;
                //options.setHeadless(true);
                robinhoodDriver.get(fixedUrl);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("5Y")));
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
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[1]/header/div[1]/span[1]/span/span[2]")));
                WebElement fiveYearReturnPercent = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[1]/header/div[1]/span[1]/span/span[2]"));
                System.out.println(fiveYearReturnPercent.getText());
                double fiveYearReturns = (Double.parseDouble(fiveYearReturnPercent.getText().substring(1, fiveYearReturnPercent.getText().indexOf(".") + 2)));
                System.out.println("FIVE YEAR RETURNS: " + fiveYearReturns);
                WebElement oneYearView = robinhoodDriver.findElement(By.partialLinkText("1Y"));
                oneYearView.click();
                Thread.sleep(1500);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[1]/header/div[1]/span[1]/span/span[2]")));
                WebElement oneYearReturnPercent = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[1]/header/div[1]/span[1]/span/span[2]"));
                System.out.println(oneYearReturnPercent.getText());
                double oneYearReturns = (Double.parseDouble(oneYearReturnPercent.getText().substring(1, oneYearReturnPercent.getText().indexOf(".") + 2)));
                System.out.println("ONE YEAR RETURNS: " + oneYearReturns);

                if ((fiveYearReturns <= -20.0) || (oneYearReturns <= -5.0)) {
                    System.out.println("RETURNS FOR " + stockToBuy + " ARE TOO LOW AT 5Y : " + fiveYearReturns + "%\n1Y : " + oneYearReturns + "%");
                } else if (oneYearReturns != 0.0) {
                    purchasedStocks.add(stockToBuy);
                    System.out.println("BUYING STOCK");
                    //WebElement buyingPowerTab = robinhoodDriver.findElement(By.partialLinkText("Buying Power"));
                    //System.out.println("BUYING POWER FOUND FOR " + stockToBuy);
                    //setBuyingPower(Double.parseDouble(buyingPowerTab.getText().substring(buyingPowerTab.getText().indexOf("$"), buyingPowerTab.getText().indexOf(" "))));
                    WebElement quantBox = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[1]/div[1]/label/div[2]/input"));
                    quantBox.sendKeys(Integer.toString(quan));
                    //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[2]/div/div[2]/button")));
                    WebElement reviewOrderButton = robinhoodDriver.findElement(By.xpath("//*[contains(text(), 'Review Order')]"));
                    reviewOrderButton.click();
                    //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[3]/div/div[2]/button[1]/span")));
                    //System.out.println((buyingPowerTab.getText().substring(buyingPowerTab.getText().indexOf("$"), buyingPowerTab.getText().indexOf(" "))));
                    Thread.sleep(2000);
                    WebElement submitPurchase = robinhoodDriver.findElement(By.xpath("//*[contains(text(), 'Submit Buy')]"));
                    //WebElement submitPurchase = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[3]/div/div[2]/button[1]/span"));
                    submitPurchase.click();
                    Thread.sleep(3500);
                    System.out.println(stockToBuy + " BOUGHT");

                } else {
                    System.out.println("sorry, there was an error loading " + stockToBuy + "'s returns");
                }

            } catch (Exception e) {
            }

        } else {
            System.out.println("buying power too low");
        }
    }

    void writeBadStocksToFile(String fileName) throws Exception {

        PrintWriter pw = new PrintWriter(new FileWriter(fileName), true);
        for (String badStock : badBoyStocks) {
            pw.println(badStock);
            //pw.println("********************");
        }
        pw.close();
    }


}
