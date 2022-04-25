package at.demo.ui.converters;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateConverter implements Converter {

    private static final String pattern = "dd.MM.yyyy";


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return LocalDate.parse(s, DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) {
            return Strings.EMPTY;
        }

        try {
            LocalDate localDate = (LocalDate) o;
            return localDate.format(DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }


}
