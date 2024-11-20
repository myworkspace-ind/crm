package mks.myworkspace.crm.service;

import mks.myworkspace.crm.entity.ResponsiblePerson;

import java.util.List;

public interface ResponsiblePersonService {

    List<ResponsiblePerson> getAllResponsiblePersons();

    ResponsiblePerson saveResponsiblePerson(ResponsiblePerson responsiblePerson);
}
