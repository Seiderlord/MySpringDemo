package at.demo.ui.converters;

import at.demo.model.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@ApplicationScope
@Component
public class UserRoleConverter extends EnumConverter<UserRole> {

    @Override
    public Class<UserRole> enumerationClass() {
        return UserRole.class;
    }


//    @Override
//    public String convertToDatabaseColumn(UserRole userRole) {
//        if (userRole == null) {
//            return null;
//        }
//        return userRole.getName();
//    }
//
//    @Override
//    public UserRole convertToEntityAttribute(String name) {
//        if (name == null) {
//            return null;
//        }
//
//        return UserRole.of(name);
//    }
}