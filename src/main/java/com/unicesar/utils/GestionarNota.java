/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicesar.utils;

import com.unicesar.businesslogic.GestionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private final int codigoEstudianteAsignatura;
    private final int codigoCorte;
    private final float nota;
    
    public GestionarNota(String cadenaSql, int codigoEstudianteAsignatura, int codigoCorte, float nota) {
        this.cadenaSql = cadenaSql;
        this.codigoEstudianteAsignatura = codigoEstudianteAsignatura;
        this.codigoCorte = codigoCorte;
        this.nota = nota;
    }
    
    @Override
    public void run() {
        GestionDB objConnect = null;
        try {
            objConnect = new GestionDB();
            Connection conexion = objConnect.getConexion();
            PreparedStatement stmt = conexion.prepareStatement(cadenaSql);
            stmt.setInt(1, codigoEstudianteAsignatura);
            stmt.setInt(2, codigoCorte);
            stmt.setFloat(3, nota);
            stmt.setFloat(4, nota);
            stmt.execute();
            stmt.close();
//            objConnect.insertarActualizarBorrar(cadenaSql, false);
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
