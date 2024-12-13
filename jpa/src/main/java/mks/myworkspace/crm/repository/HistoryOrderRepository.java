package mks.myworkspace.crm.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.HistoryOrder;
@Repository
public interface HistoryOrderRepository extends JpaRepository<HistoryOrder, Long>{

	

}
