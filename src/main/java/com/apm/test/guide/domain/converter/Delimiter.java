package com.apm.test.guide.domain.converter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public interface Delimiter {

    Splitter getSplitter();

    Joiner getJoiner();

    String getDelimiter();
}
