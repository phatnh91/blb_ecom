package pageUIs.components.simpleelement;

import org.openqa.selenium.WebElement;
import pageUIs.components.base.BaseElement;

public class SimpleElement extends BaseElement implements ISimpleElement {
    public SimpleElement(WebElement assignElement) {
        super(assignElement);
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
    public void scrollTo() {

    }

    @Override
    public void jsClick() {

    }

    @Override
    public String getTagName() {
        return "";
    }
}
