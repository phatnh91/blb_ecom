package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;


public abstract class BasePage {
    static String fileSeparator = File.separator;
    public static String BASE_FILE_LOCATION = System.getProperty("user.dir") + fileSeparator+"downloads"+fileSeparator;

//    public static BasePage getBasePageObject() {
//
//        return new BasePage();
//
//    }
    public void openPageUrl(WebDriver driver, String pageUrl) {

        driver.get(pageUrl);

    }

    public String getPageTitle(WebDriver driver) {

        return driver.getTitle();
    }

    public String getPageUrl(WebDriver driver) {

        return driver.getCurrentUrl();
    }

    public String getPageSourceCode(WebDriver driver) {

        return driver.getPageSource();
    }

    public void backToPage(WebDriver driver) {

        driver.navigate().back();
    }

    public void forwardToPage(WebDriver driver) {

        driver.navigate().forward();
    }

    public void refreshCurrentPage(WebDriver driver) {

        driver.navigate().refresh();
    }



    public By getByXpath(String xpathLocator) {

        return By.xpath(xpathLocator);
    }

    public WebElement getWebElement(WebDriver driver, String locator) {
        return driver.findElement(getByLocator(locator));

    }

    public static void switchToTabByTitle(WebDriver driver, String title) {
        String currentWindow = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();

        for (String window : windowHandles) {
            driver.switchTo().window(window);
            if (driver.getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println("Switched to tab: " + driver.getTitle());
                return;
            }
        }

        // If no match is found, switch back to the original window
        driver.switchTo().window(currentWindow);
        System.out.println("No tab found with title containing: " + title);
    }


    public By getByLocator(String locatorType) {
        By by = null;

        if (locatorType.startsWith("xpath=") || locatorType.startsWith("Xpath=") || locatorType.startsWith("XPATH=")) {
            by = By.xpath(locatorType.substring(6));
        } else if (locatorType.startsWith("css=") || locatorType.startsWith("Css=") || locatorType.startsWith("CSS=")) {

            by = By.cssSelector(locatorType.substring(4));
        } else if (locatorType.startsWith("id=") || locatorType.startsWith("Id=") || locatorType.startsWith("ID=")) {
            by = By.id(locatorType.substring(3));

        } else if (locatorType.startsWith("class=") || locatorType.startsWith("Class=")
                || locatorType.startsWith("CLASS=")) {
            by = By.className(locatorType.substring(6));

        } else if (locatorType.startsWith("name=") || locatorType.startsWith("Name=")
                || locatorType.startsWith("NAME=")) {
            by = By.name(locatorType.substring(5));

        } else {

            throw new RuntimeException("The locator is not valid");
        }

        return by;

    }

    public List<WebElement> getListWebElement(WebDriver driver, String locator) {
        return driver.findElements(getByLocator(locator));

    }

