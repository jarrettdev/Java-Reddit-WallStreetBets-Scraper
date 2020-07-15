package Yeet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class WebBot {
    WebDriver chromeDriver;
    String profileDirectoryArgument;

    void navigate(String url){
        initBot(url);

    }

    void initBot(String url){ // initialize bot
        profileDirectoryArgument = "user-data-dir=C:/Users/me/AppData/Local/Google/Chrome/User Data/";
        //Create a map to store  preferences
        Map<String, Object> prefs = new HashMap<String, Object>();
        //add key and value to map as follow to switch off browser notification
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        // set ExperimentalOption - prefs
        options.setExperimentalOption("prefs", prefs);
        options.addArguments(profileDirectoryArgument);
        options.addArguments("--disable-notifications");
        options.addArguments("profile-directory=Profile 1");
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium Drivers\\chromedriver.exe");
        chromeDriver = new ChromeDriver(options);
        chromeDriver.get(url);
    }
}
