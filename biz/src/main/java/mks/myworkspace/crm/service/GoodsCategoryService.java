package mks.myworkspace.crm.service;

import java.util.List;

import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.repository.GoodsCategoryRepository;

public interface GoodsCategoryService {
	GoodsCategoryRepository getRepo();

	List<GoodsCategory> findAllGoodsCategory();
}
