package com.apm.test.guide.domain.converter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import javax.persistence.AttributeConverter;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class StringToNumberListConverter<N extends Number, D extends Delimiter> implements AttributeConverter<List<N>, String> {

    private Splitter splitter;
    private Joiner joiner;
    private String delimiterChar;
    private Class<N> numberClass;
    private Delimiter delimiter;

    StringToNumberListConverter(Class<D> delimiterClass, Class<N> numberClass) {
        Delimiter delimiter;
        try {
            delimiter = delimiterClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InvalidParameterException("invalid delimiterClass: " + delimiterClass.getName());
        }

        this.delimiter = delimiter;
        this.numberClass = numberClass;

        splitter = delimiter.getSplitter();
        joiner = delimiter.getJoiner();
        delimiterChar = delimiter.getDelimiter();
    }

    @SuppressWarnings({ "unchecked", "unsafe"})
    public D getDelimiter() {
        return (D) delimiter;
    }

    @Override
    public String convertToDatabaseColumn(List<N> entityData) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!entityData.isEmpty()) {
            joiner.appendTo(stringBuilder.append(delimiterChar), entityData).append(delimiterChar);
        }
        return stringBuilder.toString();
    }

    @Override
    public List<N> convertToEntityAttribute(String dbData) {
        return splitter.splitToList(dbData).stream().map(this::castNumber).collect(Collectors.toList());
    }

    private N castNumber(String numberText) {
        try {
            return numberClass.cast(numberClass.getMethod("valueOf", String.class).invoke(numberClass, numberText));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new InvalidParameterException("invalid number format: " + numberText);
        }
    }
}