    public void scrollToElementOnTop(WebDriver driver, String locator) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, locator));
        sleepInSecond(2);
    }

    public void scrollToElementOnDown(WebDriver driver, String locator) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(false);", getWebElement(driver, locator));
        sleepInSecond(2);
    }

    public void changeImplicitWait(WebDriver driver, long seconds){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    public void clickToElement(WebDriver driver, String xpathLocator) {
        getWebElement(driver, xpathLocator).click();
    }

    public void clickToElementByJS(WebDriver driver, WebElement element) {
        // Click the element using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);

    }

    public void sendKeysToElement(WebDriver driver, String locator, String textValue) {
        WebElement element = getWebElement(driver, locator);
        element.clear();
        element.sendKeys(textValue);
    }


    public String getElementAttribute(WebDriver driver, String locator, String attributeName) {

        return getWebElement(driver, locator).getAttribute(attributeName);

    }

    public String getElementText(WebDriver driver, String locator) {

        return getWebElement(driver, locator).getText();
    }

    public String getElementText(WebElement element) {

        return element.getText();
    }

    public void navigateBack(WebDriver driver){
        driver.navigate().back();
        waitForPageToLoad(driver);
    }

    public String getElementCssValue(WebDriver driver, String locator, String propertyName) {

        return getWebElement(driver, locator).getCssValue(propertyName);
    }


    public int getElementsSize(WebDriver driver, String locator) {

        return getListWebElement(driver, locator).size();
    }

    public void checkToDefaultCheckboxRadio(WebDriver driver, String locator) {
        WebElement element = getWebElement(driver, locator);
        if (!element.isSelected()) {
            element.click();
        }
    }

    public void uncheckToDefaultCheckboxRadio(WebDriver driver, String locator) {
        WebElement element = getWebElement(driver, locator);
        if (element.isSelected()) {
            element.click();
        }
    }

    public boolean isElementDisplayed(WebDriver driver, String locator) {

        return getWebElement(driver, locator).isDisplayed();
    }

    public boolean isElementEnabled(WebDriver driver, String locator) {

        return getWebElement(driver, locator).isEnabled();
    }

    public boolean isElementSelected(WebDriver driver, String locator) {

        return getWebElement(driver, locator).isSelected();
    }

    public void switchToFrameIframe(WebDriver driver, String locator) {

        driver.switchTo().frame(getWebElement(driver, locator));
    }

    public void switchToDefaultContent(WebDriver driver) {

        driver.switchTo().defaultContent();
    }


    public void waitForElementVisible(WebDriver driver, String locator) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(locator)));
    }

    public void waitForAllElementVisible(WebDriver driver, String locator) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(locator)));
    }

    public void waitForAllElementInvisible(WebDriver driver, String locator) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, locator)));
    }

    public void waitForElementClickable(WebDriver driver, String locator) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(locator)));
    }

    public void waitForElementPresence(WebDriver driver, String locator) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        explicitWait.until(ExpectedConditions.presenceOfElementLocated(getByXpath(locator)));
    }

    public void waitForPageToLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    public static void downloadImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        InputStream in = connection.getInputStream();
        FileOutputStream out = new FileOutputStream(new File(destinationFile));

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        out.close();
        in.close();
        System.out.println("Image downloaded successfully as " + destinationFile);
    }

    public void addDataToPropertiesFile(String folderName, String title, List<String> tags, List<String> relatedSearches, List<String> moreRelatedSearches) {
        Properties properties = new Properties();
        properties.setProperty("Title", title);
        properties.setProperty("Tags", tags.toString());
        properties.setProperty("Related Searches", relatedSearches.toString());
        properties.setProperty("More Related Searches", moreRelatedSearches.toString());
        String fileName = "item.properties";
        String folderPath = BASE_FILE_LOCATION + folderName;
        checkIfFolderExisted(folderPath);
        // Write to properties file inside the folder
        try (FileOutputStream output = new FileOutputStream(folderPath + fileSeparator + fileName)) {
            properties.store(output, "Selenium Configuration");
            System.out.println("Properties file created successfully at: " + folderPath + fileSeparator + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkIfFolderExisted(String folderPath){
        File folder = new File(folderPath);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Folder created successfully: " + folder.getAbsolutePath());
            } else {
                System.out.println("Failed to create folder.");
            }
        } else {
            System.out.println("Folder already exists: " + folder.getAbsolutePath());
        }
    }

    public static void closeTabByTitle(WebDriver driver, String title) {
        Set<String> windowHandles = driver.getWindowHandles();

        for (String window : windowHandles) {
            driver.switchTo().window(window);
            if (driver.getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println("Closing tab: " + driver.getTitle());
                driver.close();
                break;  // Stop after closing the tab
            }
        }

        // Switch back to the main window if it's still open
        if (!driver.getWindowHandles().isEmpty()) {
            for (String window : driver.getWindowHandles()) {
                driver.switchTo().window(window);
                break;
            }
        }
    }

    public void waitPageContentLoaded(WebDriver driver) {
        try {
            By loader = By.xpath("//div[@class='loader' or @class='loading' or @class='circle' or @class='spinner' or @class='loading stripe stripe-visible' or ./*[name()='svg' and @data-icon='spinner']]");
            waitForElementUnload(driver,loader);
        } catch (Exception e) {
            System.out.println("Error when loaded page content " + e.getMessage());
        }
    }

    protected void waitForElementUnload(WebDriver driver,By locator) {
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(40))
                    .pollingEvery(Duration.ofSeconds(3))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.and(
                    ExpectedConditions.numberOfElementsToBe(locator, 0),
                    ExpectedConditions.numberOfElementsToBe(By.xpath("//button//*[name()='svg' and contains(@class, 'fa-spin')]"), 0),
                    ExpectedConditions.or(
                            ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".content div"), 3),
                            ExpectedConditions.presenceOfElementLocated(By.cssSelector("main")) // Ensures "main" is present as an alternative
                    ),
                    ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//div[contains(@class,'fader')]"), 2))
            );
        } catch (Exception ignored) {
            int spinnerNum = driver.findElements(locator).size();
            if (spinnerNum > 0) {
                System.out.println(">>>>>> The number of spinners after waiting 50 sec and 2 sec interval is " + spinnerNum);
            }
        }
    }

    public void sleepInSecond(long second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
