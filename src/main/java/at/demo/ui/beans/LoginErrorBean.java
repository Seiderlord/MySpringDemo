package at.demo.ui.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Bean for handling errors on Login-Page
 */
@Component
@Scope("request")
public class LoginErrorBean {

    private FacesContext facesContext;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @PostConstruct
    public void init() {

        if (facesContext == null) {
            facesContext = FacesContext.getCurrentInstance();
        }

    }

    /**
     * Gets a message according to the error which occurred on the login page.
     */
    public void getMessage() {
        if (type != null && type.equalsIgnoreCase("pw")) {
            facesContext.addMessage("errorHandler", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username and/or Password is wrong"));
        }
    }
}
