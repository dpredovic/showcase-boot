package showcase.boot.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import showcase.boot.domain.Contact;
import showcase.boot.domain.ContactType;

@RepositoryRestResource(collectionResourceRel = "contacts", path = "contacts")
interface ContactRepository extends JpaRepository<Contact, Long> {

    Contact findByCustomerIdAndContactType(@Param("id") long id, @Param("type") ContactType type);

}
