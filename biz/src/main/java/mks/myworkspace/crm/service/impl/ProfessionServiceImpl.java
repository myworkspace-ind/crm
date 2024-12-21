package mks.myworkspace.crm.service.impl;

import mks.myworkspace.crm.entity.Profession;
import mks.myworkspace.crm.repository.ProfessionRepository;
import mks.myworkspace.crm.service.ProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessionServiceImpl implements ProfessionService {

    private final ProfessionRepository professionRepository;

    @Autowired
    public ProfessionServiceImpl(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    @Override
    public List<Profession> getAllProfessions() {
        return professionRepository.findAll();
    }

    @Override
    public Profession saveProfession(Profession profession) {
        return professionRepository.save(profession);
    }

	@Override
	public ProfessionRepository getRepo() {
		// TODO Auto-generated method stub
		return professionRepository;
	}
}
