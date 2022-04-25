package at.demo.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class AvatarImage implements Persistable<Integer>, Serializable {

    private static final long serialVersionUID = 1L;

    // TODO change ID to a more suitable one
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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
