package pageUIs.components.items;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageUIs.components.base.BaseElement;
import pageUIs.components.simpleelement.ISimpleElement;

public class Item extends BaseElement implements IItem {

    public static final String MAIN_SEARCH_TEXT_BOX = ".//input[@data-id='search-query']";
    public static final String SEARCH_BUTTON = ".//button[@data-id='gnav-search-submit-button']";
    public static final String IMAGE_LINK = ".//a[contains(@class,listing-link)]//img";
    public static final String IMAGE_TITLE = ".//a[contains(@class,listing-link)]//h3";

    @FindBy(xpath = MAIN_SEARCH_TEXT_BOX)
    ISimpleElement searchTextBox;

    @FindBy(xpath = SEARCH_BUTTON)
    ISimpleElement searchButton;

    @FindBy(css = IMAGE_LINK)
     ISimpleElement imageLink;

    @FindBy(css = IMAGE_TITLE)
    ISimpleElement imageTitle;

    public Item(WebElement assignElement) {
        super(assignElement);
    }

    @Override
    public void scrollTo() {

    }

    @Override
    public void jsClick() {

    }

    @Override
    public void scroll() {

    }

    @Override
    public void svgClick() {

    }

    @Override
    public boolean isElementInViewPort() {
        return false;
    }

    @Override
    public void waitForSpinner() {

    }

    @Override
    public String getTagName() {
        return "";
    }

    public String getItemLink(){
        return imageLink.getAttribute("src");
    }

    public String getItemTitle(){
        return imageTitle.getText();
    }
}

