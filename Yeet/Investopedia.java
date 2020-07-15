package Yeet;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import Yeet.Webby;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.Driver;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Investopedia extends Thread {
    WebDriver chromeDriver;
    WebDriver robinhoodDriver;
    ChromeOptions options;
    int quantity;
    ArrayList<String> invalidStocks = new ArrayList<>();
    private double bpCalc = 0;
    private static boolean holup = false;
    private static String hotStock;
    private static int stockQuantity;
    private static double stockPrice;
    private static double buyingPower;
    boolean captchaComplete;
    private boolean purchaseComplete;
    boolean isPresent;
    List<String> badBoyStocks = new ArrayList<>();
    List<String> stocksToBuy = new ArrayList<>();
    List<String> purchasedStocks = new ArrayList<>();

    public boolean isPurchaseComplete() {
        return purchaseComplete;
    }

    public void setBpCalc(double buyingPowerCalculation) {
        this.bpCalc = buyingPowerCalculation;
    }

    public double getBpCalc() {
        return bpCalc;
    }

    public void setHolup(boolean holup) {
        this.holup = holup;
    }

    public boolean getHolup() {
        return holup;
    }

    public void setPurchaseComplete(boolean purchaseComplete) {
        this.purchaseComplete = purchaseComplete;
    }

    public void setBuyingPower(double buyingPower) {
        this.buyingPower = buyingPower;
    }

    public static double getBuyingPower() {
        return buyingPower;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public static double getStockPrice() {
        return stockPrice;
    }

    public static String getHotStock() {
        return hotStock;
    }

    public void setHotStock(String hotStock) {
        if (hotStock.startsWith("$")) {
            this.hotStock = hotStock.substring(1, hotStock.length());
        } else {
            this.hotStock = hotStock;
        }
    }

    public static int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public void run() {
        purchaseStock();
    }

    void login() {
        try {
            captchaComplete = false;
            options = new ChromeOptions();
            options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
            options.addArguments("profile-directory=Profile 1");
            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            chromeDriver = new ChromeDriver(options);
            chromeDriver.get("https://www.investopedia.com/simulator/trade/tradestock.aspx");
//            Thread.sleep(6000);
//            WebElement login1 = chromeDriver.findElement(By.xpath("/html/body/section[1]/div[1]/header/div/div/a[2]/span"));
//            login1.click();
//            Thread.sleep(1000);
//            WebElement emailBox1 = chromeDriver.findElement(By.xpath("//*[@id=\"username\"]"));
//            WebElement passwordBox1 = chromeDriver.findElement(By.xpath("//*[@id=\"password\"]"));
//            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].value='camlataspi@desoz.com'", emailBox1);
//            //Thread.sleep(1000);
//            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].value='Yoot123$'", passwordBox1);
//            WebElement loginButton1 = chromeDriver.findElement(By.xpath("//*[@id=\"basic-login\"]/div[1]/form/div/div[7]/div[1]/button"));
//            loginButton1.click();
//            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void purchaseStock() {
        quantity = getStockQuantity();

        ArrayList badStocks = new ArrayList<>();
        try {
            String investopediaSearchBarXPath, investopediaStockQuanFieldXPath, investopediaPreviewOrderButtonXPath,
                    investopediaSubmitOrderButtonXPath, investopediaStockPreviewPrice;

            investopediaSearchBarXPath = "//*[@id=\"symbolTextbox\"]";
            investopediaStockQuanFieldXPath = "//*[@id=\"quantityTextbox\"]";
            investopediaPreviewOrderButtonXPath = "//*[@id=\"previewButton\"]";
            investopediaSubmitOrderButtonXPath = "//*[@id=\"submitOrder\"]";
            investopediaStockPreviewPrice = "//*[@id=\"submitOrder\"]";
            Robinhood r1 = new Robinhood();
            setPurchaseComplete(false);
            //ArrayList<String> tabs2 = new ArrayList<String>(chromeDriver.getWindowHandles());
            chromeDriver.get("https://www.investopedia.com/simulator/trade/tradestock.aspx");
            //String originalHandle = chromeDriver.getWindowHandle();

            //chromeDriver.switchTo().window(originalHandle); // :)
            WebElement stockSearchBar = chromeDriver.findElement(By.xpath("//*[@id=\"symbolTextbox\"]"));
            WebElement stockQuantityField = chromeDriver.findElement(By.xpath("//*[@id=\"quantityTextbox\"]"));
            WebElement previewOrderButton = chromeDriver.findElement(By.xpath("//*[@id=\"previewButton\"]"));
            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].value='" + getHotStock() + "'", stockSearchBar);
            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].value='" + getStockQuantity() + "'", stockQuantityField);
            previewOrderButton.click();

            if (chromeDriver.getPageSource().contains("is not a valid symbol")) {
                isPresent = true;
            } else {
                isPresent = false;
            }
            if (isPresent) {
                System.out.println(getHotStock() + " does not exist!");
                invalidStocks.add(getHotStock());
                setPurchaseComplete(true);
                chromeDriver.get("https://www.investopedia.com/simulator/trade/tradestock.aspx");
            } else {
                chromeDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
                WebElement stockPreviewPrice = chromeDriver.findElement(By.xpath("//*[@id=\"Table2\"]/tbody/tr[1]/td"));
                System.out.println(getHotStock() + ": " + stockPreviewPrice.getText());
                setStockPrice(Double.parseDouble(stockPreviewPrice.getText().substring(0, stockPreviewPrice.getText().length() - 2)));
                System.out.println("Fixed price : $" + getStockPrice());
                if (getStockPrice() <= getBpCalc()) {
                    System.out.println("BUY " + getHotStock() + "!!!!!");
                    stocksToBuy.add(getHotStock());
                    WebElement submitOrderButton = chromeDriver.findElement(By.xpath("//*[@id=\"submitOrder\"]"));
                    submitOrderButton.click();
                    Thread.sleep(500);
                    setPurchaseComplete(true);
                    chromeDriver.get("https://www.investopedia.com/simulator/trade/tradestock.aspx");
                } else {
                    WebElement submitOrderButton = chromeDriver.findElement(By.xpath("//*[@id=\"submitOrder\"]"));
                    submitOrderButton.click();
                    Thread.sleep(500);
                    setPurchaseComplete(true);
                    chromeDriver.get("https://www.investopedia.com/simulator/trade/tradestock.aspx");
                }

            }
            badBoyStocks = invalidStocks.stream().distinct().collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }

//            stockSearchBar.sendKeys(Keys.ENTER);
//            Thread.sleep(3000);
//            WebElement tradeButton = chromeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[1]/div[1]/div/div[2]/table/tbody/tr[1]/td[4]/button"));
//            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(true);", stockSearchBar);
//            Thread.sleep(1000);
//            tradeButton.click();
//            Thread.sleep(2500);
//            WebElement stockQuantityBox = chromeDriver.findElement(By.xpath("//*[@id=\"shares\"]"));
//            stockQuantityBox.sendKeys(Keys.BACK_SPACE);
//            stockQuantityBox.sendKeys("1");
//            Thread.sleep(4000);
//            WebElement submitOrderButton = chromeDriver.findElement(By.xpath("/html/body/div[5]/div/div[1]/form/div[3]/div[1]/button[2]"));
//            submitOrderButton.click();
//            Thread.sleep(5000);
//            WebElement confirmationEscapeButton = chromeDriver.findElement(By.xpath("/html/body/div[5]/button"));
//            confirmationEscapeButton.click();
        //chromeDriver.switchTo().window(tabs2.get(0));
//            for (String handle : chromeDriver.getWindowHandles()) { //thanks stackoverflow
//                if (!handle.equals(originalHandle)) {
//                    chromeDriver.switchTo().window(handle);
//                    chromeDriver.close();
//                }
//            }
//            chromeDriver.switchTo().window(originalHandle); // :)

    }

    void rhProcess() {
        Collections.shuffle(stocksToBuy);
        List<String> fixedStocksToBuy = stocksToBuy.stream().distinct().collect(Collectors.toList());
        System.out.println(fixedStocksToBuy);
        for (String stock : fixedStocksToBuy) {
            makeRobinhoodPurchase(stock, 1);
        }

    }

    void openSite() {

        try {
            captchaComplete = false;
            ChromeOptions options = new ChromeOptions();
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            chromeDriver = new ChromeDriver(options);
            chromeDriver.get("https://www.marketwatch.com");
            Thread.sleep(6000);
            WebElement login1 = chromeDriver.findElement(By.xpath("/html/body/section[1]/div[1]/header/div/div/a[2]/span"));
            login1.click();
            Thread.sleep(1000);
            WebElement emailBox1 = chromeDriver.findElement(By.xpath("//*[@id=\"username\"]"));
            WebElement passwordBox1 = chromeDriver.findElement(By.xpath("//*[@id=\"password\"]"));
            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].value='camlataspi@desoz.com'", emailBox1);
            //Thread.sleep(1000);
            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].value='Yoot123$'", passwordBox1);
            WebElement loginButton1 = chromeDriver.findElement(By.xpath("//*[@id=\"basic-login\"]/div[1]/form/div/div[7]/div[1]/button"));
            loginButton1.click();
            Thread.sleep(3000);
            chromeDriver.get("https://www.marketwatch.com/game/invest-until-you-die/portfolio");
            WebElement stockSearchBar = chromeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[1]/div[1]/div/div[1]/input"));
            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].value='" + getHotStock() + "'", stockSearchBar);
            stockSearchBar.sendKeys(Keys.ENTER);
            Thread.sleep(3000);
            WebElement tradeButton = chromeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[1]/div[1]/div/div[2]/table/tbody/tr[1]/td[4]/button"));
            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(true);", stockSearchBar);
            Thread.sleep(1000);
            tradeButton.click();
            Thread.sleep(2500);
            WebElement stockQuantityBox = chromeDriver.findElement(By.xpath("//*[@id=\"shares\"]"));
            stockQuantityBox.sendKeys(Keys.BACK_SPACE);
            stockQuantityBox.sendKeys("1");
            Thread.sleep(4000);
            WebElement submitOrderButton = chromeDriver.findElement(By.xpath("/html/body/div[5]/div/div[1]/form/div[3]/div[1]/button[2]"));
            submitOrderButton.click();
            Thread.sleep(5000);
            WebElement confirmationEscapeButton = chromeDriver.findElement(By.xpath("/html/body/div[5]/button"));
            confirmationEscapeButton.click();

        } catch (Exception e) {
            e.printStackTrace();
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

    void removeDuplicates() { //remove duplicates from badstocks.txt file

        try {

            // PrintWriter object for output.txt
            PrintWriter pw = new PrintWriter("Premium Baddies.txt");

            // BufferedReader object for input.txt
            BufferedReader br1 = new BufferedReader(new FileReader("Verified Baddies.txt"));

            String line1 = br1.readLine();

            // loop for each line of input.txt
            while (line1 != null) {
                boolean flag = false;

                // BufferedReader object for output.txt
                BufferedReader br2 = new BufferedReader(new FileReader("Premium Baddies.txt"));

                String line2 = br2.readLine();

                // loop for each line of output.txt
                while (line2 != null) {

                    if (line1.equals(line2)) {
                        flag = true;
                        break;
                    }

                    line2 = br2.readLine();

                }

                // if flag = false
                // write line of input.txt to output.txt
                if (!flag) {
                    pw.println(line1);

                    // flushing is important here
                    pw.flush();
                }

                line1 = br1.readLine();

            }

            // closing resources
            br1.close();
            pw.close();

            System.out.println("File operation performed successfully");
        } catch (Exception e) {

        }
    }

    void loadRobinhood() {
        try {
            captchaComplete = false;
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
}

class Robinhood extends Thread {
    WebDriver robinhoodDriver;
    ChromeOptions options;
    private int stockQuantity;
    private double stockPrice;
    private double buyingPower;
    private String hotStock;

    public String getHotStock() {
        return hotStock;
    }

    public void setHotStock(String hotStock) {
        this.hotStock = hotStock;
    }


    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }


    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }


    public double getBuyingPower() {
        return buyingPower;
    }

    public void setBuyingPower(double buyingPower) {
        this.buyingPower = buyingPower;
    }


    @Override
    public void run() {
        setHotStock(Investopedia.getHotStock());
        setStockQuantity(Investopedia.getStockQuantity());
        makeRobinhoodPurchase(getHotStock(), getStockQuantity());
    }

    void makeRobinhoodPurchase(String stockToBuy, int quan) {
        try {
            String url = ("https://robinhood.com/stocks/");
            String fixedUrl = url + stockToBuy;
            options = new ChromeOptions();
            options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
            options.addArguments("profile-directory=Profile 1");
            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            robinhoodDriver = new ChromeDriver(options);
            robinhoodDriver.get(fixedUrl);
            Thread.sleep(2000);
//        WebElement searchBar = robinhoodDriver.findElement(By.xpath("//*[@id=\"react-select-search--value\"]/div[2]"));
//        searchBar.click();
//        Thread.sleep(400);
//        ((JavascriptExecutor) robinhoodDriver).executeScript("arguments[0].value='" + stockToBuy + "'", searchBar);
//        searchBar.sendKeys(stockToBuy);
//        searchBar.sendKeys(Keys.ENTER);
//        robinhoodDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            WebElement fiveYearView = robinhoodDriver.findElement(By.partialLinkText("5Y"));
            fiveYearView.click();
            Thread.sleep(1000);
            WebElement returnPercent = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[1]/header/div[1]/span[1]/span/span[2]"));
            System.out.println(returnPercent.getText());
            double fiveYearReturns = (Double.parseDouble(returnPercent.getText().substring(1, returnPercent.getText().indexOf(".") - 1)));
            System.out.println("FIVE YEAR RETURNS: " + fiveYearReturns);
            WebElement quantBox = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[1]/div[1]/label/div[2]/input"));
            quantBox.sendKeys(Integer.toString(quan));
            Thread.sleep(500);
            WebElement reviewOrderButton = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[2]/div/div[2]/button"));
            reviewOrderButton.click();
            WebElement submitPurchase = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[2]/div/div[2]/button[1]/span"));
            //submitPurchase.click();

        } catch (Exception e) {
        }
    }

}
