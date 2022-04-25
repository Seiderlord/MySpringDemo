package at.demo.repositories;

import at.demo.model.ImageFile;

/**
 * Repository for managing {@link ImageFile} entities.
 *
 */
public interface ImageFileRepository extends AbstractRepository<ImageFile, String> {

    @Override
    ImageFile findById(String id);
}
