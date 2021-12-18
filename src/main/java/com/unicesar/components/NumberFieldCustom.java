
package com.unicesar.components;

import org.vaadin.ui.NumberField;

public class NumberFieldCustom extends NumberField {

    public NumberFieldCustom(String caption, boolean aceptaDecimal, boolean aceptaNegativos, 
            double valorMinimo, double valorMaximo, String valorDefecto, 
            String width, Boolean requerido, Boolean habilitado, String styleName) {
        
        this.setDecimalAllowed(aceptaDecimal);                // not just integers (by default, decimals are allowed)
        if (aceptaDecimal) {
            this.setDecimalPrecision(2);                 // maximum 2 digits after the decimal separator
            this.setDecimalSeparator(',');               // e.g. 1,5
            this.setDecimalSeparatorAlwaysShown(true);   // e.g. 12345 -> 12345,
            this.setMinimumFractionDigits(2);            // e.g. 123,4 -> 123,40
        } else {
            this.setDecimalPrecision(0);                 // maximum 2 digits after the decimal separator
            this.setDecimalSeparatorAlwaysShown(false);   // e.g. 12345 -> 12345,
            this.setMinimumFractionDigits(0);            // e.g. 123,4 -> 123,40
        }
        this.setGroupingUsed(true);                  // use grouping (e.g. 12345 -> 12.345)
        this.setGroupingSeparator('.');              // use '.' as grouping separator
        this.setGroupingSize(3);                     // 3 digits between grouping separators: 12.345.678
        this.setNegativeAllowed(aceptaNegativos);              // prevent negative numbers (defaults to true)
        if (aceptaNegativos)
            this.setMinValue(valorMinimo);                         // valid values must be >= 0 ...
        else
            this.setMinValue(0.00);                         // valid values must be >= 0 ...
        this.setMaxValue(valorMaximo);                     // ... and <= 999.9
        this.setErrorText("Valor por fuera del rango aceptado"); // feedback message on bad input
//                descuento1.setValueIgnoreReadOnly("0");           // set the field's value, regardless whether it is read-only or not
//                descuento1.removeValidator();                      // omit server-side validation
        if (caption != null)
            this.setCaption(caption);
        if (valorDefecto != null)
            this.setValue(valorDefecto);
        if (width != null)
            this.setWidth(width);
        this.setRequired(requerido);
        this.setEnabled(habilitado);
        if (styleName != null)
            this.setStyleName(styleName);
        
    }
    
}
