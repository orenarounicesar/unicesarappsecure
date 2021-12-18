
package com.unicesar.utils;

import com.unicesar.businesslogic.GestionDBException;
import com.unicesar.businesslogic.Notifications;
import com.unicesar.components.ComponentClass;
import com.unicesar.components.LabelClick;
import com.unicesar.components.NumberFieldCustom;
import com.unicesar.components.TextAreaCustom;
import com.unicesar.components.TextFieldCustom;
import com.unicesar.components.TextFieldMask;
import com.unicesar.components.TwinColSelectCustom;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.vaadin.teemu.switchui.Switch;
import org.vaadin.ui.NumberField;

public class SeveralProcesses {
    
    public static boolean validateComponent(Component next) {
        for (Class componente : ComponentClass.componentes) {
            if (next.getClass() == componente) {
                return true;
            }
        }
        return false;
    }
    
    public static void confirmarSentencia (String mensaje)throws GestionDBException{
        if (!mensaje.substring(0, 4).equals("true")) {
            throw new GestionDBException(mensaje);            
        }
    }
    
    public static String getValorComponente(Component component, boolean comillas) {
        String retorno = null;
            
        if (component.getClass().equals(PopupDateField.class)) {
            PopupDateField popupDateField = (PopupDateField) component;
            if (popupDateField.getValue() != null)
                if (comillas)
                    retorno = "'" + new SimpleDateFormat("yyyy-MM-dd").format(popupDateField.getValue()) + "'";
                else
                    retorno = new SimpleDateFormat("yyyy-MM-dd").format(popupDateField.getValue());
        }
            
        if (component.getClass().equals(TextField.class) || component.getClass().equals(TextFieldCustom.class) || 
                component.getClass().equals(NumberField.class) || component.getClass().equals(NumberFieldCustom.class) || 
                component.getClass().equals(AutocompleteTextField.class)) {
            TextField textField = (TextField) component;
            if (textField.getValue() != null && !textField.getValue().trim().isEmpty())
                if (textField.getCaption() != null && textField.getCaption().equalsIgnoreCase("Email")) {
                    if (comillas)
                        retorno = "'" + getEscapedValue(textField.getValue().trim().toLowerCase()) + "'";
                    else
                        retorno = getEscapedValue(textField.getValue().trim().toLowerCase());
                } else if (textField.getCaption() != null && (textField.getCaption().equalsIgnoreCase("Primer Nombre") || 
                        textField.getCaption().equalsIgnoreCase("Segundo Nombre") || 
                        textField.getCaption().equalsIgnoreCase("Primer Apellido") || 
                        textField.getCaption().equalsIgnoreCase("Segundo Apellido"))) {
                    if (comillas)
                        retorno = "'" + getEscapedValue(textField.getValue().trim().toUpperCase()) + "'";
                    else
                        retorno = getEscapedValue(textField.getValue().trim().toUpperCase());
                } else {
                    if (comillas) {
                        retorno = "'" + getEscapedValue(textField.getValue().trim()) + "'";
                    } else {
                        retorno = getEscapedValue(textField.getValue().trim());
                    }
                }
        }
            
        if (component.getClass().equals(TextArea.class) || component.getClass().equals(TextAreaCustom.class)) {
            TextArea textArea = (TextArea) component;
            if (textArea.getValue() != null && !textArea.getValue().trim().isEmpty())
                if (comillas)
                    retorno = "'" + getEscapedValue(textArea.getValue()) + "'";
                else
                    retorno = getEscapedValue(textArea.getValue());                    
        }
            
        return retorno;
    }

    public static void setHeightTable(Table table, int cantidadMaximaFilas) {
        table.setHeightUndefined();
        if (table.size() > cantidadMaximaFilas) {
            table.setPageLength(cantidadMaximaFilas);
        } else {
            if (table.size() == 0)
                table.setPageLength(1);
            else
                table.setPageLength(table.size());
        }
    }
    
    public static void setHeightTable(Grid grid, int cantidadMaximaFilas) {
        grid.setHeightUndefined();
        if (grid.getContainerDataSource().size() > cantidadMaximaFilas) {
            grid.setHeightByRows(cantidadMaximaFilas);
        } else {
            if (grid.getContainerDataSource().size() == 0)
                grid.setHeightByRows(1);
            else
                grid.setHeightByRows(grid.getContainerDataSource().size());
        }
    }
    
