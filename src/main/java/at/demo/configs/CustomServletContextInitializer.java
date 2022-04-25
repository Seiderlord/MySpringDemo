package at.demo.configs;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Spring configuration for servlet context.
 *
 */
@Configuration
public class CustomServletContextInitializer implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        sc.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
        sc.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        sc.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
        sc.setInitParameter("javax.faces.CONFIG_FILES","/WEB-INF/faces-config.xml");
        sc.setInitParameter("primefaces.FLEX",Boolean.TRUE.toString());
        sc.setInitParameter("javax.faces.STATE_SAVING_METHOD","server");
        sc.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS",Boolean.TRUE.toString());
        // Add parameters for primefaces template
        sc.setInitParameter("primefaces.FONT_AWESOME", "true");
//        sc.setInitParameter("com.sun.faces.expressionFactory",
//                "com.sun.el.ExpressionFactoryImpl");
        // Add parameter for uploading files
        sc.setInitParameter("primefaces.UPLOADER",
                "commons");

    }
}
