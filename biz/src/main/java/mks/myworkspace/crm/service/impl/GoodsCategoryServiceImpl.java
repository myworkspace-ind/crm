package mks.myworkspace.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.GoodsCategory;
import mks.myworkspace.crm.repository.GoodsCategoryRepository;
import mks.myworkspace.crm.service.GoodsCategoryService;

@Service
@Transactional
@Slf4j
public class GoodsCategoryServiceImpl implements GoodsCategoryService{
	@Autowired
	GoodsCategoryRepository repo;

	@Override
	public GoodsCategoryRepository getRepo() {
		return repo;
	}

	@Override
	public List<GoodsCategory> findAllGoodsCategory() {
		return repo.findAll();
	}

}
