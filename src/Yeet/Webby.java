package Yeet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import Yeet.Expery;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Webby extends Thread {

    Document doc1, doc2;
    String url;
    String wsbUrl;
    ArrayList<String> commentStorage = new ArrayList<>();
    ArrayList<String> stockStorage = new ArrayList<>();
    ArrayList<String> lineStorage = new ArrayList<>();
    ArrayList<String> blacklist = new ArrayList<>();
    List<String> fixedStocks;
    List<String> listWithoutDuplicates;
    String userTxt;
    String currLine;
    String threadLink;
    String rowClass = "_3cjCphgls6DH-irkVaA0GM"; // class of the comments on Reddit thread (changes all the time)
    Investopedia i1;
    int BDE = 1;
    int quantity;

    void openRedditThread() {
        //stockStorage = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        for (String link : Expery.links) {   //////YOOOO THIS IS BIG FIX THIS
            try {
                doc1 = (Jsoup.connect(link).get());
                int count = 0;
//            ArrayList<String> upvoteStorage = new ArrayList<>();
//            ArrayList<String> userNameStorage = new ArrayList<>();

                for (Element row : doc1.select("div._3tw__eCCe7j-epNCKGXUKk ")) { // **check this one and the one below it periodically, they change from time to time**
                    commentStorage.add(row.select("div._3cjCphgls6DH-irkVaA0GM").text()); // these are the comments right here
//                for (Element upvoteCount : row.select("span.s5yqo3m-0.hvtvTU")) {
//                    if (!upvoteCount.text().equals("·")) {
//                        upvoteStorage.add(upvoteCount.text());
//                    }
//                }

                    //System.out.println(upvoteStorage);
//                for (Element userName : row.select("div.s1qo48hh-0.bThxNZ")) {
//                    userNameStorage.add(userName.text());
//                }
//                userNameStorage.remove(0);
                    //System.out.println(userNameStorage);

                    //for (Element comment2 : doc1.select("div.s3adwah-2.kdTuBX")) {
                    //System.out.println(comment2.text());
                    //commentStorage.add(comment2.text());//beta
                    //}
                }

                //for (Element comment : doc1.select("div.c497l3-6.eCeBkc.s1hmcfrd-0.bUYlDz")){
                String testy = "";
                //put it here

                //}
                //System.out.println(commentStorage);


                //System.out.println(stockStorage);

                System.out.println(stockStorage + " " + BDE);

                BDE++;
                //Thread.sleep(100);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            save("testy2.txt"); //added from openRedditSite() to work with the new web driver
            analyze();
            listFix();
            saveStock("mystocklist.txt");
            System.out.println(lineStorage + "this aint it");
            System.out.println("YOOOOOO");
            saveComment("flagged comments.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
        relay();


    }

    void bcRelay() {
        try {
            System.out.println(blacklist);
            System.out.println(fixedStocks);
            System.out.println(listWithoutDuplicates);
            BigCharts b1 = new BigCharts();
            b1.loadRobinhood();
            System.out.println("BigCharts Opened");
            for (String stock : listWithoutDuplicates) {
                quantity = Collections.frequency(stockStorage, stock);
                System.out.println("New Stock : " + stock);
                System.out.println("The frequency of " + stock + " is " + quantity);
                if (stock.startsWith("$")) {
                    System.out.println("STOCK STARTS WITH $");
                    System.out.println();
                    stock.equals(stock.substring(2));
                    System.out.println("new stock : " + stock);
                    b1.setHotStock(stock);
                    b1.setStockQuantity(quantity);
                    b1.findStock();

                } else {
                    System.out.println("NORMAL");
                    System.out.println();
                    b1.setHotStock(stock);
                    b1.setStockQuantity(quantity);
                    b1.findStock();
                }
                System.out.println("Previous Stock: " + stock);

            /*i1.setHotStock(stock);
            Thread.sleep(11000);
            i1.purchaseStock();*/


            }


            b1.loginToRobinhood();
            b1.rhProcess();
            b1.robinhoodDriver.close();
            b1.robinhoodDriver.quit();
            System.out.println(b1.invalidStocks);
            System.out.println("PURCHASED: " + b1.purchasedStocks);
            b1.writeBadStocksToFile("newBadStocks.txt");
            //i1.removeDuplicates();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    void bingRelay() {
        try {
            System.out.println(blacklist);
            System.out.println(fixedStocks);
            System.out.println(listWithoutDuplicates);
            Bing b1 = new Bing();
            b1.loadRobinhood();
            //b1.login();
            System.out.println("Bing Opened");
            for (String stock : listWithoutDuplicates) {
                quantity = Collections.frequency(stockStorage, stock);
                System.out.println("New Stock : " + stock);
                System.out.println("The frequency of " + stock + " is " + quantity);
                if (stock.startsWith("$")) {
                    System.out.println("STOCK STARTS WITH $");
                    System.out.println();
                    stock.equals(stock.substring(1, stock.length()));
                    b1.setHotStock(stock);
                    b1.setStockQuantity(quantity);
                    b1.findStock();

                } else {
                    System.out.println("NORMAL");
                    System.out.println();
                    b1.setHotStock(stock);
                    b1.setStockQuantity(quantity);
                    b1.findStock();
                }
                System.out.println("Previous Stock: " + stock);

            /*i1.setHotStock(stock);
            Thread.sleep(11000);
            i1.purchaseStock();*/


            }

            b1.chromeDriver.close();
            b1.chromeDriver.quit();
            b1.loginToRobinhood();
            b1.rhProcess();
            b1.robinhoodDriver.close();
            b1.robinhoodDriver.quit();
            System.out.println(b1.invalidStocks);
            System.out.println("PURCHASED: " + b1.purchasedStocks);
            b1.writeBadStocksToFile("newBadStocks.txt");
            //i1.removeDuplicates();
            Expery.mealRunning = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void googleRelay() {
        try {
            System.out.println(blacklist);
            System.out.println(fixedStocks);
            System.out.println(listWithoutDuplicates);
            Google g1 = new Google();
            g1.loadRobinhood();
            System.out.println("Google Opened");
            for (String stock : listWithoutDuplicates) {
                quantity = Collections.frequency(stockStorage, stock);
                System.out.println("New Stock : " + stock);
                System.out.println("The frequency of " + stock + " is " + quantity);
                if (stock.startsWith("$")) {
                    System.out.println("STOCK STARTS WITH $");
                    System.out.println();
                    stock.equals(stock.substring(1, stock.length()));
                    g1.setHotStock(stock);
                    g1.setStockQuantity(quantity);
                    g1.findStock();

                } else {
                    System.out.println("NORMAL");
                    System.out.println();
                    g1.setHotStock(stock);
                    g1.setStockQuantity(quantity);
                    g1.findStock();
                }
                System.out.println("Previous Stock: " + stock);

            /*i1.setHotStock(stock);
            Thread.sleep(11000);
            i1.purchaseStock();*/


            }

            g1.chromeDriver.close();
            g1.chromeDriver.quit();
            g1.loginToRobinhood();
            g1.rhProcess();
            g1.robinhoodDriver.close();
            g1.robinhoodDriver.quit();
            System.out.println(g1.invalidStocks);
            System.out.println("PURCHASED: " + g1.purchasedStocks);
            g1.writeBadStocksToFile("newBadStocks.txt");
            //i1.removeDuplicates();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void relay() {
        try {
            System.out.println(blacklist);
            System.out.println(fixedStocks);
            System.out.println(listWithoutDuplicates);
            Investopedia i1 = new Investopedia();
            i1.loadRobinhood();
            i1.login();
            //i1.start();
            System.out.println("Investopedia Opened");

            for (String stock : listWithoutDuplicates) {
                quantity = Collections.frequency(stockStorage, stock);
                System.out.println("New Stock : " + stock);
                System.out.println("The frequency of " + stock + " is " + quantity);
                if (stock.startsWith("$")) {
                    System.out.println("STOCK STARTS WITH $");
                    System.out.println();
                    stock.equals(stock.substring(1, stock.length()));
                    i1.setHotStock(stock);
                    i1.setStockQuantity(quantity);
                    i1.purchaseStock();
                    i1.join();

                } else {
                    System.out.println("NORMAL");
                    System.out.println();
                    i1.setHotStock(stock);
                    i1.setStockQuantity(quantity);
                    i1.purchaseStock();
                    i1.join();
                }
                System.out.println("Previous Stock: " + stock);

            /*i1.setHotStock(stock);
            Thread.sleep(11000);
            i1.purchaseStock();*/


            }
            i1.chromeDriver.close();
            i1.chromeDriver.quit();
            i1.loginToRobinhood();
            i1.rhProcess();
            i1.robinhoodDriver.close();
            i1.robinhoodDriver.quit();
            System.out.println(i1.invalidStocks);
            System.out.println("PURCHASED: " + i1.purchasedStocks);
            i1.writeBadStocksToFile("newBadStocks.txt");
            //i1.removeDuplicates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void frequencyCheck(ArrayList<String> stocks) {
        for (String s : stocks) {
            for (String s2 : stocks) {
                if (s2.equals(s)) {
                    s += "*";
                    stocks.add(s);
                }
            }
        }
    }


    void regexChecker(String regex, String strToCheck) {

        Pattern checkRegex = Pattern.compile(regex);
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

    }

    void listFix() {
        System.out.println("blacklist time-----");
        System.out.println(blacklist);
        fixedStocks = new ArrayList<>(stockStorage);
        fixedStocks.removeAll(blacklist);
        listWithoutDuplicates = fixedStocks.stream().distinct().collect(Collectors.toList());
    }

    void analyze() {
        String escapedString = Pattern.quote("$");
        try {
            Scanner sc = new Scanner(new File("testy2.txt"));

            while (sc.hasNextLine()) {
                currLine = sc.nextLine();
                if (currLine.matches("([\\$][A-Za-z]{2,5})")) {
                    //System.out.println(currLine);
                }
                regexChecker("([\\$][A-Za-z]{2,5} | [A-Z]{2,5} | [\\$][A-Za-z]{2,5}+, | [A-Z]{2,5}+,)", currLine);


            }
            sc.close();
        } catch (Exception e) {

        }
    }

    void saveStock(String fileName) throws FileNotFoundException {
        initBlacklist();
        PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
        for (String stock : listWithoutDuplicates) {
            pw.println(stock);
            pw.println("********************");
        }
        pw.println("END OF RUN");
        pw.close();
//        for (String stock : stockStorage) {
//            for(String badStock : blacklist){
//                if(stock.equals(badStock)){
//                    stockStorage.remove(stock);
//                }else{
//                    break;
//                }
//            }
//            pw.println(stock);
//            pw.println("********************");
//        }
//        pw.close();
    }

    void save(String fileName) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(fileName), false);
        for (String comment : commentStorage) {
            pw.println(comment);
            pw.println("********************");
        }
        pw.println("END OF RUN");
        pw.close();
    }

    void saveComment(String fileName) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(fileName), false);
        for (String line : lineStorage) {
            pw.println(line);
            pw.println("********************");
        }
        pw.println("END OF RUN");
        pw.close();
    }


    @Override
    public void run() {
        System.out.println("running : " + Expery.mealRunning);
        initBlacklist();
        newReddit();
    }

    void openRedditSite() {
        wsbUrl = "https://www.reddit.com/r/wallstreetbets/";
        Scanner in = new Scanner(System.in);
        try {

            //System.out.println("Where would you like to store the comments?");
            userTxt = "testy2.txt";
            doc2 = (Jsoup.connect(wsbUrl).get());
            for (Element commentThread : doc2.select("a.SQnoC3ObvgnGjWt90zD9Z")) {
                System.out.println("entry found");
                String link = "https://www.reddit.com";
                link += (commentThread.attr("href") + "?sort=top");
                System.out.println(link);

            }
            save(userTxt);
            analyze();
            saveStock("mystocklist.txt");
            saveComment("flagged comments.txt");
        } catch (Exception e) {

        }
    }

    void initBlacklist() {
        try {

            Scanner s = new Scanner(new File("Verified Baddies.txt"));
            ArrayList<String> list = new ArrayList<String>();
            while (s.hasNextLine()) {
                blacklist.add(s.nextLine());
            }
            s.close();
            blacklist.trimToSize();
            System.out.println("Blacklist : " + blacklist);
        } catch (Exception e) {

        }


    }

    void analyzeFix() {
        try {
            Scanner sc = new Scanner(new File("testy2.txt"));

            while (sc.hasNextLine()) {
                System.out.println(currLine);
                System.out.println("Done");
                //currLine = sc.nextLine();
                if (currLine.matches("([\\$][A-Za-z]{2,5})")) {
                    System.out.println(currLine);
                }
                regexCheckerFix("([\\$][A-Za-z]{2,5} | [A-Z]{2,5})", currLine);


            }
            System.out.println(stockStorage);
        } catch (Exception e) {

        }
    }

    void regexCheckerFix(String regex, String strToCheck) {

        Pattern checkRegex1 = Pattern.compile(regex);
        Matcher regexMatcher1 = checkRegex1.matcher(strToCheck);
        while (regexMatcher1.find()) {
            stockStorage.add(regexMatcher1.group().trim());
            // lineStorage.add(strToCheck);
            if (regexMatcher1.group().length() != 0) {
                //System.out.println(regexMatcher.group().trim());
            }
        }

    }

    void newReddit() {
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
        ArrayList<Double> grabRatios = new ArrayList<>();
        double percentageGrabbed = 0;
        double theRatio = 0;
        double tempRatio = 0;

        for (String link : Expery.links) { //comment grabbing loop
            try {
                int commentsGrabbed = 0;


                WebElement titleLink;
                //System.out.println("woah");
                chromeDriver.get(link);
                //wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Posted by")));
                Boolean isPresent = chromeDriver.findElements(By.xpath("//*[@id=\"SHORTCUT_FOCUSABLE_DIV\"]/div[2]/div/div/div/div[2]/div[3]/div[1]/div[1]/div[4]/div/button")).size() > 0;
                if (isPresent) {
                    WebElement viewMoreButton = chromeDriver.findElement(By.xpath("//*[@id=\"SHORTCUT_FOCUSABLE_DIV\"]/div[2]/div/div/div/div[2]/div[3]/div[1]/div[1]/div[4]/div/button"));
                    ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", viewMoreButton);
                    viewMoreButton.click();
                    Thread.sleep(100);

                } else {
                    //System.out.println("o well");
                }
                js.executeScript("window.scrollBy(0,-100000)");
                js.executeScript("window.scrollBy(0,50000)");
                Thread.sleep(100);
                js.executeScript("window.scrollBy(0,50000)");
                Thread.sleep(100);
                js.executeScript("window.scrollBy(0,-100000)");
                Thread.sleep(100);
                js.executeScript("window.scrollBy(0,50000)");
                Thread.sleep(100);
                js.executeScript("window.scrollBy(0,-50000)");
                Thread.sleep(100);
                Boolean moreReplIsPresent = chromeDriver.findElements(By.xpath("//*[contains(text(), 'more repl')]")).size() > 0;
                if (moreReplIsPresent) {
                    WebElement moreReplButton = chromeDriver.findElement(By.xpath("//*[contains(text(), 'more repl')]"));
                    ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", moreReplButton);
                    moreReplButton.click();

                } else {
                }
                //List<WebElement> listOfExtendButtons = chromeDriver.findElements(By.xpath("//*[contains(text(), 'more repl')]"));
//        for(WebElement e : listOfExtendButtons){
//            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", e);
//            e.click();
//            System.out.println("executed");
//        }


                //System.out.println("done");

                js.executeScript("window.scrollBy(0,-100000)");


                // System.out.println("ayee");

                List<WebElement> row1 = chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"));

                //TODO: END THE NEED FOR ALL THESE LOOPS CAUSED BY DYNAMIC LOADING

                //MANY LOOPS WILL FOLLOW TO GRAB A SUFFICIENT AMOUNT OF COMMENTS. SORRY
                //Boolean morePostsBannerIsPresent = chromeDriver.findElements(By.xpath("//*[contains(text(), 'More posts from the')]")).size() > 0;
                //WebElement morePostsBanner = chromeDriver.findElement(By.xpath("//*[contains(text(), 'More posts from the')]"));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), ' comment')]")));
                WebElement commentCount = chromeDriver.findElement(By.xpath("//*[contains(text(), ' comment')]"));
                //System.out.println(commentCount.getText());
                int numOfComments = (Integer.parseInt(commentCount.getText().substring(0, commentCount.getText().indexOf('C') - 1)));
                System.out.println(numOfComments + " Comments available");

                for (WebElement row : row1) { // **check this one and the one below it periodically, they change from time to time**
                    ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", row);
                    commentStorage.add(row.getText());
                }
                commentsGrabbed += row1.size();
                System.out.println(commentsGrabbed + " comments grabbed");
                //System.out.println((double) commentsGrabbed / (double) numOfComments);
                //theRatio = ((double) commentsGrabbed / (double) numOfComments);
                tempRatio = theRatio;
                js.executeScript("window.scrollBy(0,1000)");
                Thread.sleep(400);
                    List<WebElement> row2 = chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"));
                    row2.removeAll(row1);
                    for (WebElement row : row2) { // **check this one and the one below it periodically, they change from time to time**
                        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", row);
                        commentStorage.add(row.getText());
                    }
                    commentsGrabbed += row2.size();
                    theRatio = ((double) commentsGrabbed / (double) numOfComments);
                if(theRatio == tempRatio)
                    continue;
                tempRatio = theRatio;
                    System.out.println(commentsGrabbed + " comments grabbed");
                    //System.out.println((double) commentsGrabbed / (double) numOfComments);
                    js.executeScript("window.scrollBy(0,1000)");
                    Thread.sleep(400);
                    List<WebElement> row3 = chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"));
                    row3.removeAll(row2);
                    row3.removeAll(row1);

                    for (WebElement row : row3) { // **check this one and the one below it periodically, they change from time to time**
                        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", row);
                        commentStorage.add(row.getText());
                    }
                    commentsGrabbed += row3.size();
                    theRatio = ((double) commentsGrabbed / (double) numOfComments);
                if(theRatio == tempRatio)
                    continue;
                tempRatio = theRatio;
                    System.out.println(commentsGrabbed + " comments grabbed");
                    //System.out.println((double) commentsGrabbed / (double) numOfComments);
                    js.executeScript("window.scrollBy(0,1000)");
                    Thread.sleep(400);
                    List<WebElement> row4 = chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"));
                    row4.removeAll(row3);
                    row4.removeAll(row2);
                    row4.removeAll(row1);

                    for (WebElement row : row4) { // **check this one and the one below it periodically, they change from time to time**
                        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", row);
                        commentStorage.add(row.getText());
                    }
                    commentsGrabbed += row4.size();
                    theRatio = ((double) commentsGrabbed / (double) numOfComments);
                if(theRatio == tempRatio)
                    continue;
                tempRatio = theRatio;
                    System.out.println(commentsGrabbed + " comments grabbed");
                    //System.out.println((double) commentsGrabbed / (double) numOfComments);
                    js.executeScript("window.scrollBy(0,1000)");
                    Thread.sleep(400);
                    List<WebElement> row5 = chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"));
                    row5.removeAll(row2);
                    row5.removeAll(row1);
                    row5.removeAll(row3);
                    row5.removeAll(row4);
                    for (WebElement row : row5) { // **check this one and the one below it periodically, they change from time to time** <-- disregard that comment lol
                        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", row);
                        commentStorage.add(row.getText());
                    }
                    commentsGrabbed += row5.size();
                    theRatio = ((double) commentsGrabbed / (double) numOfComments);
                if(theRatio == tempRatio)
                    continue;
                tempRatio = theRatio;
                    System.out.println(commentsGrabbed + " comments grabbed");
                    //System.out.println((double) commentsGrabbed / (double) numOfComments);
                    js.executeScript("window.scrollBy(0,1000)");
                    Thread.sleep(400);
                    List<WebElement> row6 = chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"));
                    row6.removeAll(row2);
                    row6.removeAll(row1);
                    row6.removeAll(row3);
                    row6.removeAll(row4);
                    row6.removeAll(row5);
                    for (WebElement row : row6) { // **check this one and the one below it periodically, they change from time to time** <-- disregard that comment lol
                        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", row);
                        commentStorage.add(row.getText());
                    }// DONE
                    commentsGrabbed += row6.size();
                    theRatio = ((double) commentsGrabbed / (double) numOfComments);
                if(theRatio == tempRatio)
                    continue;
                tempRatio = theRatio;
                System.out.println(commentsGrabbed + " comments grabbed");
                //System.out.println((double) commentsGrabbed / (double) numOfComments);
                    js.executeScript("window.scrollBy(0,1000)");
                    Thread.sleep(400);
                    List<WebElement> row7 = chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"));
                    row7.removeAll(row1);
                    row7.removeAll(row2);
                    row7.removeAll(row3);
                    row7.removeAll(row4);
                    row7.removeAll(row5);
                    row7.removeAll(row6);
                    for (WebElement row : row7) { // **check this one and the one below it periodically, they change from time to time** <-- disregard that comment lol
                        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", row);
                        commentStorage.add(row.getText());
                    }// DONE============================================================================================
                    commentsGrabbed += row7.size();
                    theRatio = ((double) commentsGrabbed / (double) numOfComments);
                if(theRatio == tempRatio)
                    continue;
                tempRatio = theRatio;
                System.out.println(commentsGrabbed + " comments grabbed");
                //System.out.println((double) commentsGrabbed / (double) numOfComments);
                    js.executeScript("window.scrollBy(0,1000)");
                    Thread.sleep(400);
                    List<WebElement> row8 = chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"));
                    row8.removeAll(row1);
                    row8.removeAll(row2);
                    row8.removeAll(row3);
                    row8.removeAll(row4);
                    row8.removeAll(row5);
                    row8.removeAll(row6);
                    row8.removeAll(row7);
                    for (WebElement row : row8) { // **check this one and the one below it periodically, they change from time to time** <-- disregard that comment lol
                        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", row);
                        commentStorage.add(row.getText());
                    }// DONE
                    commentsGrabbed += row8.size();
                    theRatio = ((double) commentsGrabbed / (double) numOfComments);
                if(theRatio == tempRatio)
                    continue;
                tempRatio = theRatio;
                System.out.println(commentsGrabbed + " comments grabbed");
                //System.out.println((double) commentsGrabbed / (double) numOfComments);
                    js.executeScript("window.scrollBy(0,1000)");
                    Thread.sleep(400);
                    List<WebElement> row9 = chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"));
                    row9.removeAll(row1);
                    row9.removeAll(row2);
                    row9.removeAll(row3);
                    row9.removeAll(row4);
                    row9.removeAll(row5);
                    row9.removeAll(row6);
                    row9.removeAll(row7);
                    row9.removeAll(row8);
                    for (WebElement row : row9) { // **check this one and the one below it periodically, they change from time to time** <-- disregard that comment lol
                        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", row);
                        commentStorage.add(row.getText());
                    }// DONE
                    commentsGrabbed += row9.size();
                    theRatio = ((double) commentsGrabbed / (double) numOfComments);
                if(theRatio == tempRatio)
                    continue;
                tempRatio = theRatio;
                System.out.println(commentsGrabbed + " comments grabbed");
                //System.out.println((double) commentsGrabbed / (double) numOfComments);
                    js.executeScript("window.scrollBy(0,1000)");
                    Thread.sleep(400);
                    List<WebElement> row10 = chromeDriver.findElements(By.className("_3cjCphgls6DH-irkVaA0GM"));
                    row10.removeAll(row1);
                    row10.removeAll(row2);
                    row10.removeAll(row3);
                    row10.removeAll(row4);
                    row10.removeAll(row5);
                    row10.removeAll(row6);
                    row10.removeAll(row7);
                    row10.removeAll(row8);
                    row10.removeAll(row9);

                    for (WebElement row : row10) { // **check this one and the one below it periodically, they change from time to time** <-- disregard that comment lol
                        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(false);", row);
                        commentStorage.add(row.getText());
                    }// LOOPS ARE DONE
                    commentsGrabbed += row10.size();
                    theRatio = ((double) commentsGrabbed / (double) numOfComments);
                if(theRatio >= .82) //note: 65 works
                    continue;
                System.out.println(commentsGrabbed + " comments grabbed");
                //System.out.println((double) commentsGrabbed / (double) numOfComments);
                    commentsGrabbed = 0;





                //double currGrabRatio = ((double)commentsGrabbed/(double)numOfComments);
                //grabRatios.add(currGrabRatio);
                //System.out.println(grabRatios);
                System.out.println(stockStorage + " " + BDE);

                BDE++;


            } catch (Exception e) {

            }
        }
        //System.out.println("GRABBY:" + grabRatios);
        //double ratioSum = grabRatios.stream().mapToDouble(Double::doubleValue).sum();
        //System.out.println("RATIO SUM: " + ratioSum);
        //double finalGrabRatio = (ratioSum / grabRatios.size());
        chromeDriver.close();
        chromeDriver.quit();


        try {
            save("testy2.txt"); //added from openRedditSite() to work with the new web driver
            analyze();
            listFix();
            saveStock("mystocklist.txt");
            System.out.println(lineStorage + "this aint it");
            System.out.println("YOOOOOO");
            saveComment("flagged comments.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
        bingRelay();
    }

    void ensureShit(){
        lineStorage.ensureCapacity(7000);
        stockStorage.ensureCapacity(300);
        commentStorage.ensureCapacity(5000);
        blacklist.ensureCapacity(2000);


    }

    private static class MyTimeTask extends TimerTask {

        @Override
        public void run() {
            new Expery().start();
        }
    }

    void timerStuff() throws Exception {
        //the Date and time at which you want to execute
        String abc = java.time.LocalDate.now().toString();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormatter.parse("2019-07-11 15:53:45");  //change date to current date

        //Now create the time and schedule it
        Timer timer = new Timer();

        //Use this if you want to execute it once
        System.out.println("next reddit scan: " + date.toString());
        timer.schedule(new MyTimeTask(), date);
    }


}
