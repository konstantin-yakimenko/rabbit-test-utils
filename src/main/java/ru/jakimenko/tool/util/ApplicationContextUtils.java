package ru.jakimenko.tool.util;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Application context utilities.
 * @author kyyakime
 */
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        ctx = appContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }
    
    public static <T> T getBean(final Class<T> clazz) {
        return (T)ctx.getBean(clazz);
    }
    
    public static <T> T getBean(String name, final Class<T> clazz) {
        return ctx.getBean(name, clazz);
    }    
    
    public static String getLocalized(String name) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Locale locale = facesContext.getViewRoot().getLocale();
        return ResourceBundle.getBundle(DateUtil.MESSAGE_BUNDLE_NAME, locale).getString(name);
    }
    
}
