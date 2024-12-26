package mks.myworkspace.crm.service;

import mks.myworkspace.crm.entity.Profession;
import mks.myworkspace.crm.repository.ProfessionRepository;
import mks.myworkspace.crm.repository.ResponsiblePersonRepository;

import java.util.List;

public interface ProfessionService {
	ProfessionRepository getRepo();

	List<Profession> getAllProfessions();

	Profession saveProfession(Profession profession);
}
