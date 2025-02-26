package mks.myworkspace.crm.transformer;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import mks.myworkspace.crm.entity.EmailToCustomer.EmailStatus;

@Converter(autoApply = true)
public class EmailStatusConverter implements AttributeConverter<EmailStatus, Boolean> {

	@Override
	public Boolean convertToDatabaseColumn(EmailStatus status) {
		return status == EmailStatus.SENT; // SENT -> true (1), DRAFT -> false (0)
	}

	@Override
	public EmailStatus convertToEntityAttribute(Boolean dbData) {
		return dbData ? EmailStatus.SENT : EmailStatus.DRAFT;
	}

}
