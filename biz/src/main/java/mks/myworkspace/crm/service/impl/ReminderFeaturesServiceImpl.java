package mks.myworkspace.crm.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.crm.entity.ReminderFeatures;
import mks.myworkspace.crm.repository.ReminderFeaturesRepository;
import mks.myworkspace.crm.service.ReminderFeaturesService;

@Service
@Transactional
@Slf4j
public class ReminderFeaturesServiceImpl implements ReminderFeaturesService {
	@Autowired
	ReminderFeaturesRepository repo;
	
	@Override
	public ReminderFeaturesRepository getRepo() {
		return repo;
	}

	@Override
	public List<ReminderFeatures> findAll() {
		return repo.findAll();
	}

	@Override
	public Optional<ReminderFeatures> findById(Long id) {
		return repo.findById(id);
	}

}
