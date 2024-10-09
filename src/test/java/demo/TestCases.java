package demo;

//import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.asserts.SoftAssert;
import java.util.List;

import java.time.Duration;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider { // Lets us read the data
        ChromeDriver driver;
        Wrappers wrapper;

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
                wrapper = new Wrappers(driver);

        }

        @Test(enabled = true)
        public void testCase01() throws InterruptedException {
                wrapper.openURL("https://www.youtube.com");
                Thread.sleep(5000);
                Assert.assertEquals(wrapper.getCurrentURL(), "https://www.youtube.com/", "URL is incorrect");
                // wrapper.click(By.xpath("//button[@aria-label='Guide']"));
                // Thread.sleep(3000);
                // Click on "About" at the bottom of the sidebar
                // Find the element first
                WebElement aboutLink = driver.findElement(By.xpath("//a[@href='https://www.youtube.com/about/']"));

                // Use JavaScript to scroll to the element and click it
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", aboutLink);
                js.executeScript("arguments[0].click();", aboutLink);

                System.out.println("About section clicked.");
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.urlContains("about"));
                WebElement msg = wrapper.findElement(By.xpath(
                                "//main[@id='content']"));
                System.out.println(msg.getText());

        }

        @Test(enabled = true)
        public void testCase02() throws InterruptedException {
                wrapper.openURL("https://www.youtube.com");
                Boolean containsCategory = false;

                // Scroll to the extreme right (implement scrolling logic)
                try {
                        Thread.sleep(5000);
                        WebElement films = wrapper.findElement(By.xpath("//a[@href='/feed/storefront?bp=ogUCKAU%3D']"));
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("arguments[0].scrollIntoView(true);",
                                        films);
                        js.executeScript("arguments[0].click();", films);
                        Thread.sleep(5000);

                        // WebElement moviesTab =
                        // wrapper.findElement(By.xpath("//a[@href='/feed/storefront?bp=ogUCKAU%3D']"));
                        // moviesTab.click();

                        // Scroll to "Top Selling" section and navigate to extreme right
                        // WebDriverWait wait = new WebDriverWait(driver, 10);
                        // WebElement topSellingSection =
                        // wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),
                        // 'Top Selling')]/following-sibling::div")));

                        // Locate the right arrow button and scroll to the extreme right
                        WebElement rightArrow = wrapper.findElement(By.xpath("//button[@aria-label='Next']"));
                        while (rightArrow.isDisplayed()) {

                                Thread.sleep(500); // Small wait to ensure smooth scrolling
                        }

                        // Collect all movies in the Top Selling section
                        SoftAssert softAssert = new SoftAssert();
                        WebElement lastMovie = wrapper.findElement(By.xpath(
                                        "//ytd-grid-movie-renderer[@class='style-scope yt-horizontal-list-renderer'][last()]"));

                        WebElement MovieName = lastMovie.findElement(By.xpath("//span[@id='video-title']"));
                        String MovieNameString = MovieName.getText();

                        // Check if the last movie is marked "A" for Mature
                        String rating = lastMovie.findElement(By.xpath(
                                        ".//div[@class='badge  badge-style-type-simple style-scope ytd-badge-supported-renderer style-scope ytd-badge-supported-renderer']"))
                                        .getAttribute("aria-label");
                        softAssert.assertTrue(rating.equals("U"), "Last movie is not marked 'U' for Mature:");

                        // Check if the last movie has a category such as "Comedy", "Animation", "Drama"
                        String categoryText = lastMovie.findElement(By.xpath(
                                        ".//span[@class='grid-movie-renderer-metadata style-scope ytd-grid-movie-renderer']"))
                                        .getText();

                        String[] categories = { "Comedy", "Animation", "Drama" };

                        // Check if the message text contains any of the expected categories

                        for (String category : categories) {
                                if (categoryText.contains(category)) {
                                        containsCategory = true;
                                        break;
                                }
                        }

                        // Use softAssert to check the condition
                        softAssert.assertTrue(containsCategory,
                                        "The text does not contain any of the expected categories: " + categoryText);
                        System.out.println(MovieNameString + " : " + rating + "  " + categoryText);

                        // Assert all
                        softAssert.assertAll();
                }

                catch (Exception e) {
                        e.printStackTrace();
                }
        }

        @Test(enabled = true)
        public void testCase03() throws InterruptedException {
                wrapper.openURL("https://www.youtube.com");
                Thread.sleep(5000);
                WebElement musicElement = wrapper
                                .findElement(By.xpath("//a[@href='/channel/UC-9-kyTW8ZkZNDHQJ6FgpwQ']"));
                musicElement.click();
                Thread.sleep(30000);
                // Scroll to the extreme right (implement scrolling logic)

                WebElement scroll = wrapper.findElement(By.xpath(
                                "(//button[@aria-label='Next'])[1]"));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);",
                                scroll);
                // scroll.click();
                // Thread.sleep(2000);
                while (scroll.isDisplayed()) {
                        js.executeScript("arguments[0].click();", scroll);
                        Thread.sleep(2000); // Small wait to ensure smooth scrolling
                }
              //  WebElement CompletePlaylist= wrapper.findElement(By.xpath("(//div[@id='scroll-container' and @class='style-scope yt-horizontal-list-renderer'])[3]"));
                 WebElement playlist = wrapper.findElement(By.xpath("(//a[@class='yt-simple-endpoint style-scope ytd-compact-station-renderer' and @href='/playlist?list=RDCLAK5uy_kjNBBWqyQ_Cy14B0P4xrcKgd39CRjXXKk&playnext=1&index=1'])[1]"));
                             
                // Print the name of the playlist
                System.out.println("PlayList   :"+ playlist.getText());
                WebElement playlistName = playlist
                                .findElement(By.xpath(".//h3[contains(text(), 'Bollywood Dance Hitlist')]"));
                System.out.println("Playlist Name: " + playlistName.getText());

                String numberOfTracksText = ""; // Implement logic to get the number of tracks
                WebElement NoOfTracks = playlist.findElement(By.xpath(".//p[@id='video-count-text']"));
                String text = NoOfTracks.getText();
                System.out.println(text);
                numberOfTracksText = text.split(" ")[0];
                int numberOfTracks = Integer.valueOf(numberOfTracksText);
                SoftAssert softAssert = new SoftAssert();
                softAssert.assertTrue(numberOfTracks <= 50, "Number of tracks exceeds 50");
        }

        @Test(enabled = true)
        public void testCase04() throws InterruptedException {
                wrapper.openURL("https://www.youtube.com");
                Thread.sleep(5000);
                int NewsLikes;

                WebElement newsTab = wrapper.findElement(By.xpath("//a[@href='/channel/UCYfdidRxbB8Qhf0Nx7ioOYw']"));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);",
                                newsTab);
                js.executeScript("arguments[0].click();", newsTab);
                Thread.sleep(2000);

                int totalLikes = 0;
                List<WebElement> newsPosts = driver
                                .findElements(By.xpath(
                                                "//ytd-post-renderer[@class='style-scope ytd-rich-item-renderer']"));

                List<WebElement> likes = driver.findElements(By.id("vote-count-middle"));
                for (int i = 1; i <= 3; i++) {
                        WebElement newspost = newsPosts.get(i);
                        WebElement Title = newspost
                                        .findElement(By.xpath(
                                                        ".//div[@id='header' and @class='style-scope ytd-post-renderer']"));
                        String TitleText = Title.getText();
                        System.out.println(TitleText);
                        WebElement newsBody = newspost.findElement(
                                        By.xpath(".//yt-formatted-string[@id='home-content-text']"));
                        String newsBodyText = newsBody.getText();

                        WebElement NewsLikesElement = likes.get(i);

                        String NewsLikesText = NewsLikesElement.getText();
                        if (NewsLikesText.isEmpty()) {
                                NewsLikes = 0;
                        } else {
                                NewsLikes = Integer.valueOf(NewsLikesText);
                                totalLikes = totalLikes + NewsLikes;
                        }
                        System.out.println(TitleText + "  " + newsBodyText + "  " + NewsLikes);

                }
                System.out.println("totalLikes   " + totalLikes);

                // Print title and body

        }

        @Test(dataProvider = "excelData", enabled = true)
        public void testCase05(String to_be_searched) throws InterruptedException {
                wrapper.openURL("https://www.youtube.com");
                Thread.sleep(5000);

                // Search for the item
                wrapper.findElement(By.xpath("//input[@id='search']")).sendKeys(to_be_searched);
                wrapper.findElement(By.xpath("//button[@id='search-icon-legacy']")).click();

                // Wait for the results to load
                try {
                        Thread.sleep(3000);
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }

                double totalViews = 0;
                while (totalViews < 100_000_000) { // 10 Crore = 100,000,000 views
                        List<WebElement> videoElements = driver.findElements(
                                        By.xpath("(//div[@class='text-wrapper style-scope ytd-video-renderer']) | (//a[@class='yt-simple-endpoint style-scope ytd-promoted-video-renderer'])"));
                        for (WebElement video : videoElements) {
                                WebElement views = video.findElement(By.xpath(".//span[contains(text(),'views')]"));
                                String viewText = views.getText();
                                if (viewText == null || viewText.trim().isEmpty()) {
                                        System.out.println("No views available for this video. Skipping...");
                                        continue; // Skip to the next video element
                                }
                                System.out.println(viewText);
                                totalViews += wrapper.convertViewTextToNumber(viewText);
                                if (totalViews >= 100_000_000) {
                                        System.out.println("Reached 10 Crore views for search: " + to_be_searched);
                                        return;
                                }
                        }

                        // Scroll down to load more results
                        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1000);");
                        try {
                                Thread.sleep(2000);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
        }

        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();

        }
}