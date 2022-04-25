package at.demo.ui.converters;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeConverter implements Converter {

    private static final String localDateTimePattern = "dd.MM.yyyy HH:mm";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(localDateTimePattern));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) {
            return Strings.EMPTY;
        }

        try {
            LocalDateTime localDateTime = (LocalDateTime) o;
            return localDateTime.format(DateTimeFormatter.ofPattern(localDateTimePattern));
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }
}
