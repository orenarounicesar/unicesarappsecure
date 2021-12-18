
package com.unicesar.businesslogic;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;

public class BooleanToFontIconConverter implements Converter<String, Boolean> {

    @Override
    public Boolean convertToModel(String value,
            Class<? extends Boolean> targetType, Locale locale)
            throws ConversionException {
        throw new ConversionException("Not supported");
    }

    @Override
    public String convertToPresentation(Boolean value,
            Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (Boolean.TRUE.equals(value)) {
            return FontAwesome.CHECK_SQUARE_O.getHtml();
        } else {
            return FontAwesome.SQUARE_O.getHtml();
        }
    }

    @Override
    public Class<Boolean> getModelType() {
        return Boolean.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

}
