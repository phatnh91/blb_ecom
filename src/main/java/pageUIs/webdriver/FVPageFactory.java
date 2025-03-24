package pageUIs.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import pageUIs.components.base.BaseElement;
import pageUIs.components.base.IBaseElement;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class FVPageFactory extends DefaultFieldDecorator {

    public FVPageFactory(WebDriver driver){
        super(new DefaultElementLocatorFactory(driver));
    }
    public FVPageFactory(WebElement element) {
        super(new DefaultElementLocatorFactory(element));
    }
    public static void initElements(WebElement element, Object page){
        PageFactory.initElements(new FVPageFactory(element), page);
    }

    public static void initElements(WebDriver driver, Object page){
        PageFactory.initElements(new FVPageFactory(driver), page);
    }

    protected boolean isDecoratableList(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        } else {
            Type genericType = field.getGenericType();
            if (!(genericType instanceof ParameterizedType)) {
                return false;
            } else {
                Type listType = ((ParameterizedType)genericType).getActualTypeArguments()[0];
                if (!IBaseElement.class.isAssignableFrom((Class<?>) listType)) {
                    return false;
                } else {
                    return field.getAnnotation(FindBy.class) != null || field.getAnnotation(FindBys.class) != null || field.getAnnotation(FindAll.class) != null;
                }
            }
        }
    }


    public Object decorate(ClassLoader loader, Field field) {
        if (!IBaseElement.class.isAssignableFrom(field.getType()) && !this.isDecoratableList(field)) {
            return null;
        }

        // Get the locator for the field from the factory
        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        } if (IBaseElement.class.isAssignableFrom(field.getType())){
            return proxyForFVElementLocator(loader, locator, field.getType());
//            return createCustomElement(field.getType(), (WebElement) super.decorate(loader, field));
        } else if (List.class.isAssignableFrom(field.getType())) {
            return proxyForFVListLocator(loader, locator, field);
//            return results;
        } else {
            return null;
        }
    }

    public static BaseElement createCustomElement(Class<?> clazz, WebElement element) {
        try {
            // Assume the custom element implementation has a constructor that takes a WebElement
            return (BaseElement) clazz.getConstructor(WebElement.class).newInstance(element);
        } catch (ReflectiveOperationException e) {
            // throw new RuntimeException("Failed to create custom WebElement implementation instance of " + clazz.toString(), e);
        }
        return null;
    }

    public static Object proxyForFVElementLocator(ClassLoader loader, ElementLocator locator, Class<?> clazz) {
        InvocationHandler handler = new FVLocatingElementHandler(locator, clazz);
        return clazz.cast(Proxy.newProxyInstance(loader, new Class[]{clazz}, handler));
    }

    public static Object proxyForFVElementApplier(ClassLoader loader, WebElement element, Class<?> clazz) {
        InvocationHandler handler = new FVApplyingElementHandler(element, clazz);
        return clazz.cast(Proxy.newProxyInstance(loader, new Class[]{clazz}, handler));
    }

    public static List<Object> proxyForFVListLocator(ClassLoader loader, ElementLocator locator, Field field) {
        InvocationHandler handler = new FVLocatingElementListHandler(loader, locator, field);
        List<Object> proxy = (List) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
        return proxy;
    }


    public static class FVLocatingElementHandler implements InvocationHandler{

        private final ElementLocator locator;
        private Class<?> clazz;
        public FVLocatingElementHandler(ElementLocator locator, Class<?> clazz) {
            this.locator = locator;
            this.clazz = clazz;
        }

        public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
            WebElement element = null;

            if (clazz.isAssignableFrom(WebElement.class)) {
                element = this.locator.findElement();
                return method.invoke(element, objects);
            }
            // else
            try {
                element = (WebElement) FVPageFactory.proxyForFVElementLocator(clazz.getClassLoader(), this.locator, WebElement.class);
            } catch (Exception e){
            }

            Method staticMethod = clazz.getDeclaredMethod("createInstance", WebElement.class);
            Object instance;
            try {
                instance = staticMethod.invoke(null, element);
                return method.invoke(instance, objects);
            } catch (Exception e) {
                throw e.getCause();
            }
        }
    }

    public static class FVApplyingElementHandler implements InvocationHandler{

        private WebElement element;
        private Class<?> clazz;
        public FVApplyingElementHandler(WebElement element, Class<?> clazz) {
            this.element = element;
            this.clazz = clazz;
        }

        public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
            Method staticMethod = clazz.getDeclaredMethod("createInstance", WebElement.class);
            BaseElement baseElement = (BaseElement) staticMethod.invoke(null, element);
            try {
                return method.invoke(baseElement, objects);
            } catch (InvocationTargetException var6) {
                throw var6.getCause();
            }
        }
    }

    public static class FVLocatingElementListHandler implements InvocationHandler{

        private final ElementLocator locator;
        private Class<?> clazz;

        private ClassLoader loader;
        public FVLocatingElementListHandler(ClassLoader loader, ElementLocator locator, Field field) {
            this.locator = locator;
            this.clazz = (Class<?>) (((ParameterizedType) field.getGenericType()).getActualTypeArguments())[0];
            this.loader = loader;
        }

        public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
            List<WebElement> elements = this.locator.findElements();
            List<Object> customElements = new ArrayList<>();
            for (WebElement element : elements) {
                customElements.add(FVPageFactory.proxyForFVElementApplier(loader, element, clazz));
            }

            try {
                return method.invoke(customElements, objects);
            } catch (InvocationTargetException var6) {
                throw var6.getCause();
            }
        }
    }
}
