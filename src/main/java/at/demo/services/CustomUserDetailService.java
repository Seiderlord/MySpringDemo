package at.demo.services;

import at.demo.model.User;
import at.demo.repositories.UserRepository;
import at.demo.utils.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String userName) {
            User usr = userRepository.findFirstByUsername(userName);
            if (usr == null) {
                throw new IllegalArgumentException(userName);
            }
            return new UserPrincipal(usr);
        }

}
