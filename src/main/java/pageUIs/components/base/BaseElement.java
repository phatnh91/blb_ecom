package pageUIs.components.base;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import pageObjects.BasePage;
import pageUIs.webdriver.FVPageFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Driver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public abstract class BaseElement extends BasePage implements WebElement, IBaseElement {

    WebElement rootElement;
    static long totalWaitTime = 0;
    public static String BASE_LOCATION = "src" + File.separatorChar + "main" + File.separatorChar + "resources"
            + File.separatorChar;
//    public static String BASE_FILE_LOCATION = System.getProperty("user.dir") + fileSeparator+"src"+fileSeparator+"test"+fileSeparator+"resources"+fileSeparator+"files"+fileSeparator;

    public BaseElement(WebElement assignElement){
        this.rootElement = assignElement;
        FVPageFactory.initElements(this.rootElement, this);
    }


    public WebElement getRootElement(){
        return findElement(By.xpath("."));
    }


    public boolean isElementInViewPort(WebDriver driver) {
        boolean isElementInViewport = (Boolean) ((JavascriptExecutor) Objects.requireNonNull(driver)).executeScript(
                "var rect = arguments[0].getBoundingClientRect();\n" +
                        "return (\n" +
                        "    rect.top >= 0 &&\n" +
                        "    rect.left >= 0 &&\n" +
                        "    rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&\n" +
                        "    rect.right <= (window.innerWidth || document.documentElement.clientWidth)\n" +
                        ");", getRootElement());
        if (!isElementInViewport) {
            System.out.println("Element is not in the viewport");
        }
        return isElementInViewport;
    }

    public void sleep(Integer ms) {
        ms = ms != null ? ms : 4000;
        try {
            totalWaitTime += ms;
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void waitVisible(){
        int count = 0;
        do{
            count++;
            sleep(1000);
            if(count>20){
                break;
            }
        }while (!isDisplayed());
    }

    public void waitVisible(int sec){
        int count = 0;
        do{
            count++;
            sleep(1000);
            if(count>sec){
                break;
            }
        }while (!isDisplayed());
    }


    public void waitEnable(){
        int count = 0;
        do{
            count++;
            sleep(1000);
            if(count>20){
                break;
            }
        }while (!isEnabled());
    }

    public void click(){
            if (!isElementInViewPort()) {
                scroll();
            }
            try{
                getRootElement().click();
            }
            catch (Exception e){
            }
    }

    public void jsClick(WebDriver driver){
            scroll();
            waitEnable();
            try{
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getRootElement());
            }
            catch (Exception ignored){
            }

    }


    public void sendKeys(CharSequence... keysToSend){
        waitVisible();
            if (!isElementInViewPort()) {
                scroll();
            }
            waitVisible();
            waitEnable();
            rootElement.sendKeys(keysToSend);
    }

    public void submit(){
            waitVisible();
            rootElement.submit();
    }

    public void clear(){
            waitVisible();
            rootElement.sendKeys(Keys.CONTROL+"A");
            rootElement.sendKeys(Keys.DELETE);
    }


    @Override
    public String getAttribute(String name) {
        return rootElement.getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return rootElement.isSelected();
    }

    @Override
    public boolean isEnabled() {
                waitVisible();
                return rootElement.isEnabled();
    }

    @Override
    public String getText() {
                    if(!isElementInViewPort()){
                        scroll();
                        waitVisible();
                    }
                    return rootElement.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return rootElement.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return rootElement.findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        try{
            return rootElement.isDisplayed();
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public Point getLocation() {
        return rootElement.getLocation();
    }

    @Override
    public Dimension getSize() {
        return rootElement.getSize();
    }

    @Override
    public Rectangle getRect() {
        return rootElement.getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return rootElement.getCssValue(propertyName);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return rootElement.getScreenshotAs(target);
    }

    public void verifyAttribute(String attribute, String expectation, String message){
        Assert.assertEquals(rootElement.getAttribute(attribute), expectation, message);
    }

    public void verifyAttributeContains(String attribute, String containValue, String message){
        Assert.assertTrue(rootElement.getAttribute(attribute).contains(containValue), message);
    }

    public boolean checkForElementTextContains(String containedText, boolean isCaseSensitive) {
        try {
            waitVisible();
            scroll();
            String elementText = getText();
            if (isCaseSensitive)
                return elementText.contains(containedText);
            else
                return elementText.toLowerCase().contains(containedText.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkForElementTextEquals(String expectedEqualText, boolean isCaseSensitive) {
        try {
            waitVisible();
            scroll();
            String elementText = getText();
            if (isCaseSensitive)
                return elementText.equals(expectedEqualText);
            else
                return elementText.equalsIgnoreCase(expectedEqualText);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementPresent(By by) {
        try {
            WebElement element = rootElement.findElement(by);
            return element.isDisplayed(); // Element is found
        } catch (Exception e) {
            return false; // Element is not found
        }
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

    public static boolean isImageNotBroken(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();

            return (responseCode == 200);
        } catch (Exception e) {
            e.fillInStackTrace();
            return false;
        }
    }

    public abstract void scrollTo();

    public abstract void jsClick();
}
