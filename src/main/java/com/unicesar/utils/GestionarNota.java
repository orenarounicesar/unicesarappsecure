/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicesar.utils;

import com.unicesar.businesslogic.GestionDB;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author orenaro
 */
public class GestionarNota extends Thread {

    private final String cadenaSql;
    
    public GestionarNota(String cadenaSql) {
        this.cadenaSql = cadenaSql;
    }
    
    @Override
    public void run() {
        GestionDB objConnect = null;
        try {
            objConnect = new GestionDB();
            objConnect.insertarActualizarBorrar(cadenaSql, false);
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, cadenaSql + " - " + SeveralProcesses.getSessionUser(), ex);
        } finally {
            try {
                if (objConnect != null) {
                    objConnect.desconectar();
                }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Cerrando Conexión - " + SeveralProcesses.getSessionUser(), ex);
            }
        }
    }
}
