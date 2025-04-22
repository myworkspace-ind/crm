package mks.myworkspace.crm.transformer;

import mks.myworkspace.crm.entity.Status;

public class StatusMapper {
	public static String toDTO(Status status) {
		return status != null ? status.getName() : null;
	}
}
