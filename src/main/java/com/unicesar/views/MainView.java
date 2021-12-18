/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicesar.views;

import com.unicesar.components.LabelClick;
import com.unicesar.utils.SeveralProcesses;
import com.unicesar.utils.VariablesSesion;
import com.unicesar.utils.Views;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author orenaro
 */
public class MainView extends VerticalSplitPanel implements View {

    private Label lblTitulo;
    private LabelClick lblSalir;
    private GridLayout layoutCabecera;
    private Button btnRegistrar;
    private Button btnConsultar;
    private HorizontalLayout layoutImages;
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        lblTitulo = new Label("Notas");
        lblTitulo.setWidthUndefined();
        lblTitulo.setStyleName("titulo");
        lblTitulo.addStyleName("textoEnormeRojo");
        lblSalir = new LabelClick(VaadinIcons.EXIT_O.getHtml() + " " + UI.getCurrent().getSession().getAttribute(VariablesSesion.NOMBRE_USUARIO) + " - Cerrar Sesión", false);
        lblSalir.setWidthUndefined();
        lblSalir.layoutLabel.addLayoutClickListener(e -> {
            SeveralProcesses.cerrarSesion();
        });
        
        layoutCabecera = new GridLayout(3, 2);
        layoutCabecera.addComponent(lblTitulo, 1, 0);
        layoutCabecera.addComponent(lblSalir.layoutLabel, 2, 0);
        layoutCabecera.setWidth("100%");
        layoutCabecera.setMargin(new MarginInfo(false, true, false, true));
        layoutCabecera.setSpacing(true);
        layoutCabecera.setComponentAlignment(lblTitulo, Alignment.MIDDLE_CENTER);
        layoutCabecera.setComponentAlignment(lblSalir.layoutLabel, Alignment.MIDDLE_RIGHT);
        
        btnRegistrar = new Button(new ThemeResource("images/registrar.png"));
        btnRegistrar.setWidth("450px");
        btnRegistrar.setHeight("450px");
        btnRegistrar.setStyleName(ValoTheme.BUTTON_LINK);
        btnRegistrar.setEnabled(SeveralProcesses.getCodigoDocenteEnSesion() != null);
        
        btnConsultar = new Button(new ThemeResource("images/consultar.jpg"));
        btnConsultar.setWidth("450px");
        btnConsultar.setHeight("450px");
        btnConsultar.setStyleName(ValoTheme.BUTTON_LINK);
        btnConsultar.setEnabled(SeveralProcesses.getCodigoEstudianteEnSesion() != null);
        
        layoutImages = new HorizontalLayout(btnRegistrar, btnConsultar);
        layoutImages.setComponentAlignment(btnRegistrar, Alignment.MIDDLE_CENTER);
        layoutImages.setComponentAlignment(btnConsultar, Alignment.MIDDLE_CENTER);
        layoutImages.setSizeFull();
        layoutImages.setMargin(true);
        layoutImages.setSpacing(true);
        
        
        addComponents(layoutCabecera, layoutImages);
        setSizeFull();
        setSplitPosition(85, Sizeable.Unit.PIXELS);
        setStyleName("fondoaplicacion");
        setLocked(true);
        
        btnRegistrar.addClickListener(e -> {
            UI.getCurrent().getNavigator().navigateTo(Views.REGISTRARNOTAS);
        });
        
        btnConsultar.addClickListener(e -> {
            UI.getCurrent().getNavigator().navigateTo(Views.CONSULTARNOTAS);
        });
        
    }
    
}
