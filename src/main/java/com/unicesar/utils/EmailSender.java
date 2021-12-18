
package com.unicesar.utils;

import com.unicesar.beans.BeanFiles;
import com.unicesar.businesslogic.GestionDB;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.NamingException;

public class EmailSender {
    
    private final String emailOrigen;
    private final String password;
    private final String fileName;
    private final String emailDestination;
    private final Address[] emailsDestination;
    private final String emailIssue;
    private final String emailMessage;
    private final String datoAdicional;
    private final String cadenaSqlBitacoraEnvio;
    private final File file;
    private final ArrayList<BeanFiles> files;
    
    private boolean mensajeEnviado = false;

    public EmailSender(String emailOrigen, String password, File file, String fileName, String emailDestination, String emailIssue, String emailMessage, String datoAdicional) {
        this.emailOrigen = emailOrigen;
        this.password = password;
        this.fileName = fileName;
        this.emailDestination = emailDestination;
        this.emailIssue = emailIssue;
        this.emailMessage = emailMessage;
        this.file = file;
        this.datoAdicional = datoAdicional;
        this.emailsDestination = null;
        this.files = null;
        this.cadenaSqlBitacoraEnvio = null;
    }
    
    public EmailSender(String emailOrigen, String password, File file, String fileName, Address[] emailsDestination, String emailIssue, String emailMessage, String datoAdicional) {
        this.emailOrigen = emailOrigen;
        this.password = password;
        this.fileName = fileName;
        this.emailsDestination = emailsDestination;
        this.emailIssue = emailIssue;
        this.emailMessage = emailMessage;
        this.file = file;
        this.datoAdicional = datoAdicional;
        this.emailDestination = null;
        this.files = null;
        this.cadenaSqlBitacoraEnvio = null;
    }
    
    public EmailSender(
            String emailOrigen, 
            String password, 
            ArrayList<BeanFiles> files, 
            Address[] emailsDestination, 
            String emailIssue, 
            String emailMessage, 
            String datoAdicional, 
            String cadenaSqlBitacoraEnvio
    ) {
        this.emailOrigen = emailOrigen;
        this.password = password;
        this.fileName = null;
        this.emailsDestination = emailsDestination;
        this.emailIssue = emailIssue;
        this.emailMessage = emailMessage;
        this.datoAdicional = datoAdicional;
        this.file = null;
        this.emailDestination = null;
        this.files = files;
        this.cadenaSqlBitacoraEnvio = cadenaSqlBitacoraEnvio;
    }
    
    public EmailSender(String emailOrigen, String password, Address[] emailsDestination, String emailIssue, String emailMessage, String datoAdicional) {
        this.emailOrigen = emailOrigen;
        this.password = password;
        this.fileName = null;
        this.emailsDestination = emailsDestination;
        this.emailIssue = emailIssue;
        this.emailMessage = emailMessage;
        this.datoAdicional = datoAdicional;
        this.file = null;
        this.emailDestination = null;
        this.files = null;
        this.cadenaSqlBitacoraEnvio = null;
    }
    
    public EmailSender(String emailOrigen, String password, String emailDestination, String emailIssue, String emailMessage, String datoAdicional, String cadenaSqlBitacoraEnvio, File file, String fileName) {
        this.emailOrigen = emailOrigen;
        this.password = password;
        this.emailDestination = emailDestination;
        this.emailIssue = emailIssue;
        this.emailMessage = emailMessage;
        this.datoAdicional = datoAdicional;
        this.fileName = fileName;
        this.file = file;
        this.emailsDestination = null;
        this.files = null;
        this.cadenaSqlBitacoraEnvio = cadenaSqlBitacoraEnvio;
    }
    
    public EmailSender(
            String emailOrigen, 
            String password, 
            String emailDestination, 
            String emailIssue, 
            String emailMessage, 
            String datoAdicional, 
            String cadenaSqlBitacoraEnvio, 
            ArrayList<BeanFiles> files
    ) {
        this.emailOrigen = emailOrigen;
        this.password = password;
        this.emailDestination = emailDestination;
        this.emailIssue = emailIssue;
        this.emailMessage = emailMessage;
        this.datoAdicional = datoAdicional;
        this.fileName = null;
        this.file = null;
        this.emailsDestination = null;
        this.files = files;
        this.cadenaSqlBitacoraEnvio = cadenaSqlBitacoraEnvio;
    }

