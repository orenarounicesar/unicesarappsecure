
package com.unicesar.utils;

//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Properties;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class Settings {
    public static int MINUTOSSESION;
    public static String RUTADIRECTORIOTEMPORAL;
    public static boolean VERIFICARCIERRECONEXION;
    public static int SEGUNDOSCERRARCONEXION;
    public static String EMAILORIGEN;
    public static String EMAILPASSWORD;
    
    public Settings() {
            MINUTOSSESION = 30;
            VERIFICARCIERRECONEXION = true;
            SEGUNDOSCERRARCONEXION = 290;
            EMAILORIGEN = "orenarotest@gmail.com";
            EMAILPASSWORD = "39087327Br";
        
//        try {
//            RUTADIRECTORIOTEMPORAL = System.getProperty("jboss.server.temp.dir");
//            FileInputStream fis = new FileInputStream(System.getProperty("jboss.server.config.dir") + "/unicesarapp/configuracion.properties");
//            Properties props = new Properties();
//            props.load(fis);
//            MINUTOSSESION = Integer.valueOf(props.getProperty("MINUTOSSESION"));
//            VERIFICARCIERRECONEXION = Boolean.valueOf(props.getProperty("VERIFICARCIERRECONEXION"));
//            SEGUNDOSCERRARCONEXION = Integer.valueOf(props.getProperty("SEGUNDOSCERRARCONEXION"));
//            fis.close();
//        } catch (IOException ex) {
//            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Cargando properties - " + SeveralProcesses.getSessionUser(), ex);
//        }
    }
}
