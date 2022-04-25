package at.demo.services;

import at.demo.model.Product;
import at.demo.repositories.ProductRepository;
import at.demo.ui.beans.SessionInfoBean;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Service for accessing and manipulating user data.
 *
 */
@Component
@Scope("application")
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuditLogService auditLogService;

    /**
     * Returns a collection of all products.
     *
     * @return
     */
    public Collection<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Loads a single product identified by its name.
     *
     * @param name the name to search for
     * @return the product with the given name
     */
    public Product loadProduct(String name) {
        return productRepository.findByName(name);
    }

    /**
     * Saves the product. This method will also set {@link Product#createDate} for new
     * entities or {@link Product#updateDate} for updated entities.
     *
     * @param product the product to save
     * @return the updated product
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public Product saveProduct(Product product) {
        if (product.isNew()) {
            product.setCreateDate(LocalDateTime.now());
            auditLogService.logCreateProduct(product);
            return productRepository.save(product);
        } else {
            product.setUpdateDate(LocalDateTime.now());
            auditLogService.logUpdateProduct(product);
            return productRepository.save(product);
        }
    }


    /**
     * Deletes the product.
     *
     * @param product the product to delete
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public void deleteProduct(Product product) {
        productRepository.delete(product);
        auditLogService.logDeleteProduct(product);
    }


    /**
     * Marks the product as deleted.
     *
     * @param product the product to mark as deleted
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public Product markProductAsDeleted(Product product) {
        Product toBeMarkedDeleted = productRepository.findById(product.getId());
        toBeMarkedDeleted.setUpdateDate(LocalDateTime.now());
        toBeMarkedDeleted.setDeleted(true);
        auditLogService.logDeleteProduct(toBeMarkedDeleted);
        return productRepository.save(toBeMarkedDeleted);
    }
    /**
     * Disable the product.
     *
     * @param product the product to disable
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public Product disableProduct(Product product) {
        Product productToDisable = productRepository.findByName(product.getName());
        productToDisable.setUpdateDate(LocalDateTime.now());
        productToDisable.setEnabled(false);

        auditLogService.logUpdateProduct(productToDisable);
        return productRepository.save(productToDisable);
    }

    /**
     * Enable the product.
     *
     * @param product the product to enable
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public Product enableProduct(Product product) {
        Product productToDisable = productRepository.findByName(product.getName());
        productToDisable.setUpdateDate(LocalDateTime.now());
        productToDisable.setEnabled(true);

        auditLogService.logUpdateProduct(productToDisable);
        return productRepository.save(productToDisable);
    }

    public UploadedFile loadImage(Product product) {
        String id = product.getId();
        return productRepository.findById(id).getImage();
    }
}