    public static String getSessionUser() {
        return UI.getCurrent().getSession().getAttribute(VariablesSesion.LOGIN).toString();
    }
    
    public static Object getObjectSessionUser() {
        return UI.getCurrent().getSession().getAttribute(VariablesSesion.LOGIN);
    }
    
    public static boolean validarMail(String mail) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return pattern.matcher(mail).find() == true;
    }
    
    public static File streamToFile (InputStream inputStream, String prefix) {
        try {
            File tempFile = File.createTempFile(prefix + "_" + getSessionUser(), ".tmp");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(inputStream, out);
            }
            return tempFile;
        }   catch (IOException ex) {
            Logger.getLogger(SeveralProcesses.class.getName()).log(Level.SEVERE, "stream2file - " + getSessionUser(), ex);
        }
        return null;
    }
    
    public static StreamResource inputStreamToStreamResource(InputStream inputStream, String fileName) {
        try {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return new StreamResource(() -> new ByteArrayInputStream(bytes), fileName.replace(".", "_" + new SimpleDateFormat("kk_mm_ss").format(new Date()) + ".") );
        } catch (IOException ex) {
            Logger.getLogger(SeveralProcesses.class.getName()).log(Level.SEVERE, "inputStreamToStreamResource - " + getSessionUser(), ex);
            return null;
        }
    }
    
    public static String getEscapedValue(Object value) {
        if (value == null)
            return null;
        else
            return value.toString().replace("'", "''").replace("\"", "\\" + "\"");
    }
    
    public static String getEscapedStringValue(Object value) {
        if (value == null)
            return null;
        else
            return "'" + getEscapedValue(value) + "'";
    }
    
    public static String getEscapedStringUpperValue(Object value) {
        if (value == null)
            return null;
        else
            return "'" + getEscapedValue(value).toUpperCase() + "'";
    }
    
    public static String getEscapedStringLowerValue(Object value) {
        if (value == null)
            return null;
        else
            return "'" + getEscapedValue(value).toLowerCase() + "'";
    }
    
    
    public static double getValorRedondeado(double valor, double decimales) {
        return Math.round(valor * decimales) / decimales;
    }
    
    public static String getCaracteresReemplazados(String cadena) {
        return cadena.replaceAll("[^\\w\\s\\.]","");
    }
    
    
    public static String getValorComponenteEncuesta(Component componente) {
        String retorno;
        if (componente.getClass().equals(Switch.class)) {
            if (((Switch)componente).getValue())
                retorno = "'SI'";
            else
                retorno = "'NO'";
        } else {
            retorno = getValorComponente(componente, true);
        }
        
        return retorno;
    }
    
    public static boolean isIgual(Object valor1, Object valor2) {
        if ( valor1 == null || valor2 == null )
            return valor1 == valor2;
        else
            return valor1.equals(valor2);
    }
    
    public static String getStringDateFormat(Object fecha, String formato) {
        if ( fecha == null )
            return null;
        else
            return new SimpleDateFormat(formato).format(fecha);
    }
    
    public static Boolean isComponentRequired(ComponentContainer componentContainer) {
        
        ArrayList<ComponentContainer> listComponentContainer = new ArrayList<>();
        listComponentContainer.add(componentContainer);
        Boolean retorno = true;
//        int i = 0;
        while(!listComponentContainer.isEmpty() && retorno){
//        while(i < listComponentContainer.size() && retorno){
            Iterator<Component> componentIterator = listComponentContainer.get(0).iterator();

            while(componentIterator.hasNext() && retorno){
                Component next = componentIterator.next();
                
                if(validateComponent(next)){                    
                    retorno = valueComponentValidate(next, retorno);
                } else {
                    if (next.getClass().equals(Panel.class) || next.getClass().equals(TwinColSelectCustom.class)) {
                        Component content = ((Panel) next).getContent();
                        if(validateComponent(content)) {
                            retorno = valueComponentValidate(content, retorno);
                        } else {
                            listComponentContainer.add((ComponentContainer) content);
                        }
                    } else {
                        listComponentContainer.add((ComponentContainer) next);
                    }
                }
            }
//            i++;
            listComponentContainer.remove(0);
        }
        if (!retorno)
            Notifications.getError("FALTA INFORMACIÓN REQUERIDA");
        return retorno;
    }
    
    public static Boolean valueComponentValidate(Component next, Boolean retorno) {
                    
        if ( next.getClass().equals(TextField.class) || next.getClass().equals(TextFieldCustom.class) 
                || next.getClass().equals(NumberField.class) || next.getClass().equals(NumberFieldCustom.class)
                || next.getClass().equals(AutocompleteTextField.class) || next.getClass().equals(TextFieldMask.class) ) {
            TextField textField = (TextField) next;
            if (!textField.isValid()){
                textField.setRequiredError("El valor de " + textField.getCaption().trim() + 
                        " no puede ser vacio o nulo");
                textField.setImmediate(true);
                textField.focus();
                retorno = false;
            }
        }
        
        if(next.getClass().equals( PasswordField.class)) {
            PasswordField passwordField = (PasswordField) next;
            if(!passwordField.isValid()){
                passwordField.setRequiredError("El valor de " + passwordField.getCaption().trim() + 
                        " no puede ser vacio o nulo");
                passwordField.setImmediate(true);
                passwordField.focus();
                retorno = false;
            }
        }
        
        if(next.getClass().equals(TextArea.class) || next.getClass().equals(TextAreaCustom.class)) {
            TextArea textArea = (TextArea) next;
            if(!textArea.isValid()){
                textArea.setRequiredError("El valor de " + textArea.getCaption().trim() + 
                        " no puede ser vacio o nulo");
                textArea.setImmediate(true);
                textArea.focus();
                retorno = false;
            }
        }
        
        if(next.getClass().equals(CheckBox.class)) {
            CheckBox checkBox = (CheckBox) next;
            if(!checkBox.isValid()){
                checkBox.setRequiredError("El valor de " + checkBox.getCaption().trim() + 
                        " no puede ser vacio o nulo");
                checkBox.setImmediate(true);
                checkBox.focus();
                retorno = false;
            }
        }
        
        if(next.getClass().equals(TwinColSelect.class)) {
            TwinColSelect twinColSelect = (TwinColSelect) next;
            if(!twinColSelect.isValid()){
                twinColSelect.setRequiredError("El valor de " + twinColSelect.getCaption() + 
                        " no puede ser vacio o nulo");
                twinColSelect.setImmediate(true);
                twinColSelect.focus();
                retorno = false;
            }
        }
        
        if(next.getClass().equals(OptionGroup.class)) {
            OptionGroup optionGroup = (OptionGroup) next;
            if(!optionGroup.isValid()){
                if (optionGroup.getCaption() == null)
                    optionGroup.setRequiredError("El valor de Componente no puede ser vacio o nulo");
                else
                    optionGroup.setRequiredError("El valor de " + optionGroup.getCaption().trim() + " no puede ser vacio o nulo");
                optionGroup.setImmediate(true);
                optionGroup.focus();
                retorno = false;
            }
        }
        
        if(next.getClass().equals(PopupDateField.class)) {
            PopupDateField popupDateField = (PopupDateField) next;
            if(!popupDateField.isValid()){
                popupDateField.setRequiredError("El valor de " + popupDateField.getCaption().trim() + 
                        " no puede ser vacio o nulo");
                popupDateField.setImmediate(true);
                popupDateField.focus();
                retorno = false;
            }
        }
        
        if(next.getClass().equals(LabelClick.class)) {
            retorno =  true;
        }
        
        return retorno;
    }
    
    public static Object getCodigoDocenteEnSesion() {
        return UI.getCurrent().getSession().getAttribute(VariablesSesion.CODIGO_DOCENTE);
    }
    
    public static Object getCodigoEstudianteEnSesion() {
        return UI.getCurrent().getSession().getAttribute(VariablesSesion.CODIGO_ESTUDAINTE);
    }
    
    public static void cerrarSesion() {
        UI.getCurrent().getSession().setAttribute(VariablesSesion.LOGIN, null);
        UI.getCurrent().getSession().setAttribute(VariablesSesion.NOMBRE_USUARIO, null);
        UI.getCurrent().getSession().setAttribute(VariablesSesion.CODIGO_USUARIO, null);
        UI.getCurrent().getSession().setAttribute(VariablesSesion.CODIGO_DOCENTE, null);
        UI.getCurrent().getSession().setAttribute(VariablesSesion.CODIGO_ESTUDAINTE, null);
        UI.getCurrent().getSession().close();
        UI.getCurrent().close();
        UI.getCurrent().getNavigator().navigateTo(Views.REGISTRARNOTAS);
    }
}
