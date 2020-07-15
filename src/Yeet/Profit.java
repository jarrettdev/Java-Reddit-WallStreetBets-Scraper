package Yeet;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import javax.swing.text.Document;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Profit extends Thread {

    protected static boolean profitRunning = false;

    @Override
    public void run(){
        profitRunning = true;
        loadRobinhood();
    }

    WebDriver chromeDriver, robinhoodDriver;
    ChromeOptions options;
    private static int stockQuantity;
    private static double stockPrice;
    private static double buyingPower;
    private static String hotStock;
    List<String> ownedShares = new ArrayList<>();
    List<String> soldStocks = new ArrayList<>();



    public static int getStockQuantity() {
        return stockQuantity;
    }

    public static void setStockQuantity(int stockQuantity) {
        Profit.stockQuantity = stockQuantity;
    }

    public static double getStockPrice() {
        return stockPrice;
    }

    public static void setStockPrice(double stockPrice) {
        Profit.stockPrice = stockPrice;
    }

    public static double getBuyingPower() {
        return buyingPower;
    }

    public static void setBuyingPower(double buyingPower) {
        Profit.buyingPower = buyingPower;
    }

    private double bpCalc = 0;

    public void setBpCalc(double buyingPowerCalculation) {
        this.bpCalc = buyingPowerCalculation;
    }

    public double getBpCalc() {
        return bpCalc;
    }




    void loadRobinhood() { //checks profits on each stock and sells if you have a net loss or gain of > 20 percent
        try {
            String totalReturnPercentXPath = "//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[2]/div/div[1]/table/tbody/tr[3]/td[3]/span/span[2]";
            options = new ChromeOptions();
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
            options.addArguments("profile-directory=Profile 1");
            options.addArguments("--disable-notifications");
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            chromeDriver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(chromeDriver, 10);
            chromeDriver.get("https://robinhood.com/");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Account")));
            WebElement accountTab = chromeDriver.findElement(By.partialLinkText("Account"));
            accountTab.click();
            WebElement BP = chromeDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/header/div/div[2]/div/div[2]/div[2]/div/header/div/div[2]/div/div[1]"));
            setBuyingPower(Double.parseDouble(BP.getText().substring(1, BP.getText().length())));
            System.out.println("Buying power : $" + getBuyingPower());
            setBpCalc(getBuyingPower() * .25);
            System.out.println("RECOMMENDED MAX PRICE PER STOCK : " + getBpCalc());
            accountTab.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/div/div[2]/div/div/div[1]/div/div")));
            List<WebElement> stockList = chromeDriver.findElements(By.partialLinkText(" Share"));
            for (WebElement stock : stockList) {
                ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", stock); //scroll the stock into view
                System.out.println(stock.getAttribute("href"));
                ownedShares.add(stock.getAttribute("href"));


            }
            System.out.println(ownedShares);
            for (String link : ownedShares) {
                chromeDriver.get(link);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(totalReturnPercentXPath)));
                WebElement rtrn = chromeDriver.findElement(By.xpath(totalReturnPercentXPath)); //total return percent
                ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", rtrn); //scroll the percent into view
                //System.out.println(rtrn.getText());
                String firstForm = rtrn.getText();
                String midForm = firstForm.substring(1, firstForm.indexOf(')') - 1);
                //System.out.println(midForm);
                double finalForm = Double.parseDouble(midForm);
                System.out.println(finalForm);
                if (finalForm != 0.0 && finalForm < 90000 && finalForm > -90000) { //safety first
                    if (finalForm <= -20.0 || finalForm >= 15.0) {
                        System.out.println(finalForm + " is outside the threshold");
                        System.out.println("going to sell " + link.substring(link.indexOf("s/") + 2));
                        sellStock(robinhoodDriver, link);
                    } else {
                        System.out.println("check complete for " + link.substring(link.indexOf("s/") + 2));
                        checkBP(chromeDriver);
                    }

                }else{
                    System.out.println("there was a problem with the return percentages (loadRobinhood)");
                }
            }
            chromeDriver.close();
            chromeDriver.quit();
            chromeDriver = new ChromeDriver(options);
            chromeDriver.get("https://robinhood.com/");
            chromeDriver.close();
            chromeDriver.quit();
            profitRunning = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sellStock(WebDriver c, String stock) {
        try{
        String returnPercentXPath = "//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[1]/header/div[1]/span[1]/span/span[2]";
        String quantBoxXPath = "//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[1]/div[1]/label/div[2]/input";
        String reviewOrderButtonXPath = "//*[contains(text(), 'Review Order')]";
        String submitOrderButtonXPath = "//*[contains(text(), 'Submit Buy')]";
        String sellButtonXPath = "//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/header/div/div[1]/div/div[2]";
        WebDriverWait wait = new WebDriverWait(c, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sellButtonXPath)));
        WebElement sellButton = c.findElement(By.xpath(sellButtonXPath));
        sellButton.click();
        WebElement quantBox = chromeDriver.findElement(By.xpath(quantBoxXPath));
        quantBox.sendKeys(Integer.toString(1));
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[2]/div/div[2]/button")));
        WebElement reviewOrderButton = chromeDriver.findElement(By.xpath(reviewOrderButtonXPath));
        reviewOrderButton.click();
        Thread.sleep(2000);
        WebElement submitPurchase = chromeDriver.findElement(By.xpath(submitOrderButtonXPath));
        WebElement submitPurchase = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[3]/div/div[2]/button[1]/span"));
        submitPurchase.click();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void checkBP(WebDriver c){
        JavascriptExecutor js = (JavascriptExecutor) c;
        WebDriverWait wait = new WebDriverWait(c, 10);
        js.executeScript("window.scrollBy(0,-100000)");
        WebElement accountTab = chromeDriver.findElement(By.partialLinkText("Account"));
        accountTab.click();
        WebElement BP = chromeDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/header/div/div[2]/div/div[2]/div[2]/div/header/div/div[2]/div/div[1]"));
        setBuyingPower(Double.parseDouble(BP.getText().substring(1, BP.getText().length())));
        System.out.println("Buying power : $" + getBuyingPower());
        setBpCalc(getBuyingPower() * .25);
        System.out.println("RECOMMENDED MAX PRICE PER STOCK : " + getBpCalc());
        accountTab.click();

    }

}
