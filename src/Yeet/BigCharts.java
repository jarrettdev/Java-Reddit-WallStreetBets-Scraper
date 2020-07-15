package Yeet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BigCharts {
    Document bc;
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
        if (hotStock.startsWith("$")) {
            BigCharts.hotStock = hotStock.substring(1, hotStock.length());
        } else {
            BigCharts.hotStock = hotStock;
        }
    }

    public static int getStockQuantity() {
        return stockQuantity;
    }

    public static void setStockQuantity(int stockQuantity) {
        BigCharts.stockQuantity = stockQuantity;
    }

    public static double getStockPrice() {
        return stockPrice;
    }

    public static void setStockPrice(double stockPrice) {
        BigCharts.stockPrice = stockPrice;
    }

    public static double getBuyingPower() {
        return buyingPower;
    }

    public static void setBuyingPower(double buyingPower) {
        BigCharts.buyingPower = buyingPower;
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

    String searchBarXPath = "//*[@id=\"qchartform\"]/input[1]";
    String priceXPath = "//*[@id=\"quote\"]/tbody/tr[3]/td[1]/div";
    String headingXPath = "//*[@id=\"homepage\"]/div[1]";
    String bigChartsUrl = "bigcharts.marketwatch.com";
    String url = "http://bigcharts.marketwatch.com/quickchart/quickchart.asp?";
    String fixedUrl;

    void login() {

        try {
            options = new ChromeOptions();
            options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
            options.addArguments("profile-directory=Profile 1");
            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            chromeDriver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(chromeDriver, 10);
            chromeDriver.get("bigcharts.marketwatch.com/");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(headingXPath)));


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
        fixedUrl = url + "symb=" + getHotStock() + "&insttype=&freq=1&show=&time=8";
        System.out.println(fixedUrl);
        bc = (Jsoup.connect(fixedUrl).get());
        Elements ding = bc.select("#quote > tbody > tr:nth-child(3) > td.last > div");
        if (!ding.text().equals("")) {
            System.out.println("not null hoe");
            System.out.println(ding.text());
            String stockValueString = ding.text();
            String newNew = stockValueString.replaceAll(",", "");
            System.out.println(getHotStock() + " : $" + newNew);
            try {
                setStockPrice(Double.parseDouble(newNew));
            } catch (NumberFormatException e) {
                System.out.println("dun do that bru :( ");
            }
            System.out.println("Fixed price : $" + getStockPrice());
            if (getStockPrice() <= getBpCalc()) {
                System.out.println("BUY " + getHotStock() + "!!!!!");
                stocksToBuy.add(getHotStock());
            } else {
                System.out.println("not gonna buy " + getHotStock() + " b/c " + getStockPrice() + " is more than " + getBpCalc());
            }


        } else {
            System.out.println("nully boi");
            invalidStocks.add(getHotStock());
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
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            robinhoodDriver = new ChromeDriver(options);
            robinhoodDriver.get(url);
            Thread.sleep(2000);

        } catch (Exception e) {

        }
    }

    void makeRobinhoodPurchase(String stockToBuy, int quan) {
        WebDriverWait wait = new WebDriverWait(robinhoodDriver, 10);
        if (getBuyingPower() > 40) {
            try {
                String url = ("https://robinhood.com/stocks/");
                String fixedUrl = url + stockToBuy;
                //options.setHeadless(true);
                robinhoodDriver.get(fixedUrl);
                if (!robinhoodDriver.getPageSource().contains("Such 404")) {

                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("5Y")));
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
                        WebElement quantBox = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[1]/div[1]/label/div[2]/input"));
                        quantBox.sendKeys(Integer.toString(quan));
                        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[2]/div/div[2]/button")));
                        WebElement reviewOrderButton = robinhoodDriver.findElement(By.xpath("//*[contains(text(), 'Review Order')]"));
                        reviewOrderButton.click();
                        Thread.sleep(2000);
                        WebElement submitPurchase = robinhoodDriver.findElement(By.xpath("//*[contains(text(), 'Submit Buy')]"));
                        System.out.println(stockToBuy + " BOUGHT");

                    } else {
                        System.out.println("sorry, there was an error loading " + stockToBuy + "'s returns");
                    }
                } else {
                    invalidStocks.add(stockToBuy);
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
