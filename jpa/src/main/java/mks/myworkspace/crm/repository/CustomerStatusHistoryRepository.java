package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Customer;
import mks.myworkspace.crm.entity.CustomerStatusHistory;

@Repository
public interface CustomerStatusHistoryRepository extends JpaRepository<CustomerStatusHistory, Long> {
    List<CustomerStatusHistory> findByCustomerOrderByChangeDateAsc(Customer customer);
}
