package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {
   private WebDriver driver;

    public Wrappers(WebDriver driver) {
        this.driver=driver;
    }

    public void openURL(String url) {
        driver.get(url);
    }

    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    public void click(By locator) {
        findElement(locator).click();
    }

    public void sendKeys(By locator, String keys) {
        findElement(locator).sendKeys(keys);
    }

    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    public double convertViewTextToNumber(String viewText) {
       // viewText = viewText.toLowerCase().replaceAll(",", "");  // Normalize the text

        // Check for "lakh" and "crore" and convert to numeric format
        viewText = viewText.toLowerCase().replaceAll(",", "");  // Normalize the text

        // Handle "lakh", "crore", "million (M)", and "thousand (K)"
        if (viewText.contains("lakh")) {
            double num = Double.parseDouble(viewText.split(" ")[0]);
            return num * 1_00_000;  // 1 lakh = 100,000
        } else if (viewText.contains("crore")) {
            double num = Double.parseDouble(viewText.split(" ")[0]);
            return num * 1_00_00_000;  // 1 crore = 10,000,000
        } else if (viewText.contains("m")) {  // Check for "M" (millions)
            double num = Double.parseDouble(viewText.split("m")[0]);  // Remove the "M" and convert to double
            return num * 1_000_000;  // 1 million = 1,000,000
        } else if (viewText.contains("k")) {  // Check for "K" (thousands)
            double num = Double.parseDouble(viewText.split("k")[0]);  // Remove the "K" and convert to double
            return num * 1_000;  // 1 thousand = 1,000
  } 
  
  else {
            // Handle other cases like "views" or direct numbers
            return Double.parseDouble(viewText.split(" ")[0]);
        }
    }
        

    // Wrapper class to use the converter method
    // public static void main(String[] args) {
    //     // Test cases for the converter
    //     String[] views = { "1.2M", "500k", "3M", "750k", "1200000" };

    //     for (String viewCount : views) {
    //         System.out.printf("Original: %s, Converted: %.2fM\n", viewCount, convertToMillions(viewCount));
    //     }
    // }
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }
}
