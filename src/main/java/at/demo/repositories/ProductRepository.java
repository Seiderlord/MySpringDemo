package at.demo.repositories;

import at.demo.model.Product;

/**
 * Repository for managing {@link Product} entities.
 *
 */
public interface ProductRepository extends AbstractRepository<Product, String> {

    Product findByName(String name);

    @Override
    Product findById(String integer);

//    @Query("SELECT p.image FROM Product p WHERE p = :product")
//    ImageFile findImageFileByProductId(@Param("product") Product product);
}
