package Yeet;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mock {





    void open() {


        try {
            //Create a map to store  preferences
            Map<String, Object> prefs = new HashMap<String, Object>();

            //add key and value to map as follow to switch off browser notification
            prefs.put("profile.default_content_setting_values.notifications", 2);

            ChromeOptions options = new ChromeOptions();

            // set ExperimentalOption - prefs
            options.setExperimentalOption("prefs", prefs);

            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            WebDriver chromeDriver = new ChromeDriver(options);
            WebElement titleLink;
            System.out.println("woah");
            chromeDriver.get("https://www.reddit.com/r/wallstreetbets/");
            Thread.sleep(10000);
            List<WebElement> elements = chromeDriver.findElements(By.cssSelector("div.rpBJOHq2PR60pnwJlUyP0.s34aip-0.dvQtYX > div"));
            for (WebElement element : elements) {
                WebElement element2 = element.findElement(By.partialLinkText("Comment"));
                //WebElement centerbar = element.findElement(By.xpath("//*[@id=\"t3_bf1wr6\"]/div[1]"));
                ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", element2);
                System.out.println(element2.getAttribute("href"));
                Thread.sleep(2050);
            }

            /*titleLink = chromeDriver.findElement(By.cssSelector("h2.s15fpu6f-0.jlakiP"));
            System.out.println(titleLink.getText());
            titleLink.click();
            System.out.println(chromeDriver.getCurrentUrl());
            Thread.sleep(2000);
            WebElement exitButton = chromeDriver.findElement(By.cssSelector("button.c_rRg_d32D6ZO5sV8DmMM.p23tea-8.eMGCCj"));
            exitButton.click();
            WebElement moreButton = chromeDriver.findElement(By.cssSelector("p.s1i8jpjg-1.cmQAcE"));*/


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("yo");
    }

    void robby(String stockToBuy) {
        ChromeDriver robinhoodDriver;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/");
        options.addArguments("profile-directory=Profile 1");
        //options.setHeadless(true);
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
        robinhoodDriver = new ChromeDriver(options);
        robinhoodDriver.get("https://robinhood.com/");
        try {
            String url = ("https://robinhood.com/stocks/");
            String fixedUrl = url + stockToBuy;
            //options.setHeadless(true);
            robinhoodDriver.get(fixedUrl);
            Thread.sleep(2000);
            WebDriverWait wait = new WebDriverWait(robinhoodDriver, 10);

////        WebElement searchBar = robinhoodDriver.findElement(By.xpath("//*[@id=\"react-select-search--value\"]/div[2]"));
////        searchBar.click();
////        Thread.sleep(400);
////        ((JavascriptExecutor) robinhoodDriver).executeScript("arguments[0].value='" + stockToBuy + "'", searchBar);
////        searchBar.sendKeys(stockToBuy);
////        searchBar.sendKeys(Keys.ENTER);
////        robinhoodDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("5Y")));
            Thread.sleep(500);
            WebElement fiveYearView = robinhoodDriver.findElement(By.partialLinkText("5Y"));
            WebElement buyingPowerTab = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[4]"));
            System.out.println(buyingPowerTab.getText());
            fiveYearView.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[1]/header/div[1]/span[1]/span/span[2]")));
            Thread.sleep(750);
            WebElement fiveYearReturnPercent = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[1]/header/div[1]/span[1]/span/span[2]"));
            System.out.println(fiveYearReturnPercent.getText());
            double fiveYearReturns = (Double.parseDouble(fiveYearReturnPercent.getText().substring(1, fiveYearReturnPercent.getText().indexOf(".") + 1)));
            System.out.println("FIVE YEAR RETURNS: " + fiveYearReturns);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("1Y")));
            WebElement oneYearView = robinhoodDriver.findElement(By.partialLinkText("1Y"));
            oneYearView.click();
            WebElement oneYearReturnPercent = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[1]/div/section[1]/header/div[1]/span[1]/span/span[2]"));
            System.out.println(oneYearReturnPercent.getText());
            double oneYearReturns = (Double.parseDouble(oneYearReturnPercent.getText().substring(1, oneYearReturnPercent.getText().indexOf(".") + 1)));
            System.out.println("ONE YEAR RETURNS: " + oneYearReturns);
            WebElement quantBox = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[1]/div[1]/label/div[2]/input"));
            quantBox.sendKeys(Integer.toString(1));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[3]/div/div[2]/button")));
            //WebElement buyingPowerTab = robinhoodDriver.findElement(By.xpath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[3]"));
            // System.out.println((buyingPowerTab.getText().substring(buyingPowerTab.getText().indexOf("$"), buyingPowerTab.getText().indexOf(" "))));
            WebElement reviewOrderButton = robinhoodDriver.findElement(By.xpath("//*[contains(text(), 'Review Order')]"));
            reviewOrderButton.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Submit Buy')]")));
            WebElement submitPurchase = robinhoodDriver.findElement(By.xpath("//*[contains(text(), 'Submit Buy')]"));
            submitPurchase.click();
            Thread.sleep(1000);
            System.out.println(robinhoodDriver.findElementByXPath("//*[@id=\"react_root\"]/div/main/div[2]/div/div/div/main/div[2]/div[2]/div/form/div[1]/div[3]/div/div[1]/div/div/div/div/p").getText());
            submitPurchase.click();
            Thread.sleep(3500);
            System.out.println(stockToBuy + "BOUGHT");


        } catch (Exception e) {
        }


    }

    void newReddit(String link) {

        try {
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);

            ChromeOptions options = new ChromeOptions();

            // set ExperimentalOption - prefs
            options.setExperimentalOption("prefs", prefs);

            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            WebDriver chromeDriver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(chromeDriver, 20);
            JavascriptExecutor js = (JavascriptExecutor) chromeDriver;
            WebElement titleLink;
            System.out.println("woah");
            chromeDriver.get(link);
            //wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Posted by")));
            Boolean isPresent = chromeDriver.findElements(By.xpath("//*[@id=\"SHORTCUT_FOCUSABLE_DIV\"]/div[2]/div/div/div/div[2]/div[3]/div[1]/div[1]/div[4]/div/button")).size() > 0;
            if (isPresent) {
                WebElement viewMoreButton = chromeDriver.findElement(By.xpath("//*[@id=\"SHORTCUT_FOCUSABLE_DIV\"]/div[2]/div/div/div/div[2]/div[3]/div[1]/div[1]/div[4]/div/button"));
                ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", viewMoreButton);
                viewMoreButton.click();
                Thread.sleep(2000);

            } else {
                System.out.println("o well");
            }
            js.executeScript("window.scrollBy(0,-100000)");
            Thread.sleep(2000);
            WebElement moreReplButton = chromeDriver.findElement(By.xpath("//*[contains(text(), 'more repl')]"));
            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", moreReplButton);
            moreReplButton.click();
            //List<WebElement> listOfExtendButtons = chromeDriver.findElements(By.xpath("//*[contains(text(), 'more repl')]"));
//        for(WebElement e : listOfExtendButtons){
//            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", e);
//            e.click();
//            System.out.println("executed");
//        }

            System.out.println("done");

            js.executeScript("window.scrollBy(0,-100000)");


            System.out.println("ayee");

            for (WebElement row : chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"))) { // **check this one and the one below it periodically, they change from time to time**
                System.out.println(row.getText());
                System.out.println();
            }


        } catch (Exception e) {

        }
    }
}
