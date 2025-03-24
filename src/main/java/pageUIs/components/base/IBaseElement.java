package pageUIs.components.base;

import org.openqa.selenium.WebElement;

public interface IBaseElement extends WebElement{
    void waitVisible();
    void scroll();
    void click();
    void clear();
    String getText();
    void sendKeys(CharSequence...value);

    /**
     * Only use for web element with svg tag, or inside a svg tag
     */
    void svgClick();
    boolean isElementInViewPort();
    void waitForSpinner();
    boolean isDisplayed();
}
