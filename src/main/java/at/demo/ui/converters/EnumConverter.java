package at.demo.ui.converters;

import org.springframework.util.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public abstract class EnumConverter<E extends Enum<E>> implements Converter {

    @Override
    public E getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        if (StringUtils.isEmpty(s)) {
            return null;
        }

        try {
            return E.valueOf(this.enumerationClass(), s);
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", e.getMessage()));
        }
    }

    public abstract Class<E> enumerationClass();

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {

        if (o == null) {
            return "";
        }

        try {
            Enum<?> enumeration = (Enum) o;
            return enumeration.name();
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", e.getMessage()));
        }

    }
}
