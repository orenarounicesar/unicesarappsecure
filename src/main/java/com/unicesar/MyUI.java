package com.unicesar;

import com.unicesar.utils.Settings;
import com.unicesar.utils.VariablesSesion;
import com.unicesar.utils.Views;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private Navigator navigator;
    
    
    public Navigator getNavigatorUI() {
        return navigator;
    }            
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Settings settings = new Settings();
        
        navigator = new Navigator(this, this);
        
        Views.setViewsUI(navigator);
        VaadinSession.getCurrent().getSession().setMaxInactiveInterval((int)TimeUnit.MINUTES.toSeconds(Settings.MINUTOSSESION));
        
        navigator.navigateTo(Views.LOGIN);
        
        if (VaadinService.getCurrent().getDeploymentConfiguration().isProductionMode()) {
            if (UI.getCurrent().getSession().getAttribute(VariablesSesion.LOGIN) == null) {
                    navigator.navigateTo(Views.LOGIN);
            } else {
                if (!getPage().getLocation().toString().contains("!") || getPage().getLocation().toString().contains(Views.LOGIN)) {
                    navigator.navigateTo(Views.REGISTRARNOTAS);
                }
                
            }
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true, heartbeatInterval = 300, closeIdleSessions = true)
    public static class MyUIServlet extends VaadinServlet {
        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();

            CustomizedSystemMessages messages = new CustomizedSystemMessages();
            messages.setSessionExpiredCaption("Sesión Expirada");
            messages.setSessionExpiredMessage("Haga Click para Inicio Sesión");
            messages.setSessionExpiredNotificationEnabled(false);

            getService().setSystemMessagesProvider(e -> {
                return messages;
            });
            
        }
    }
}
