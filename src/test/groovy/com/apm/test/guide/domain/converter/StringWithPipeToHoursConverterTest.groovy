package com.apm.test.guide.domain.converter

import groovy.util.logging.Slf4j
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static spock.genesis.Gen.*

@Slf4j
class StringWithPipeToHoursConverterTest extends Specification {

    @Shared
    def stringWithPipeToBytesConverter

    @Shared
    def hoursRegex

    def setupSpec() {
        stringWithPipeToBytesConverter = new StringWithPipeToHoursConverter()

        hoursRegex = ""
        (1..24).each {hoursRegex += "(\\s|[1-9][|]|[01][0-9][|]|2[0-3][|])"}
        hoursRegex = "[|]$hoursRegex"
    }

    @Unroll
    def "call convertToDatabaseColumn with random #hours"() {
        setup:
        log.info("hours: $hours")
        def resultLength = stringWithPipeToBytesConverter.delimiter.joiner.join(hours).length() + (hours.isEmpty() ? 0 : 2)

        when:
        def result = stringWithPipeToBytesConverter.convertToDatabaseColumn(hours)

        then:
        resultLength == result.length()

        where:
        hours << list(integer(0, 23), 0, 24).take(100)
    }

    @Unroll
    def "call convertToEntityAttribute with random #hours"() {
        setup:
        def hourLength = stringWithPipeToBytesConverter.delimiter.splitter.split(hours).size()

        when:
        def numberListSize = stringWithPipeToBytesConverter.convertToEntityAttribute(hours).size()

        then:
        hourLength == numberListSize

        where:
        hours << string(~hoursRegex).take(100)
    }
}
