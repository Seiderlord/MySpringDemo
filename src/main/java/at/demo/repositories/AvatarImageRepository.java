package at.demo.repositories;

import at.demo.model.AvatarImage;
import at.demo.model.User;

/**
 * Repository for managing {@link AvatarImage} entities.
 *
 */
public interface AvatarImageRepository extends AbstractRepository<AvatarImage, Integer> {

    public AvatarImage findByUser(User user);

}
