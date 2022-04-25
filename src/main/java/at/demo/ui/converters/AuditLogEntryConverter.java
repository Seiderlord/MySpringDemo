package at.demo.ui.converters;

import at.demo.model.AuditLogEntry;
import at.demo.model.User;
import at.demo.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

@ApplicationScope
@Component
public class AuditLogEntryConverter implements Converter {

    @Autowired
    private AuditLogService auditLogService;

    @Override
    public AuditLogEntry getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        if (StringUtils.isEmpty(s)) {
            return null;
        }

        try {
            return this.auditLogService.loadAuditLogEntry(s);
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {

        if (o == null) {
            return "";
        }

        try {
            return ((AuditLogEntry) o).getId().toString();
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }
}