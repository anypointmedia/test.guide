package com.apm.test.guide.domain

import groovy.util.logging.Slf4j
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.Charset
import java.time.ZoneId

import static spock.genesis.Gen.*

@Slf4j
class CampaignTest extends Specification {

    @Shared
    EnhancedRandom randomBean

    @Shared
    def now = new Date()

    def setupSpec() {
        randomBean = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .objectPoolSize(100)
                .randomizationDepth(3)
                .charset(Charset.forName("UTF-8"))
                .dateRange((now - 10).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), (now + 5).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .stringLengthRange(1, 100)
                .collectionSizeRange(0, 24)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(true)
                .build()
    }

    @Unroll
    def "test using genesis - call validate with #randomCampaign"() {
        when:
        def invalid = randomCampaign.startAt < now + 1 || randomCampaign.endAt <= randomCampaign.startAt
        def result = randomCampaign.validate()

        then:
        invalid != result

        where:
        randomCampaign << type(Campaign,
                id: getLong(),
                title: string(1, 100),
                startAt: date(now - 10, now + 5),
                endAt: date(now - 10, now + 10),
                status: these(CampaignStatus).repeat()
        ).take(10)
    }

    @Unroll
    def "test using random beans -  call validate with #randomCampaign"() {
        when:
        def invalid = randomCampaign.startAt < now + 1 || randomCampaign.endAt <= randomCampaign.startAt
        def result = randomCampaign.validate()

        then:
        invalid != result

        where:
        randomCampaign << randomBean.objects(Campaign, 10)
    }

}
