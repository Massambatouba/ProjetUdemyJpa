package com.hibernate4all.tutorial.converter;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hibernate4all.tutorial.domain.Certification;

@Converter(autoApply = true)
public class CertificationAttributeConverter
		implements
			AttributeConverter<Certification, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Certification attribute) {
		return attribute != null ? attribute.getKey() : null;
	}

	@Override
	public Certification convertToEntityAttribute(Integer dbData) {
		return Stream.of(Certification.values())
				.filter(certif -> certif.getKey().equals(dbData)).findFirst()
				.orElse(null);
	}

}