    public boolean isMensajeEnviado() {
        return mensajeEnviado;
    }
    
    public void ejecutar() {
        if (this.emailDestination != null && !this.emailDestination.isEmpty() && !this.emailDestination.equals("no refiere")) {
            mensajeEnviado = send();
            actualizarConfirmacionEnvio();
            System.out.println("Mensaje enviado a " + this.emailDestination);
        }
    }
    
    private void actualizarConfirmacionEnvio() {
        if (mensajeEnviado && datoAdicional != null) {
            GestionDB objConnect = null;
            try {
                objConnect = new GestionDB();
                objConnect.insertarActualizarBorrar(datoAdicional, false);
            } catch (NamingException | SQLException ex) {
                Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, datoAdicional + " - " + SeveralProcesses.getSessionUser(), ex);
            } finally {
                try {
                    if (objConnect != null)
                        objConnect.desconectar();
                } catch (SQLException ex) {
                    Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, "Cerrando conexión - " + SeveralProcesses.getSessionUser(), ex);
                }
            }
        }
        if (mensajeEnviado && cadenaSqlBitacoraEnvio != null) {
            GestionDB objConnect = null;
            try {
                objConnect = new GestionDB();
                objConnect.insertarActualizarBorrar(cadenaSqlBitacoraEnvio, false);
            } catch (NamingException | SQLException ex) {
                Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, cadenaSqlBitacoraEnvio + " - " + SeveralProcesses.getSessionUser(), ex);
            } finally {
                try {
                    if (objConnect != null)
                        objConnect.desconectar();
                } catch (SQLException ex) {
                    Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, "Cerrando conexión - " + SeveralProcesses.getSessionUser(), ex);
                }
            }
        }
    }
    
    private boolean send() {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", this.emailOrigen);
            props.setProperty("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props, null);
            BodyPart texto = new MimeBodyPart();
            texto.setText(this.emailMessage);
            
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            if (this.file != null) {
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(new FileDataSource(this.file)));
                adjunto.setFileName(this.fileName);
                multiParte.addBodyPart(adjunto);
            }
            if (this.files != null) {
//                Iterator<BeanBytesFiles> itrFiles = this.files.iterator();
//                while (itrFiles.hasNext()) {
//                    BeanBytesFiles next = itrFiles.next();
//                    try {
//                        File tempFile = File.createTempFile(next.getFileName(), ".pdf");
//                        FileOutputStream fos = new FileOutputStream(tempFile);
//                        fos.write(next.getBytesFile());
//                        BodyPart adjunto = new MimeBodyPart();
//                        adjunto.setDataHandler(new DataHandler(new FileDataSource(tempFile)));
//                        adjunto.setFileName(next.getFileName() + ".pdf");
//                        multiParte.addBodyPart(adjunto);
//                        tempFile.delete();
//                    } catch (IOException ex) {
//                        Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
                files.forEach(e -> {
                    try {
                        BodyPart adjunto = new MimeBodyPart();
                        adjunto.setDataHandler(new DataHandler(new FileDataSource(e.getFile())));
                        adjunto.setFileName(e.getFileName());
                        multiParte.addBodyPart(adjunto);
                    } catch (MessagingException ex) {
                        Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.emailOrigen));
            if (this.emailDestination != null)
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.emailDestination));
            if (this.emailsDestination != null)
                message.addRecipients(Message.RecipientType.TO, this.emailsDestination);
            message.setSubject(this.emailIssue);
            message.setContent(multiParte);
            
            Transport t = session.getTransport("smtp");
            t.connect(this.emailOrigen, this.password);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, SeveralProcesses.getSessionUser(), ex);
            return false;
        } finally {
            if (this.file != null) {
                file.delete();
            }
            if (this.files != null) {
                files.forEach(beanfile -> {
                    beanfile.getFile().delete();
                });
            }
        }
    }

    public String getEmailDestination() {
        return emailDestination;
    }
    
}
