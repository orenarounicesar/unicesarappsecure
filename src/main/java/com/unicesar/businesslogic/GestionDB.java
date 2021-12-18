package com.unicesar.businesslogic;


import com.unicesar.utils.CloseConnection;
import com.unicesar.utils.Settings;
import com.vaadin.data.util.sqlcontainer.connection.J2EEConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

public class GestionDB {
    
    private static JDBCConnectionPool connectionPool;
    private static String vJndi;
    private Connection conexion = null;
    private Statement instancia = null;
    private ResultSet datos = null;
    private String llaveTabla, nombreTabla;
    private boolean conexionCerrada;
    private final boolean verificarCierreConexion;
    private ArrayList<String> listaSentencias;
    
    public GestionDB() throws NamingException, SQLException {
        vJndi = "unicesarappjndi";
        llaveTabla = null;
        nombreTabla = null;
        this.verificarCierreConexion = Settings.VERIFICARCIERRECONEXION;
        conexionCerrada = true;
        this.conectar();
    }
    
    public Connection getConexion() {
        return conexion;
    }        
    
    public static JDBCConnectionPool getConnectionPool(){
        connectionPool = new J2EEConnectionPool(vJndi);
        return connectionPool;
    }
    
    public void conectar() throws NamingException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/unicesarappdb?useSSL=false", "orenaro", "orenaro");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GestionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
//        this.conexion = InitialContext.<DataSource>doLookup(vJndi).getConnection();
        this.instancia = (Statement) this.conexion.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY
        );
        this.conexionCerrada = false;
        if (this.verificarCierreConexion) {
            this.listaSentencias = new ArrayList<>();
            if (this.verificarCierreConexion) {
                CloseConnection closeConnection = new CloseConnection(this, Settings.SEGUNDOSCERRARCONEXION);
                closeConnection.start();
            }
        }
    }
    
    public void desconectar() throws SQLException{
        this.conexion.setAutoCommit(true);
        this.instancia.close();
        this.conexion.close();
        this.conexionCerrada = true;
        if (this.verificarCierreConexion) {
            this.listaSentencias.clear();
            this.listaSentencias = null;
        }
    }
    
//    public ResultSet consultar(String cadenaSql) throws SQLException{
//        if (this.verificarCierreConexion)
//            this.listaSentencias.add(cadenaSql);
//        datos = instancia.executeQuery(cadenaSql);
//        return datos;
//    }
    
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
    
    public String insertarActualizarBorrar(String cadenaSql, boolean returnKey) throws SQLException {
        if (this.verificarCierreConexion)
            this.listaSentencias.add(cadenaSql);
        if (nombreTabla != null)
            nombreTabla = "'" + nombreTabla + "'";
        if ( returnKey == false ) {
            instancia.execute(cadenaSql);
//            if ("slplusdbjndi".equals(vJndi)) {
//                instancia.execute("INSERT INTO bitacora (login, cadenasql, fecha, llave, tipo, nombre_tabla) VALUES (\"" 
//                + UI.getCurrent().getSession().getAttribute(VariablesSesion.CURRENT_USER).toString() + "\", \"" + cadenaSql + "\", now(), "
//                        + llaveTabla + ", '"
//                        + cadenaSql.substring(0, 6) + "', "
//                        + nombreTabla + ")");
//            }
            nombreTabla = null;
            llaveTabla = null;
            return "true";
        } else {
            instancia.execute(cadenaSql,Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = instancia.getGeneratedKeys();
            rs.next();
            String llaveGenerada = rs.getString(1);
//            if ("slplusdbjndi".equals(vJndi)) {
//                instancia.execute("INSERT INTO bitacora (login, cadenasql, fecha, llave, tipo, nombre_tabla) VALUES (\""
//                    + UI.getCurrent().getSession().getAttribute(VariablesSesion.CURRENT_USER).toString() + "\", \"" + cadenaSql + "\", now(), "
//                    + llaveGenerada + ", '"
//                    + cadenaSql.substring(0, 6) + "', "
//                    + nombreTabla + ")");
//            }
            nombreTabla = null;
            llaveTabla = null;
            return "true" + llaveGenerada;
        }
    }
    
    public String insertarActualizarBlob(ByteArrayOutputStream file, String cadenaSql, boolean returnKey) throws SQLException {
        if (this.verificarCierreConexion)
            this.listaSentencias.add(cadenaSql);
        if (returnKey==false) {
            instancia.execute(cadenaSql);
            return "true";
        }else{
            instancia.execute(cadenaSql,Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = instancia.getGeneratedKeys();
            rs.next();
            return "true"+rs.getInt(1);
        }
    }
    
    public void begin() throws SQLException {
        if (this.verificarCierreConexion)
            this.listaSentencias.add("begin");
        this.conexion.setAutoCommit(false);
        instancia.execute("begin");
    }
    
    public void commit() throws SQLException {
        if (this.verificarCierreConexion)
            this.listaSentencias.add("commit");
        instancia.execute("commit");
        this.conexion.setAutoCommit(true);
    }
    
    public void rollback() throws SQLException {
        if (this.verificarCierreConexion)
            this.listaSentencias.add("rollback");
        instancia.execute("rollback");
        this.conexion.setAutoCommit(true);
    }

    public void setLlaveTable(String llaveTable) {
        this.llaveTabla = llaveTable;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public boolean isConexionCerrada() {
        return conexionCerrada;
    }

    public ArrayList<String> getListaSentencias() {
        return listaSentencias;
    }
    
}
