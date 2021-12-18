
package com.unicesar.businesslogic;

import com.vaadin.ui.Notification;
import java.sql.SQLException;


public class Notifications {
    
    public static void getErrorSql(SQLException ex){
        Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);    
    }
    
    public static void getErrorSql(NullPointerException ex){
        Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);    
    }
    
    public static void getError(String ex){
        Notification.show(ex, Notification.Type.ERROR_MESSAGE);    
    }
    
    public static void getMessageProcessConfirmation(String ex){
        Notification.show(ex, Notification.Type.HUMANIZED_MESSAGE);    
    }
    
}
