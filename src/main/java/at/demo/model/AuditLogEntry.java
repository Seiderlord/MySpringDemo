package at.demo.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity representing audit logs.
 */
@Entity
public class AuditLogEntry implements Persistable<String>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "audit-generator")
    @GenericGenerator(name = "audit-generator",
            parameters = @Parameter(name = "prefix", value = "AUD"),
            strategy = "at.demo.utils.CustomIdentifierGenerator")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditLogEvent auditLogEvent;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime time;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User actor;

    private String targetId;

    @Override
    public String toString() {
        return "AuditLogEntry{" +
                "id=" + id +
                ", auditLogEvent=" + auditLogEvent +
                ", time=" + time +
                ", actor=" + actor +
                ", targetId=" + targetId +
                '}';
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return (this.time == null);
    }

    public AuditLogEvent getAuditLogEvent() {
        return auditLogEvent;
    }

    public void setAuditLogEvent(AuditLogEvent auditLogEvent) {
        this.auditLogEvent = auditLogEvent;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
