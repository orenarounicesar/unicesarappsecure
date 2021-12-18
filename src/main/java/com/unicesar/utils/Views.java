
package com.unicesar.utils;

import com.unicesar.views.ConsultarNotasView;
import com.unicesar.views.LoginView;
import com.unicesar.views.MainView;
import com.vaadin.navigator.Navigator;
import com.unicesar.views.RegistrarNotasView;


public class Views {
    
    public static final String MAIN = "MAINVIEW";
    public static final String LOGIN = "LOGINVIEW";
    public static final String REGISTRARNOTAS = "REGISTRARNOTASVIEW";
    public static final String CONSULTARNOTAS = "CONSULTARNOTASVIEW";
    
    
    public static void setViewsUI(Navigator vNavigator){
        vNavigator.addView(Views.LOGIN, LoginView.class);
        vNavigator.addView(Views.MAIN, MainView.class);
        vNavigator.addView(Views.REGISTRARNOTAS, RegistrarNotasView.class);
        vNavigator.addView(Views.CONSULTARNOTAS, ConsultarNotasView.class);
        
    }

    
}
