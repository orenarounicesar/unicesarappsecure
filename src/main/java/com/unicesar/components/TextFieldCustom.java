
package com.unicesar.components;

import com.vaadin.data.Property;
import com.vaadin.ui.TextField;

public class TextFieldCustom extends TextField {

    public TextFieldCustom(String width, Boolean required, String styleName) {
        SetUp(width, required, true, styleName);
    }

    public TextFieldCustom(String caption, String width, Boolean required, Boolean enabled) {
        super(caption);
        SetUp(width, required, enabled, null);
    }

    public TextFieldCustom(String caption, String width, Boolean required, Boolean enabled, String styleName) {
        super(caption);
        SetUp(width, required, enabled, styleName);
    }

    public TextFieldCustom(Property dataSource, String width, Boolean required, Boolean enabled, String styleName) {
        super(dataSource);
        SetUp(width, required, enabled, styleName);
    }

    public TextFieldCustom(String caption, Property dataSource, String width, Boolean enabled, Boolean required, String styleName) {
        super(caption, dataSource);
        SetUp(width, required, enabled, styleName);
    }

    public TextFieldCustom(String caption, String value, String width, Boolean required, Boolean enabled, String styleName) {
        super(caption, value);
        SetUp(width, required, enabled, styleName);
    }
    
    public String getEscapedValue() {
        if (this.getValue() == null)
            return null;
        else
            return this.getValue().replace("'", "''").replace("\"", "\\" + "\"");
    }
    
    private void SetUp(String width, Boolean required, Boolean enabled, String styleName){
        setEnabled(enabled);
        setNullRepresentation("");
        if (width != null)
            setWidth(width);
        setRequired(required);
        if (styleName != null)
            setStyleName(styleName);
    }
    
}
