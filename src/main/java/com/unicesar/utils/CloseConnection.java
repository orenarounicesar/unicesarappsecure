
package com.unicesar.utils;

import com.unicesar.businesslogic.GestionDB;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CloseConnection extends Thread {
    private final GestionDB objConnect;
    private final int SEGUNDOS;
    public int contador;
    
    public CloseConnection(GestionDB objConnect, int segundos) {
        this.objConnect = objConnect;
        this.SEGUNDOS = segundos;
        contador = 1;
    }
    
    @Override
    public void run() {
        while (!objConnect.isConexionCerrada() && contador <= SEGUNDOS) {
            try {
                Thread.sleep(1000);
                contador++;
            } catch (InterruptedException ex) {
                Logger.getLogger(CloseConnection.class.getName()).log(Level.SEVERE, SeveralProcesses.getSessionUser(), ex);
            }
        }
        try {
            if (!objConnect.isConexionCerrada()) {
                objConnect.getListaSentencias().forEach((setencia) -> {
                    Logger.getLogger(CloseConnection.class.getName()).log(Level.SEVERE, "Conexión no Cerrada: " + setencia + " - " + SeveralProcesses.getSessionUser());
                    System.out.println("Conexión no Cerrada: " + setencia);
                });
                objConnect.desconectar();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CloseConnection.class.getName()).log(Level.SEVERE, "Cerrando Conexión - " + SeveralProcesses.getSessionUser(), ex);
        }
    }
}
