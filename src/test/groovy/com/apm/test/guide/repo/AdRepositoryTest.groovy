package com.apm.test.guide.repo

import com.apm.test.guide.domain.Ad
import com.apm.test.guide.domain.AdHourTarget
import com.apm.test.guide.domain.Campaign
import com.apm.test.guide.domain.CampaignStatus
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import spock.lang.Shared
import spock.lang.Specification

import static java.util.Calendar.HOUR_OF_DAY
import static spock.genesis.Gen.*

@Slf4j
@SpringBootTest
@DataJpaTest(showSql = true)
@Rollback
class AdRepositoryTest extends Specification {

    @Shared
    def now = new Date()

    @Autowired
    AdRepository adRepository

    @Autowired
    CampaignRepository campaignRepository

    Collection<Ad> ads

    def setup() {
        def campaigns = type(Campaign,
                                    title: string(1, 100),
                                    startAt: date(now - 10, now + 5),
                                    endAt: date(now - 10, now + 10),
                                    status: these(CampaignStatus).repeat(),
                                    ads: list(type(
                                            Ad,
                                            startAt: date(now - 10, now + 5),
                                            endAt: date(now - 10, now + 10),
                                            creativeUri: string(5, 200),
                                            hourTarget: type(
                                                    AdHourTarget,
                                                    hours: list(integer(0, 23), 0, 23)
                                            )
                                    ), 1, 5)
                            ).take(1000).map {
                                campaign -> campaign.ads.forEach {
                                    ad ->
                                        ad.campaign = campaign
                                        ad.hourTarget?.ad = ad
                                }
                                return campaign
                            }.collect()

        campaigns = campaignRepository.saveAll(campaigns)

        ads = []
        campaigns.findAll { ads += it.ads }

        log.info("total ad count: $ads.size")
    }

    def "inspect fetchPublishingAds jpa query result"() {
        when:
        def resultAds = adRepository.fetchPublishingAds()
        def expectAds = ads.findAll {
            ad ->
                ad.campaign.status == CampaignStatus.PUBLISHING &&
                        ad.campaign.startAt <= now && ad.campaign.endAt >= now && ad.startAt <= now && ad.endAt >= now &&
                        (ad.hourTarget == null || ad.hourTarget.hours.isEmpty() || ad.hourTarget.hours.contains(now[HOUR_OF_DAY]))
        }

        log.info("result size: $resultAds.size, expect size: $expectAds.size")

        then:
        noExceptionThrown()
        resultAds.size == expectAds.size
        resultAds.containsAll(expectAds)
    }

    def "inspect fetchFinishedAds jpa query result"() {
        when:
        def resultAds = adRepository.fetchFinishedAds()
        def expectAds = ads.findAll { it.campaign.status == CampaignStatus.FINISHED }

        log.info("result size: $resultAds.size, expect size: $expectAds.size")

        then:
        noExceptionThrown()
        resultAds.size == expectAds.size
        resultAds.containsAll(expectAds)
    }
}
