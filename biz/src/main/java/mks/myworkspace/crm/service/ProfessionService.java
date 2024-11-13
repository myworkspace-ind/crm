package mks.myworkspace.crm.service;

import mks.myworkspace.crm.entity.Profession;

import java.util.List;

public interface ProfessionService {

    List<Profession> getAllProfessions();

    Profession saveProfession(Profession profession);
}
