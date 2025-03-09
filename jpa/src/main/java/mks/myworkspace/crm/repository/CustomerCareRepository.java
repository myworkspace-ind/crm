package mks.myworkspace.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import mks.myworkspace.crm.entity.CustomerCare;

public interface CustomerCareRepository extends JpaRepository<CustomerCare, Long>, JpaSpecificationExecutor<CustomerCare> {

}
