package mks.myworkspace.crm.service;

import mks.myworkspace.crm.entity.ResponsiblePerson;
import mks.myworkspace.crm.repository.ResponsiblePersonRepository;
import mks.myworkspace.crm.repository.StatusRepository;

import java.util.List;

public interface ResponsiblePersonService {
	ResponsiblePersonRepository getRepo();
	List<ResponsiblePerson> getAllResponsiblePersons();
	ResponsiblePerson saveResponsiblePerson(ResponsiblePerson responsiblePerson);
}
