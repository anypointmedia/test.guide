package com.apm.test.guide.domain.converter;

import javax.persistence.Convert;

@Convert
public class StringWithPipeToHoursConverter extends StringToNumberListConverter<Byte, PipeDelimiter> {
    public StringWithPipeToHoursConverter() {
        super(PipeDelimiter.class, Byte.class);
    }
}
