package at.demo.ui.controllers;

import at.demo.model.Product;
import at.demo.services.ProductService;
import at.demo.utils.AvatarImageLoader;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the product list view.
 */

@Component
@Scope("view")
public class ProductController implements Serializable {

    @Autowired
    private ProductService productService;

    // Property for listing all products
    private List<Product> products;

    // Property for deleting multiple products simultaneously
    private List<Product> selectedProducts;
    // Property for detail view, creation and manipulation
    private Product selectedProduct;
    // Property for detail view, creation and manipulation of the product's image
    private UploadedFile uploadFile;

    // Property flag for permanently deletion
    private boolean permanentDeletion;
    // Property flag for showing deleted marked products
    private boolean showDeletedProducts;


    @PostConstruct
    public void init() {
        doReload();
        permanentDeletion = false;
        showDeletedProducts=false;
    }

    public void doReload(){
        this.products = new ArrayList<>(this.productService.getAllProducts());
        if(!showDeletedProducts){
            this.products = this.products.stream().filter(product -> !product.isDeleted()).collect(Collectors.toList());
        }
    }

    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public boolean isShowDeletedProducts() {
        return showDeletedProducts;
    }

    public void setShowDeletedProducts(boolean showDeletedProducts) {
        this.showDeletedProducts = showDeletedProducts;
        doReload();
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
        this.uploadFile = selectedProduct.getImage();
    }

    public boolean hasSelectedProducts() {
        return this.selectedProducts != null && !this.selectedProducts.isEmpty();
    }


    public void openNew() {
        this.selectedProduct = new Product();
        this.selectedProduct.setEnabled(true);
        this.selectedProduct.setDeleted(false);
        this.uploadFile = null;
    }

    public void saveProduct() {

        if(this.uploadFile != null) {
            this.selectedProduct.setImage(this.uploadFile);
            if (this.selectedProduct.getId() == null) {
                this.selectedProduct = productService.saveProduct(selectedProduct);
                this.products.add(selectedProduct);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Added"));
            } else {
                this.selectedProduct = productService.saveProduct(selectedProduct);
                int index = this.products.indexOf(this.selectedProduct);
                this.products.set(index, selectedProduct);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Updated"));
            }

            PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
            PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
        }else {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"No image uploaded!",""));
        }
    }

    public void deleteProduct() {
        if(permanentDeletion) {
            productService.deleteProduct(selectedProduct);
            this.permanentDeletion = false;
            this.products.remove(this.selectedProduct);
            this.selectedProduct = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Removed"));
        }else {
            productService.markProductAsDeleted(selectedProduct);
            if(showDeletedProducts) {
                int index = this.products.indexOf(this.selectedProduct);
                this.products.set(index, selectedProduct);
            }else {
                this.products.remove(selectedProduct);
            }
            this.selectedProduct = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Set As Deleted"));
        }
        PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedProducts()) {
            int size = this.selectedProducts.size();
            return size > 1 ? size + " products selected" : "1 product selected";
        }

        return "Delete";
    }


    public void deleteSelectedProducts() {
        for(Product product : this.selectedProducts)
            productService.deleteProduct(product);
        this.products.removeAll(this.selectedProducts);
        this.selectedProducts = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Products Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
        PrimeFaces.current().executeScript("PF('dtProducts').clearFilters()");
    }


    public void handleFileUpload(FileUploadEvent event) {
        if (event.getFile() != null) {
            this.uploadFile = event.getFile();
            FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    public String getConfirmDeleteButtonMessage() {
        return "Yes"+(this.permanentDeletion?" (permanently)":"");
    }
    public boolean isPermanentDeletion() {
        return permanentDeletion;
    }

    public void setPermanentDeletion(boolean permanentDeletion) {
        this.permanentDeletion = permanentDeletion;
    }

    public UploadedFile getUploadFile() {
        return uploadFile;
    }

}