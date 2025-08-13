package startup.spring_auth.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import startup.spring_auth.application.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
