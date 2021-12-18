
package com.unicesar.components;

import com.vaadin.ui.Panel;
import com.vaadin.ui.TwinColSelect;

public class TwinColSelectCustom extends Panel {
    
    public TwinColSelect twinColSelect;

    public TwinColSelectCustom(String caption, int rows, String[] valores) {
        setWidth("100%");
        twinColSelect = new TwinColSelect();
        twinColSelect.setWidth("100%");
        twinColSelect.setLeftColumnCaption("Disponibles");
        twinColSelect.setRightColumnCaption("Seleccionadas");
        if (caption != null)
            setCaption(caption);
            twinColSelect.setCaption(caption);
        if (rows != 0) {
            twinColSelect.setRows(rows);
        } else {
            setHeight("100%");
            twinColSelect.setHeight("100%");
        }
        if (valores != null)
            twinColSelect.addItems(valores);
        setContent(twinColSelect);
    }
    
}
