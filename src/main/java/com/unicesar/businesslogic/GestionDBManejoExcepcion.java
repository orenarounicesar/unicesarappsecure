package com.unicesar.businesslogic;


import com.unicesar.utils.VariablesSesion;
import com.vaadin.data.util.sqlcontainer.connection.J2EEConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class GestionDBManejoExcepcion {
    
    private Connection conexion = null;
    private Statement instancia = null;
    private ResultSet datos = null;
    private static JDBCConnectionPool connectionPool;

    private static String vJndi;
    
    private String llaveTabla, nombreTabla;
    
    public GestionDBManejoExcepcion() {
        vJndi = "slplusdbjndi";
        llaveTabla = null;
        nombreTabla = null;
        this.conectar();
    }
    
    public GestionDBManejoExcepcion(String servidor) {        
        setJNDI(servidor);        
        this.conectar();
    }
    
    private static void setJNDI(String servidor) {
        switch (servidor) {
            case "Valledupar":
                vJndi = "sojndi";
                break;
            case "La Jagua":
                vJndi = "jaguajndi";
                break;
            case "Brigadas Rojo":
                vJndi = "rojojndi";
                break;
            case "Brigadas Gris":
                vJndi = "grisjndi";
                break;
            case "SYSPLUS":
                vJndi = "sysplusjndi";
                break;
            case "SYSPLUSGENERAL":
                vJndi = "sysplusgeneraljndi";
                break;
            default:
                break;
        }
    }

    public Connection getConexion() {
        return conexion;
    }        
    
    public static JDBCConnectionPool getConnectionPool(){
        connectionPool = new J2EEConnectionPool("slplusdbjndi");
        return connectionPool;
    }
    
    public static JDBCConnectionPool getConnectionPool(String servidor){        
        setJNDI(servidor);
        connectionPool = new J2EEConnectionPool(vJndi);
        return connectionPool;
    }
  
    public void conectar() {
        try {
            this.conexion = InitialContext.<DataSource>doLookup(vJndi).getConnection();
            this.instancia = (Statement) this.conexion.createStatement();
        } catch (SQLException | NullPointerException | NamingException ex) {
            Logger.getLogger(GestionDB.class.getName()).log(Level.SEVERE, null, ex);
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }
    
    public void desconectar(){
        try {
            this.conexion.setAutoCommit(true);
            this.instancia.close();
            this.conexion.close();
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(GestionDB.class.getName()).log(Level.SEVERE, "Cerrando Conexión - Usuario: " 
                    + UI.getCurrent().getSession().getAttribute(VariablesSesion.LOGIN), ex);
            Notification.show(ex.getMessage() + "\nImposible cerrar conexion ... FAIL", Notification.Type.TRAY_NOTIFICATION);
        }
    }
    
    public ResultSet consultar(String cadenaSql){            
        try {
            datos = instancia.executeQuery(cadenaSql);
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(GestionDB.class.getName()).log(Level.SEVERE, cadenaSql, ex);
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            desconectar();
        }
        
        return datos;
    }
    
    public static String clausulaWhere (String operador, String campo, String valor){
        
        String clausula="";
        if (operador.equals("igual a")) clausula = campo + " = '" + valor + "'";
        if (operador.equals("no igual a")) clausula = campo + " != '" + valor + "'";
        if (operador.equals("empiece por")) clausula = campo + " like '" + valor + "%'";
        if (operador.equals("no empiece por")) clausula = campo + " not like '" + valor + "%'";
        if (operador.equals("terminar por")) clausula = campo + " like '%" + valor + "'";
        if (operador.equals("no termina por")) clausula = campo + " not like '%" + valor + "'";
        if (operador.equals("contiene")) clausula = campo + " like '%" + valor + "%'";
        if (operador.equals("no contiene")) clausula = campo + " not like '%" + valor + "%'";
        if (operador.equals("es nulo")) clausula = campo + " is null";
        if (operador.equals("no es nulo")) clausula = "not " + campo + " is null";
        if (operador.equals("está en")) clausula = campo + " in (" + valor + ")";
        if (operador.equals("no está en")) clausula = "not " + campo + " in (" + valor + ")";
        
        return clausula;
    }
    
    public String insertarActualizarBorrar(String cadenaSql, boolean returnKey) {
        try {
            if (nombreTabla != null)
                nombreTabla = "'" + nombreTabla + "'";
            if (returnKey==false){
                instancia.execute(cadenaSql);
                if ("slplusdbjndi".equals(vJndi)) {
                    instancia.execute("INSERT INTO bitacora (login, cadenasql, fecha, llave, tipo, nombre_tabla) VALUES (\"" 
                    + UI.getCurrent().getSession().getAttribute(VariablesSesion.LOGIN).toString() + "\", \"" + cadenaSql + "\", now(), "
                            + llaveTabla + ", '"
                            + cadenaSql.substring(0, 6) + "', "
                            + nombreTabla + ")");
                }
                nombreTabla = null;
                llaveTabla = null;
                return "true";
            } else {
                instancia.execute(cadenaSql,Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = instancia.getGeneratedKeys();
                rs.next();
                String llaveGenerada = rs.getString(1);
                if ("slplusdbjndi".equals(vJndi)) {
                    instancia.execute("INSERT INTO bitacora (login, cadenasql, fecha, llave, tipo, nombre_tabla) VALUES (\""
                        + UI.getCurrent().getSession().getAttribute(VariablesSesion.LOGIN).toString() + "\", \"" + cadenaSql + "\", now(), "
                        + llaveGenerada + ", '"
                        + cadenaSql.substring(0, 6) + "', "
                        + nombreTabla + ")");
                }
                nombreTabla = null;
                llaveTabla = null;
                return "true" + llaveGenerada;
            }                        
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(GestionDB.class.getName()).log(Level.SEVERE, cadenaSql, ex);
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            desconectar();
            return ex.getMessage();
        }
    }
    
    public String insertarActualizarBorrarSinBitacora(String cadenaSql, boolean returnKey) {
        try {
            if (returnKey==false){
                instancia.execute(cadenaSql);
                return "true";
            } else {
                instancia.execute(cadenaSql,Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = instancia.getGeneratedKeys();
                rs.next();
//                String llaveGenerada = rs.getString(1);
//                return "true" + llaveGenerada;
                return "true" + rs.getString(1);
            }                        
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(GestionDB.class.getName()).log(Level.SEVERE, cadenaSql, ex);
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            desconectar();
            return ex.getMessage();
        }
    }
    
    public String insertarActualizarBlob(ByteArrayOutputStream file, String cadenaSql, boolean returnKey){
        try {
            if (returnKey==false){
                instancia.execute(cadenaSql);
                return "true";
            }else{
                instancia.execute(cadenaSql,Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = instancia.getGeneratedKeys();
                rs.next();
                return "true"+rs.getInt(1);
            }                        
        } catch (SQLException | NullPointerException ex) {
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            desconectar();
            return ex.getMessage();
        }
    }
    
    public void begin() {
        try {
            this.conexion.setAutoCommit(false);
            instancia.execute("begin");
        } catch (SQLException | NullPointerException ex) {
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            desconectar();
        }
    }
    
    public void commit() {
        try {
            instancia.execute("commit");
            this.conexion.setAutoCommit(true);
        } catch (SQLException | NullPointerException ex) {
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            desconectar();
        }
    }
    
    public void rollback() {
        try {
            instancia.execute("rollback");
            this.conexion.setAutoCommit(true);
        } catch (SQLException | NullPointerException ex) {
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            desconectar();
        }
    }

    public void setLlaveTable(String llaveTable) {
        this.llaveTabla = llaveTable;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }
    
}
