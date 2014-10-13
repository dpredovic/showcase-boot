package showcase.boot.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import showcase.boot.domain.Customer;

@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
interface CustomerRepository extends JpaRepository<Customer, Long> {

}
