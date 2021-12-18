
package com.unicesar.beans;

public class DatosFilasTablas {
    
    private String property;
    private String valor;
    private String styleName;

    public DatosFilasTablas(String property, String valor, String styleName) {
        this.property = property;
        this.valor = valor;
        this.styleName = styleName;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
    
}
