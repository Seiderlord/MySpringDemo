package at.demo.ui.converters;

import at.demo.model.AuditLogEntry;
import at.demo.model.AuditLogEvent;
import at.demo.model.Gender;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@ApplicationScope
@Component
public class AuditLogEventConverter extends EnumConverter<AuditLogEvent> {

    @Override
    public Class<AuditLogEvent> enumerationClass() {
        return AuditLogEvent.class;
    }

}
