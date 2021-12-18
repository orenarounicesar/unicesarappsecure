
package com.unicesar.components;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class LabelClick extends Label {
    
    public HorizontalLayout layoutLabel;

    public LabelClick(String value, boolean small) {
        this.setContentMode(ContentMode.HTML);
        this.setValue("<a>" + value + "</a>");
        layoutLabel = new HorizontalLayout(this);
        layoutLabel.setMargin(false);
        layoutLabel.setWidthUndefined();
        layoutLabel.setHeightUndefined();
        if (small) {
            this.setStyleName(ValoTheme.LABEL_SMALL);
        }
    }

}
