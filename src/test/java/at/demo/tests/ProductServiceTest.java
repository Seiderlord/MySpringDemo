package at.demo.tests;

import at.demo.model.Product;
import at.demo.model.ProductType;
import at.demo.services.ProductService;
import at.demo.services.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Some very basic tests for {@link ProductService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ProductServiceTest {

    @Autowired
    ProductService productService;


    @DirtiesContext
    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    public void testDeleteProduct() {
        Product toBeDeletedProduct = productService.loadProduct("blue coffin");
        Assert.assertNotNull("Product could not be loaded from test data source", toBeDeletedProduct);
        int numberOfProducts = productService.getAllProducts().size();
        productService.deleteProduct(toBeDeletedProduct);

        Assert.assertEquals("No product has been deleted after calling Product.deleteProduct", numberOfProducts - 1, productService.getAllProducts().size());


        for (Product remainingProduct : productService.getAllProducts()) {
            Assert.assertNotEquals("Deleted product could still be loaded from test data source via ProductService.getAllProducts", toBeDeletedProduct.getName(),remainingProduct.getName());
        }
        Product deletedProduct = productService.loadProduct("blue coffin");
        Assert.assertNull("Deleted product could still be loaded from test data source via ProductService.loadProduct", deletedProduct);

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    public void testUpdateProduct() {

        Product toBeUpdatedProduct = productService.loadProduct("green coffin");
        Assert.assertNotNull("Product \"green coffin\" could not be loaded from test data source", toBeUpdatedProduct);

        Assert.assertNull("Product \"green coffin\" has a updateDate defined", toBeUpdatedProduct.getUpdateDate());
        toBeUpdatedProduct.setDescription("It’s a green coffin");
        productService.saveProduct(toBeUpdatedProduct);

        Product freshlyLoadedProduct = productService.loadProduct("green coffin");

        Assert.assertNotNull("Product could not be loaded from test data source after being saved", freshlyLoadedProduct);
        Assert.assertNotNull("Product \"green coffin\" does not have a updateDate defined after being saved", freshlyLoadedProduct.getUpdateDate());
        Assert.assertEquals("Product \"green coffin\" does not have a the correct description attribute stored being saved", "It’s a green coffin", freshlyLoadedProduct.getDescription());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    public void testCreateProduct() {

        Product toBeCreatedProduct = new Product();



        String name = "newCoffin";
        String producer = "producerName";
        Double price = 10.10;
        String description = "It's a product";
        toBeCreatedProduct.setName(name);
        toBeCreatedProduct.setProducer(producer);
        toBeCreatedProduct.setDescription(description);
        toBeCreatedProduct.setPrice(price);
        toBeCreatedProduct.setEnabled(true);
        toBeCreatedProduct.setType(ProductType.COFFIN);

        productService.saveProduct(toBeCreatedProduct);

        Product freshlyCreatedProduct = productService.loadProduct(name);
        Assert.assertNotNull("New product could not be loaded from test data source after being saved", freshlyCreatedProduct);
        Assert.assertEquals("Product \""+name+"\" does not have a the correct name attribute stored being saved", name, freshlyCreatedProduct.getName());
        Assert.assertEquals("Product \""+name+"\" does not have a the correct producer attribute stored being saved", producer, freshlyCreatedProduct.getProducer());
        Assert.assertEquals("Product \""+name+"\" does not have a the correct description attribute stored being saved", description, freshlyCreatedProduct.getDescription());
        Assert.assertEquals("Product \""+name+"\" does not have a the correct price attribute stored being saved", price, freshlyCreatedProduct.getPrice());
        Assert.assertTrue("Product \""+name+"\" does not have a the correct enabled attribute stored being saved",freshlyCreatedProduct.isEnabled());
        Assert.assertNotNull("Product \""+name+"\" does not have a createDate defined after being saved", freshlyCreatedProduct.getCreateDate());
        Assert.assertEquals("Product \""+name+"\" does not have the correct product type defined after being saved", ProductType.COFFIN, freshlyCreatedProduct.getType());

    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    public void testMarkProductAsDeleted() {
        Product toBeMarkedDeletedProduct = productService.loadProduct("green coffin");
        Assert.assertNotNull("Product \"green coffin\" could not be loaded from test data source", toBeMarkedDeletedProduct);

        int numberOfProducts = productService.getAllProducts().size();

        productService.markProductAsDeleted(toBeMarkedDeletedProduct);

        Assert.assertEquals("Product has been deleted after calling ProductService.markProductAsDeleted", numberOfProducts, productService.getAllProducts().size());
        Product deletedProduct = productService.loadProduct("green coffin");
        Assert.assertNotNull("Marked product could not be loaded from test data source via ProductService.loadProduct", deletedProduct);
        Assert.assertTrue("The deleted flag of the marked product is not true after calling ProductService.markProductAsDeleted", deletedProduct.isDeleted());

    }
    @DirtiesContext
    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    public void testDisableAndEnableProduct() {
        Product toBeDisabledProduct = productService.loadProduct("green coffin");
        Assert.assertNotNull("Product \"green coffin\" could not be loaded from test data source", toBeDisabledProduct);
        int numberOfProducts = productService.getAllProducts().size();

        productService.disableProduct(toBeDisabledProduct);

        Assert.assertEquals("Number of products changed after calling ProductService.disableProduct", numberOfProducts, productService.getAllProducts().size());
        Product disabledProduct = productService.loadProduct("green coffin");
        Assert.assertNotNull("Marked product could not be loaded from test data source via ProductService.loadProduct", disabledProduct);
        Assert.assertNotNull(disabledProduct.getUpdateDate());
        Assert.assertFalse("The enabled flag of the marked product is not false after calling ProductService.disableProduct", disabledProduct.isEnabled());

        productService.enableProduct(disabledProduct);

        Assert.assertEquals("Number of products changed after calling ProductService.disableProduct", numberOfProducts, productService.getAllProducts().size());
        Product enabledProduct = productService.loadProduct("green coffin");
        Assert.assertNotNull("Marked product could not be loaded from test data source via ProductService.loadProduct", enabledProduct);
        Assert.assertNotNull(enabledProduct.getUpdateDate());
        Assert.assertTrue("The enabled flag of the marked product is not true after calling ProductService.disableProduct", enabledProduct.isEnabled());

    }


    @Test
    public void testUnauthenticatedLoadProducts() {
        for (Product product : productService.getAllProducts()) {
            Assert.assertNotNull("Loaded product should not be null",product);
        }
    }

    @Test
    public void testUnauthenticatedLoadProduct() {
        Product product = productService.loadProduct("green coffin");
        Assert.assertNotNull("Loaded product should not be null",product);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedSaveProduct() {
        Product product = productService.loadProduct("green coffin");
        Assert.assertNotNull("Product \"green coffin\" could not be loaded from test data source", product);
        productService.saveProduct(product);
        Assert.fail("Call to ProductService.saveProduct should not work without proper authorization");
    }
    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedDeleteProduct() {
        Product product = productService.loadProduct("green coffin");
        Assert.assertNotNull("Product \"green coffin\" could not be loaded from test data source", product);
        productService.deleteProduct(product);
        Assert.fail("Call to ProductService.deleteProduct should not work without proper authorization");
    }
}
