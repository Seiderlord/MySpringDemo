package at.demo.services;

import at.demo.model.*;
import at.demo.repositories.AuditLogRepository;
import at.demo.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Service for accessing and manipulating audit log entry data.
 *
 */
@Lazy
@Service
@Scope("application")
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    /**
     * Returns a collection of all audit log entries.
     *
     * @return all loaded audit log entries
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<AuditLogEntry> getAllAuditLogEntries() {
        return auditLogRepository.findAll();
    }
    /**
     * Returns a single audit log entry with given id.
     *
     * @param id the id to look for
     * @return audit log entry
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public AuditLogEntry loadAuditLogEntry(String id) {
        return auditLogRepository.findById(id);
    }
    /**
     * Saves the audit log entry. This method will also set {@link AuditLogEntry#time} for new
     * entities. The user
     * performing this operation will also be stored as {@link AuditLogEntry#actor}.
     *
     * @param event the event of the audit log entry to save
     * @param targetId the target id of the audit log entry to save
     * @return the saved audit log entry
     */
    @PreAuthorize("isAuthenticated()")
    private AuditLogEntry logEntry(AuditLogEvent event, String targetId) {
        AuditLogEntry auditLogEntry = new AuditLogEntry();
        auditLogEntry.setAuditLogEvent(event);
        auditLogEntry.setActor(sessionInfoBean.getCurrentUser());
        auditLogEntry.setTime(LocalDateTime.now());
        auditLogEntry.setTargetId(targetId);
        return auditLogRepository.save(auditLogEntry);
    }
    /**
     * Saves the audit log entry. This method will also set {@link AuditLogEntry#time} for new
     * entities. The user
     * performing this operation will also be stored as {@link AuditLogEntry#actor}.
     *
     * @param actor the user performing this operation
     * @param event the event of the audit log entry to save
     * @param targetId the target id of the audit log entry to save
     * @return the saved audit log entry
     */
    private AuditLogEntry logEntry(User actor, AuditLogEvent event, String targetId) {
        AuditLogEntry auditLogEntry = new AuditLogEntry();
        auditLogEntry.setAuditLogEvent(event);
        auditLogEntry.setActor(actor);
        auditLogEntry.setTime(LocalDateTime.now());
        auditLogEntry.setTargetId(targetId);
        return auditLogRepository.save(auditLogEntry);
    }
    // Logging methods for user
    public AuditLogEntry logUserLogIn(){
        return logEntry(AuditLogEvent.USER_LOGIN,null);
    }
    public AuditLogEntry logUserLogOut(){
        return logEntry(AuditLogEvent.USER_LOGOUT,null);
    }
    public AuditLogEntry logUserLogIn(User actor){
        return logEntry(actor, AuditLogEvent.USER_LOGIN,null);
    }
    public AuditLogEntry logUserLogOut(User actor){
        return logEntry(actor, AuditLogEvent.USER_LOGOUT,null);
    }
    public AuditLogEntry logCreateUser(User user){
        return logEntry(AuditLogEvent.CREATE_USER,user.getId());
    }
    public AuditLogEntry logUpdateUser(User user){
        return logEntry(AuditLogEvent.UPDATE_USER,user.getId());
    }
    public AuditLogEntry logDeleteUser(User user){
        return logEntry(AuditLogEvent.DELETE_USER,user.getId());
    }

    // Logging methods for products
    public AuditLogEntry logCreateProduct(Product product){
        return logEntry(AuditLogEvent.CREATE_PRODUCT,product.getId());
    }
    public AuditLogEntry logUpdateProduct(Product product){
        return logEntry(AuditLogEvent.UPDATE_PRODUCT,product.getId());
    }
    public AuditLogEntry logDeleteProduct(Product product){
        return logEntry(AuditLogEvent.DELETE_PRODUCT,product.getId());
    }
}
