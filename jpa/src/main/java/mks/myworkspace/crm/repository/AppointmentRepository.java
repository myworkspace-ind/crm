package mks.myworkspace.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mks.myworkspace.crm.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

}
