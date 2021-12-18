
package com.unicesar.components;

import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;

public class TextAreaCustom extends TextArea {
    
    public Panel panel;
    
    public TextAreaCustom(String caption, String value, String width, Boolean required, Boolean enabled, String styleName, int rows) {
        super(caption, value);
        SetUp(width, required, enabled, styleName, rows);
        panel = new Panel(caption, this);
        if ( rows == 10000 )
            panel.setHeight("100%");
    }
    
    public String getEscapeValue() {
        if (this.getValue() == null)
            return null;
        else
            return this.getValue().replace("'", "''").replace("\"", "\\" + "\"");
    }
    
    private void SetUp(String width, Boolean required, Boolean enabled, String styleName, int rows){
        setEnabled(enabled);
        setNullRepresentation("");
        if (width != null)
            setWidth(width);
        setRequired(required);
        if (styleName != null)
            setStyleName(styleName);
        switch (rows) {
            case 0:
                setHeightUndefined();
                break;
            case 10000:
                setHeight("100%");
                break;
            default:
                setRows(rows);
                break;
        }
    }
    
}
