package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.GoodsCategory;
@Repository
public interface GoodsCategoryRepository  extends JpaRepository<GoodsCategory, Long>{
	@Query("SELECT g FROM GoodsCategory g ORDER BY g.seqno ASC")
	List<GoodsCategory> findAllOrderBySeqno();
}
