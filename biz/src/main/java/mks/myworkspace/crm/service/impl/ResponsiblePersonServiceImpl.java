package mks.myworkspace.crm.service.impl;

import mks.myworkspace.crm.entity.ResponsiblePerson;
import mks.myworkspace.crm.repository.ResponsiblePersonRepository;
import mks.myworkspace.crm.service.ResponsiblePersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponsiblePersonServiceImpl implements ResponsiblePersonService {

    private final ResponsiblePersonRepository responsiblePersonRepository;

    @Autowired
    public ResponsiblePersonServiceImpl(ResponsiblePersonRepository responsiblePersonRepository) {
        this.responsiblePersonRepository = responsiblePersonRepository;
    }

    @Override
    public List<ResponsiblePerson> getAllResponsiblePersons() {
        return responsiblePersonRepository.findAll();
    }

    @Override
    public ResponsiblePerson saveResponsiblePerson(ResponsiblePerson responsiblePerson) {
        return responsiblePersonRepository.save(responsiblePerson);
    }
}
