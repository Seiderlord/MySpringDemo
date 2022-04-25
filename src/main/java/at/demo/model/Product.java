package at.demo.model;


import at.demo.utils.ByteArrayUploadedFile;
import org.hibernate.annotations.GenericGenerator;
import org.primefaces.model.file.UploadedFile;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing products.
 *
 * The product entity contains information of a product.
 */

@Entity
public class Product implements Persistable<String>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "product-generator")
    @GenericGenerator(name = "product-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = "PRO"),
            strategy = "at.demo.utils.CustomIdentifierGenerator")
    private String id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private Double price;
    private String description;
    private String producer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_file_id", referencedColumnName = "id")
    private ImageFile image;

    @Column(nullable = false)
    private boolean enabled;
    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime createDate;
    @Column(nullable = true)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime updateDate;

    public UploadedFile getImage() {
        if(image == null)
            return null;
        return new ByteArrayUploadedFile(image.getData(),image.getName(),image.getMimeType());
    }

    public void setImage(UploadedFile image) {
        this.image = new ImageFile(image.getFileName(),image.getContentType(),image.getContent(),LocalDateTime.now());
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public ProductType getType() {
        return type;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Product)) {
            return false;
        }
        final Product other = (Product) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", producer='" + producer + '\'' +
                ", type=" + type +
                ", image=" + ((image!=null)?"exist":"none") +
                ", enabled=" + enabled +
                ", deleted=" + deleted +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

    @Override
    public String getId() {
        return this.id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean available) {
        this.enabled = available;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setId(String id) {
        this.id = id;
    }
}