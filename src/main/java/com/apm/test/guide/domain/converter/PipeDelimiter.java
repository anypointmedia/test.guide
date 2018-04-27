package com.apm.test.guide.domain.converter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class PipeDelimiter implements Delimiter {

    private static String DELIMITER = "|";

    private static Splitter splitter = Splitter.on(DELIMITER).omitEmptyStrings().trimResults();
    private static Joiner joiner = Joiner.on(DELIMITER).skipNulls();

    @Override
    public Splitter getSplitter() {
        return splitter;
    }

    @Override
    public Joiner getJoiner() {
        return joiner;
    }

    @Override
    public String getDelimiter() {
        return DELIMITER;
    }
}
