package mks.myworkspace.crm.transformer;

import mks.myworkspace.crm.entity.Profession;

public class ProfessionMapper {
	public static String toDTO(Profession profession) {
		return profession != null ? profession.getName() : null;
	}
}
