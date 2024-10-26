package mks.myworkspace.crm.repository;

import mks.myworkspace.crm.entity.OrderCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCategoryRepository  extends JpaRepository<OrderCategory, String>{
	List<OrderCategory> findAll();
}
