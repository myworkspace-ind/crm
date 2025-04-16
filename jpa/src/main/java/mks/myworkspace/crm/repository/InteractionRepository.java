package mks.myworkspace.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mks.myworkspace.crm.entity.Interaction;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
	@Query("SELECT i FROM Interaction i WHERE i.customer.id = :customerId ORDER BY i.createdAt DESC")
    Interaction findLastInteractionForCustomer(@Param("customerId") Long customerId);
}
