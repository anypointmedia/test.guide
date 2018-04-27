package com.apm.test.guide.service

import com.apm.test.guide.domain.Campaign
import com.apm.test.guide.domain.CampaignStatus
import com.apm.test.guide.repo.CampaignRepository
import groovy.util.logging.Slf4j
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static spock.genesis.Gen.*

@Slf4j
class CampaignServiceTest extends Specification {

    @Shared CampaignService campaignService

    @Shared CampaignRepository campaignRepository

    @Shared
    def now = new Date()

    def setupSpec() {
        campaignRepository = Mock()

        campaignService = new CampaignService()
        campaignService.campaignRepository = campaignRepository
    }

    @Unroll
    def "call save - #randomCampaign"() {
        setup:
        def expect = randomCampaign.validate()
        campaignRepository.save(_ as Campaign) >> _

        when:
        def result = campaignService.save(randomCampaign as Campaign)

        then:
        noExceptionThrown()
        expect == result

        where:
        randomCampaign << type(Campaign,
                id: getLong(),
                title: string(1, 100),
                startAt: date(now - 10, now + 5),
                endAt: date(now + 1, now + 10),
                status: these(CampaignStatus).repeat()
        ).take(100)
    }
}
