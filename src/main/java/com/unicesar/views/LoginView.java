package com.unicesar.views;

import com.unicesar.businesslogic.GestionDB;
import com.unicesar.utils.VariablesSesion;
import com.unicesar.utils.SeveralProcesses;
import com.unicesar.utils.Views;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.mindrot.jbcrypt.BCrypt;

public class LoginView extends VerticalLayout implements View {
    
    private TextField txtLogin;  
    private PasswordField txtPassword;
    private Button btnIngresar;
    private FormLayout layoutLogin;
    private Panel panelLogin;
    
    private String cadenaSql;
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.setWidth("100%");
        this.setHeight("100%");
        this.setMargin(true);

        setWidth("100.0%");
        setHeight("100.0%");

        setStyleName("fondoaplicacion");

        panelLogin = new Panel();
        panelLogin.setCaption("Acceso a Unicesar App");
        panelLogin.setSizeUndefined();
        panelLogin.setStyleName("panelverdeancho", true);
        panelLogin.setStyleName("bordeverde", true);

        layoutLogin = new FormLayout();
        layoutLogin.setImmediate(false);
        layoutLogin.setWidth("450px");
        layoutLogin.setHeight("100.0%");
        layoutLogin.setMargin(true);
        layoutLogin.setSpacing(true);

        txtLogin = new TextField();
        txtLogin.setRequired(true);
        txtLogin.setCaption("Usuario:");
        txtLogin.setWidth("100%");
        txtLogin.setIcon(VaadinIcons.USER);
        txtLogin.focus();
        layoutLogin.addComponent(txtLogin);

        txtPassword = new PasswordField();
        txtPassword.setRequired(true);
        txtPassword.setCaption("Contraseña:");
        txtPassword.setImmediate(false);
        txtPassword.setWidth("100%");
        txtPassword.setHeight("-1px");
        txtPassword.setIcon(VaadinIcons.KEY);
        layoutLogin.addComponent(txtPassword);

        btnIngresar = new Button("Ingresar" , VaadinIcons.CHECK);
        btnIngresar.setImmediate(true);
        btnIngresar.setWidth("100%");
        btnIngresar.setHeight("-1px");
        btnIngresar.addStyleName("primary");
        btnIngresar.setClickShortcut( KeyCode.ENTER );
        btnIngresar.addClickListener(e -> {
            if (SeveralProcesses.isComponentRequired(layoutLogin)) {
                validarLogin();
            }
        });
        layoutLogin.addComponent(btnIngresar);
        layoutLogin.setComponentAlignment(btnIngresar, new Alignment(20));

        panelLogin.setContent(layoutLogin);

        this.addComponent(panelLogin);
        this.setComponentAlignment(panelLogin, Alignment.MIDDLE_CENTER);
    }
    
    private void validarLogin() {
        cadenaSql = "SELECT "
                + "a.codigo_usuario, "
                + "a.login, "
                + "a.nombre_usuario, "
                + "a.codigo_docente, "
                + "a.codigo_estudiante, "
                + "a.password "
            + "FROM usuarios a " 
            + "WHERE BINARY a.login = ? AND a.activo = 1  "
//                + "AND a.password = md5(?) " 
            + "LIMIT 1";
        GestionDB objConnect = null;
        try {
            objConnect = new GestionDB();
            Connection conexion = objConnect.getConexion();
            try (PreparedStatement stmt = conexion.prepareStatement(cadenaSql)) {
                stmt.setString(1, txtLogin.getValue().trim());
                ResultSet rs = stmt.executeQuery();
                if ( rs.next() && BCrypt.checkpw (txtPassword.getValue().trim(), rs.getString("password"))  ) {
                    UI.getCurrent().getSession().setAttribute(VariablesSesion.CODIGO_USUARIO, rs.getString("codigo_usuario"));
                    UI.getCurrent().getSession().setAttribute(VariablesSesion.LOGIN, rs.getString("login"));
                    UI.getCurrent().getSession().setAttribute(VariablesSesion.NOMBRE_USUARIO, rs.getString("nombre_usuario"));
                    UI.getCurrent().getSession().setAttribute(VariablesSesion.CODIGO_DOCENTE, rs.getString("codigo_docente"));
                    UI.getCurrent().getSession().setAttribute(VariablesSesion.CODIGO_ESTUDIANTE, rs.getString("codigo_estudiante"));
                    UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
                } else {
                    Notification.show("Usuario y/o Contraseña Inconrectos", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, cadenaSql, ex);
        } finally {
            try {
                if (objConnect != null) {
                    objConnect.desconectar();
                }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Cerrando Conexión Login", ex);
            }
        }
    }
}
