/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicesar.views;

import com.unicesar.businesslogic.GestionDB;
import com.unicesar.components.LabelClick;
import com.unicesar.components.TableWithFilterSplit;
import com.unicesar.utils.SeveralProcesses;
import com.unicesar.utils.Views;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author orenaro
 */
public class ConsultarNotas extends VerticalSplitPanel implements View {

    private Label lblTitulo;
    private Label lblNombreDocente;
    private LabelClick lblAtras;
    private GridLayout layoutCabecera;
    private TableWithFilterSplit tblAsignaturas;
    
    private String cadenaSql;
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        lblTitulo = new Label("Consulta de Notas");
        lblTitulo.setWidthUndefined();
        lblTitulo.setStyleName("titulo");
        lblTitulo.addStyleName("textoEnormeRojo");
        lblAtras = new LabelClick(VaadinIcons.ARROW_LEFT.getHtml() + " Atras", false);
        lblAtras.setWidthUndefined();
        lblAtras.layoutLabel.addLayoutClickListener(e -> {
            UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        });
        lblNombreDocente = new Label("Estudiante: <strong>" + getNombreEstudiante(SeveralProcesses.getCodigoEstudianteEnSesion()) + "</strong>", ContentMode.HTML);
        lblNombreDocente.setWidthUndefined();
        
        layoutCabecera = new GridLayout(3, 1);
        layoutCabecera.addComponent(lblNombreDocente, 0, 0);
        layoutCabecera.addComponent(lblTitulo, 1, 0);
        layoutCabecera.addComponent(lblAtras.layoutLabel, 2, 0);
        layoutCabecera.setWidth("100%");
        layoutCabecera.setMargin(new MarginInfo(false, true, false, true));
        layoutCabecera.setSpacing(true);
        layoutCabecera.setComponentAlignment(lblTitulo, Alignment.MIDDLE_CENTER);
        layoutCabecera.setComponentAlignment(lblAtras.layoutLabel, Alignment.MIDDLE_RIGHT);
        layoutCabecera.setComponentAlignment(lblNombreDocente, Alignment.MIDDLE_LEFT);
        
        tblAsignaturas = new TableWithFilterSplit("corte,asignatura", "Listado de Asignaturas", true);
        tblAsignaturas.addContainerProperty("corte", Object.class, null, "Corte", null, Table.Align.CENTER);
        tblAsignaturas.addContainerProperty("codigo", Object.class, null, "Codigo", null, Table.Align.CENTER);
        tblAsignaturas.addContainerProperty("asignatura", Object.class, null, "Asignatura", null, Table.Align.CENTER);
        tblAsignaturas.addContainerProperty("nota", Object.class, null, "Nota", null, Table.Align.CENTER);
        tblAsignaturas.setSizeFull();
        tblAsignaturas.setStyleName("tablaasignaturas");
        tblAsignaturas.layoutContent.setSizeFull();
        tblAsignaturas.panel.setSizeFull();
        tblAsignaturas.panel.setStyleName("panelverde");
        tblAsignaturas.panel.addStyleName("bordeverde");
        
        
        addComponents(layoutCabecera, tblAsignaturas.panel);
        setSizeFull();
        setSplitPosition(40, Sizeable.Unit.PIXELS);
        setStyleName("fondoaplicacion");
        setLocked(true);
        
        cargarTblAsignaturas();
    }
    
    private String getNombreEstudiante(Object codigoDocente) {
        cadenaSql = "SELECT "
                + "CONCAT_WS(' ',a.nombre1, a.nombre2, a.apellido1, a.apellido2) AS nombre_docente "
            + "FROM datos_personales a "
            + "INNER JOIN docentes b ON b.codigo_dato_personal = a.codigo_dato_personal AND b.codigo_docente = ?"
            ;
        GestionDB objConnect = null;
        try {
            objConnect = new GestionDB();
            Connection conexion = objConnect.getConexion();
            try (PreparedStatement stmt = conexion.prepareStatement(cadenaSql)) {
                stmt.setInt(1, Integer.valueOf(codigoDocente.toString()));
                ResultSet rs = stmt.executeQuery();
                if ( rs.next() ) {
                    return rs.getString(1);
                }
            }
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
        return null;
    }
    
    
    
    private void cargarTblAsignaturas() {
        cadenaSql = "SELECT "
                + "c.codigo_estudiante_asignatura, e.nombre_corte, a.codigo_asignatura, a.nombre_asignatura, d.nota "
            + "FROM asignaturas a "
            + "INNER JOIN docentes_asignaturas b ON b.codigo_asignatura = a.codigo_asignatura "
            + "INNER JOIN estudiantes_asignaturas c ON c.codigo_asignatura = a.codigo_asignatura AND c.codigo_estudiante = ? "
            + "INNER JOIN notas d ON d.codigo_estudiante_asignatura = c.codigo_estudiante_asignatura AND d.publicada = 1 "
            + "INNER JOIN cortes e ON e.codigo_corte = d.codigo_corte";
        GestionDB objConnect = null;
        try {
            objConnect = new GestionDB();
            Connection conexion = objConnect.getConexion();
            try (PreparedStatement stmt = conexion.prepareStatement(cadenaSql)) {
                stmt.setInt(1, Integer.valueOf(SeveralProcesses.getCodigoEstudianteEnSesion().toString()));
                ResultSet rs = stmt.executeQuery();
                while ( rs.next() ) {
                    tblAsignaturas.addItem(
                            new Object[]{
                                rs.getString("nombre_corte"),
                                rs.getInt("codigo_asignatura"),
                                rs.getString("nombre_asignatura"),
                                rs.getString("nota")
                            }, 
                            rs.getInt("codigo_estudiante_asignatura")
                    );
                }
            }
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
