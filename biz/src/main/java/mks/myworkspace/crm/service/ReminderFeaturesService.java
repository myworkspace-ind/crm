package mks.myworkspace.crm.service;

import java.util.List;
import java.util.Optional;

import mks.myworkspace.crm.entity.ReminderFeatures;
import mks.myworkspace.crm.repository.ReminderFeaturesRepository;

public interface ReminderFeaturesService {
	ReminderFeaturesRepository getRepo();
	
	List<ReminderFeatures> findAll();
	
	Optional<ReminderFeatures> findById(Long id);

}
