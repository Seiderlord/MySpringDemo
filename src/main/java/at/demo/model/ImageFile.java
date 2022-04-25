package at.demo.model;

import org.primefaces.model.file.UploadedFile;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class ImageFile implements Persistable<Integer>, Serializable {

    private static final long serialVersionUID = 1L;

    public ImageFile() {
    }

    public ImageFile(String name, String mimeType, byte[] data, LocalDateTime createTime) {
        this.name = name;
        this.mimeType = mimeType;
        this.data = data;
        this.createTime = createTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String mimeType;
    @Lob
    @Column(nullable = false)
    private byte[] data;

    @Column(nullable = false)
    private LocalDateTime createTime;

    public void setImage(UploadedFile file) {
        data = file.getContent();
        name = file.getFileName();
        mimeType = file.getContentType();
    }

    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String toString() {
        return "ImageFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    public String getName() {
        return name;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return (this.createTime == null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvatarImage avatarImage = (AvatarImage) o;
        return getId().equals(avatarImage.getId());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
