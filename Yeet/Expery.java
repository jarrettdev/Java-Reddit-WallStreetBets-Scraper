package Yeet;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import Yeet.Webby;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Expery extends Thread {
    protected static boolean mealRunning = false;
    static ArrayList<String> links; // TODO: use ensureCapacity() to efficiently adjust capacity
    Webby w1 = new Webby();
    int CBT = 0;
    WebDriver chromeDriver;

    @Override
    public void run() {
        mealRunning = true;
        open();
    }

    void open() {


        try {
            links = new ArrayList<>();
            links.ensureCapacity(200); //save memory (there will not be more than 200 links)

            //Create a map to store  preferences
            Map<String, Object> prefs = new HashMap<String, Object>();

            //add key and value to map as follow to switch off browser notification
            prefs.put("profile.default_content_setting_values.notifications", 2);

            ChromeOptions options = new ChromeOptions();
            // set ExperimentalOption - prefs
            options.setExperimentalOption("prefs", prefs);
            //options.setHeadless(true);
            System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
            chromeDriver = new ChromeDriver(options);
            JavascriptExecutor js = (JavascriptExecutor) chromeDriver;
            WebElement titleLink;
            System.out.println("woah");
            chromeDriver.get("https://www.reddit.com/r/wallstreetbets/");

            ///DynaLoad algorithm below

            for (int i = 0; i < 5; i++) {
                js.executeScript("window.scrollBy(0,50000)");
                Thread.sleep(1000);
                js.executeScript("window.scrollBy(0,50000)");
                Thread.sleep(1000);
                js.executeScript("window.scrollBy(0,-100000)");
                Thread.sleep(1000);
            }

            List<WebElement> elements2 = chromeDriver.findElements(By.partialLinkText(" Comments")); // making a list of elements
            //List<WebElement> listOfElements = chromeDriver.findElements(By.xpath("//*[contains(text(), 'more repl')]"));
            for (WebElement element : elements2) {
                //WebElement element2 = element.findElement(By.partialLinkText("Comment"));
                //WebElement centerbar = element.findElement(By.xpath("//*[@id=\"t3_bf1wr6\"]/div[1]"));
                ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", element);
                //element2.click();
                links.add((element.getAttribute("href")) + "?sort=confidence");
                String ha = element.getAttribute("href");
                System.out.println(ha);
                //Thread.sleep(1000);
                CBT++;
                //exitButton.click();


            }

            js.executeScript("window.scrollBy(0,1500)");
            Thread.sleep(400);

            List<WebElement> elements3 = chromeDriver.findElements(By.partialLinkText(" Comments"));
            elements3.removeAll(elements2);
            for (WebElement element : elements3) {
                //WebElement element2 = element.findElement(By.partialLinkText("Comment"));
                //WebElement centerbar = element.findElement(By.xpath("//*[@id=\"t3_bf1wr6\"]/div[1]"));
                ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", element);
                //element2.click();
                links.add((element.getAttribute("href")) + "?sort=confidence");
                String ha = element.getAttribute("href");
                System.out.println(ha);
                //Thread.sleep(1000);
                CBT++;
                //WebElement exitButton = chromeDriver.findElement(By.xpath("//*[@id=\"SHORTCUT_FOCUSABLE_DIV\"]/div[3]/div/div[1]/div/div[2]/button"));
                //exitButton.click();


            }

            js.executeScript("window.scrollBy(0,1500)");
            Thread.sleep(400);

            List<WebElement> elements4 = chromeDriver.findElements(By.partialLinkText(" Comments"));
            elements4.removeAll(elements2);
            elements4.removeAll(elements3);
            for (WebElement element : elements4) {
                //WebElement element2 = element.findElement(By.partialLinkText("Comment"));
                //WebElement centerbar = element.findElement(By.xpath("//*[@id=\"t3_bf1wr6\"]/div[1]"));
                ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", element);
                //element2.click();
                links.add((element.getAttribute("href")) + "?sort=confidence");
                String ha = element.getAttribute("href");
                System.out.println(ha);
                //Thread.sleep(1000);
                CBT++;
                //WebElement exitButton = chromeDriver.findElement(By.xpath("//*[@id=\"SHORTCUT_FOCUSABLE_DIV\"]/div[3]/div/div[1]/div/div[2]/button"));
                //exitButton.click();


            }

            js.executeScript("window.scrollBy(0,1500)");
            Thread.sleep(400);

            List<WebElement> elements5 = chromeDriver.findElements(By.partialLinkText(" Comments"));
            elements5.removeAll(elements2);
            elements5.removeAll(elements3);
            elements5.removeAll(elements4);
            for (WebElement element : elements5) {
                //WebElement element2 = element.findElement(By.partialLinkText("Comment"));
                //WebElement centerbar = element.findElement(By.xpath("//*[@id=\"t3_bf1wr6\"]/div[1]"));
                ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", element);
                //element2.click();
                links.add((element.getAttribute("href")) + "?sort=confidence");
                String ha = element.getAttribute("href");
                System.out.println(ha);
                //Thread.sleep(1000);
                CBT++;
                //WebElement exitButton = chromeDriver.findElement(By.xpath("//*[@id=\"SHORTCUT_FOCUSABLE_DIV\"]/div[3]/div/div[1]/div/div[2]/button"));
                //exitButton.click();

                //TODO: End the need for all these repeating loops. ^^
            }

            chromeDriver.close();
            chromeDriver.quit();

            /*titleLink = chromeDriver.findElement(By.cssSelector("h2.s15fpu6f-0.jlakiP"));
            System.out.println(titleLink.getText());
            titleLink.click();
            System.out.println(chromeDriver.getCurrentUrl());
            Thread.sleep(2000);
            WebElement exitButton = chromeDriver.findElement(By.cssSelector("button.c_rRg_d32D6ZO5sV8DmMM.p23tea-8.eMGCCj"));
            exitButton.click();
            WebElement moreButton = chromeDriver.findElement(By.cssSelector("p.s1i8jpjg-1.cmQAcE"));*/


        } catch (NoSuchElementException nse) {
            System.out.println("element not found!");
            nse.printStackTrace();
        } catch (TimeoutException tex) {
            System.out.println("Took too long!");
            tex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("u fucked up and im not sure how");
        }
        System.out.println(links);
        System.out.println("yo");
        System.out.println(CBT + " links obtained");


        w1.start();

    }
}
