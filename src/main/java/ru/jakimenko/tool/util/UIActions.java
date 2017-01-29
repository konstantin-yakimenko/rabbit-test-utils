package ru.jakimenko.tool.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author kyyakime
 */
public class UIActions {
    
    public static void showMessage(String summary, String detail, FacesMessage.Severity severity) {
        final FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
        
}
