
package com.unicesar.utils;

import java.io.Serializable;

public class ReceiverEmail implements Serializable {
    public static boolean enviandocorreo = false;
    public static synchronized void addEmail(EmailSender emailSender) {
        enviandocorreo = true;
        emailSender.ejecutar();
        enviandocorreo = false;
    }
    
}
