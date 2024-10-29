package mks.myworkspace.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mks.myworkspace.crm.entity.GoodsCategory;

public interface GoodsCategoryRepository  extends JpaRepository<GoodsCategory, String>{
	List<GoodsCategory> findAll();
}
