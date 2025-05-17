package mks.myworkspace.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.ReminderFeatures;

@Repository
public interface ReminderFeaturesRepository extends JpaRepository<ReminderFeatures, Long>, JpaSpecificationExecutor<ReminderFeatures>{

}
