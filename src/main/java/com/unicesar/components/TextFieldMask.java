
package com.unicesar.components;

import com.vaadin.ui.TextField;
import org.vaadin.inputmask.InputMask;
import org.vaadin.inputmask.client.Alias;

public class TextFieldMask extends TextField {
    
    private InputMask inputMask;
    private final String tipo;
    
    public TextFieldMask(String tipo, String caption, String valor, String max, String min, String width, Boolean requerido, Boolean habilitado, String styleName) {
        this.tipo = tipo;
        setCaption(caption);
        setValor(valor);
        
        switch (this.tipo) {
            case "MONEDA":
                inputMask = new InputMask(Alias.CURRENCY);
                inputMask.setPrefix("$ ");
                inputMask.setGroupSeparator(".");
                inputMask.setRadixPoint(",");
                if ( max != null )
                    inputMask.setMax(max);
                if ( min != null )
                    inputMask.setMin(min);
                inputMask.extend(this);
                break;
            case "ENTERO":
                inputMask = new InputMask(Alias.INTEGER);
                inputMask.setNumericInput(true);
                if ( max != null )
                    inputMask.setMax(max);
                if ( min != null )
                    inputMask.setMin(min);
                inputMask.extend(this);
                break;
            case "DECIMAL":
                inputMask = new InputMask(Alias.DECIMAL);
                inputMask.setNumericInput(true);
                if ( max != null )
                    inputMask.setMax(max);
                if ( min != null )
                    inputMask.setMin(min);
                inputMask.extend(this);
                break;
            default:
                break;
        }
        
        if ( width != null )
            setWidth(width);
        
        setRequired(requerido);
        
        setEnabled(habilitado);
        
        if ( styleName != null )
            setStyleName(styleName);
        
        addFocusListener(e -> {
            super.selectAll();
        });
        
    }
    
    @Override
    public String getValue() {
        if ( tipo.equals("MONEDA") || tipo.equals("ENTERO") || tipo.equals("DECIMAL") )
            return super.getValue()
                .replace("$", "")
                .replace(" ", "")
                .replace(".", "")
                .replace(",", ".")
                ;
        else
            return super.getValue();
    }
    
    public void setValor(String value) {
        if ( value != null && (tipo.equals("MONEDA") || tipo.equals("ENTERO") || tipo.equals("DECIMAL")) )
            super.setValue(value.replace(".", ","));
        else
            super.setValue(value);
    }
}
