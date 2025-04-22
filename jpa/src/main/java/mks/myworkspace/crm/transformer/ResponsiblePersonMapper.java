package mks.myworkspace.crm.transformer;

import mks.myworkspace.crm.entity.ResponsiblePerson;

public class ResponsiblePersonMapper {
	public static String toDTO(ResponsiblePerson responsiblePerson) {
		return responsiblePerson != null ? responsiblePerson.getName() : null;
	}
}
