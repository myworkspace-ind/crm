package mks.myworkspace.crm.repository;

import mks.myworkspace.crm.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
  @Query("SELECT a FROM Address a WHERE a.id = :id")
  Optional<Address> findByAddressId(@Param("id") Long id);

}