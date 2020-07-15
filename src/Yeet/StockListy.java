package Yeet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockListy extends Thread {
    ArrayList<String> stockStorage;
    String currLine;

    public void run(){
        playground();
        analyze();

    }
    void doStuff(){
    try{
    Document d = Jsoup.connect("https://www.gurufocus.com/stock_list.php?&r=USA").get();
    for (Element ticker : d.select("td.text")){
        regexChecker("([A-Z]{2,5})",ticker.text());
    }

    }catch(Exception e){

    }

    }

    void playground(){
        try{

        Scanner sc = new Scanner(new File("testy2.txt"));
        stockStorage = new ArrayList<>();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void regexChecker(String regex, String strToCheck) {
        Pattern checkRegex = Pattern.compile(regex);
        Matcher regexMatcher = checkRegex.matcher(strToCheck);
        while (regexMatcher.find()) {
            if (regexMatcher.group().length() != 0) {
                System.out.println(regexMatcher.group().trim());
            }
        }
    }



    void analyze() {
        String escapedString = Pattern.quote("$");
        try {
            //Scanner sc = new Scanner(new File("testy2.txt"));
            for(String line : stockStorage){
                regexChecker("([\\$][A-Za-z]{2,6} | [A-Z]{2,5})",line);

            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
