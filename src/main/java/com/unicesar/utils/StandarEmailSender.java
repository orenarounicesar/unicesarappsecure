
package com.unicesar.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StandarEmailSender extends Thread {
    
    private final EmailSender emailSender;
    private int contador;

    public StandarEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }
    
    @Override
    public void run() {
        try {
            contador = 0;
            while ( ReceiverEmail.enviandocorreo && contador <= 100 ) {
                sleep(3000);
                contador++;
                System.out.println("En espera para enviar correo... " + emailSender.getEmailDestination());
            }
            ReceiverEmail.addEmail(emailSender);
        } catch (InterruptedException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error en sleep de envío de correo - " + SeveralProcesses.getSessionUser(), ex);
        } finally {
            
        }
    }
    
}
