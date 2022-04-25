package at.demo.utils;

import at.demo.model.AvatarImage;
import at.demo.services.UserService;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;

// TODO add delete method
@Component
@Scope("application")
public class AvatarImageLoader {

    @Autowired
    private UserService service;

    public boolean profileImageExists;
    public StreamedContent avatarImage;

    public AvatarImage defaultImage;

    @PostConstruct
    private void init() {
        defaultImage = new AvatarImage();
        try {
            defaultImage.setData(IOUtils.toByteArray(new ClassPathResource("./default/defaultProfileImage.webp").getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadImage();
    }

    public void reloadImage() {
        try {
            StreamedContent loadedImage = this.loadImage();
            this.avatarImage = loadedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StreamedContent loadImage() throws IOException {
        AvatarImage avatarImage = service.loadProfileImage();
        profileImageExists = (avatarImage != null);

        if (!profileImageExists) {
            avatarImage = defaultImage;
        }
        AvatarImage finalAvatarImage = avatarImage;
        return DefaultStreamedContent.builder().contentType("jpg").name("profilePicture").stream(() -> new ByteArrayInputStream(finalAvatarImage.getData())).build();
    }

    public StreamedContent getAvatarImage() {
        return avatarImage;
    }

    public boolean isProfileImageExists() {
        return profileImageExists;
    }

}
