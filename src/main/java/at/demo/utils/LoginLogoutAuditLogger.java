package at.demo.utils;

import at.demo.model.User;
import at.demo.repositories.UserRepository;
import at.demo.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginLogoutAuditLogger {

        //TODO The current way to load the user is not optimal.

        @Autowired
        private AuditLogService auditLogService;

        @Autowired
        private UserRepository userRepository;

        /**
         * Listens for logins and logs
         * @param event a login event
         */
        @EventListener
        public void onLoginEvent(AuthenticationSuccessEvent event) {
                try {
                        String username = ((UserPrincipal) event.getAuthentication().getPrincipal()).getUsername();
                        User user = this.userRepository.findFirstByUsername(username);
                        this.auditLogService.logUserLogIn(user);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        /**
         * Listens for logout and log
         *
         * @param event the logout event
         */
        @EventListener
        public void onLogoutEvent(LogoutSuccessEvent event) {
                try {
                        String username = ((UserPrincipal) event.getAuthentication().getPrincipal()).getUsername();
                        User user = this.userRepository.findFirstByUsername(username);
                        this.auditLogService.logUserLogOut(user);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

}