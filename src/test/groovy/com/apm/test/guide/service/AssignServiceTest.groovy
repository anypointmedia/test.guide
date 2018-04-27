package com.apm.test.guide.service

import com.apm.test.guide.domain.Ad
import com.apm.test.guide.domain.AvailableAdCache
import com.apm.test.guide.repo.AvailableAdCacheRepository
import groovy.util.logging.Slf4j
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static io.github.benas.randombeans.api.EnhancedRandom.randomListOf
import static spock.genesis.Gen.integer

@Slf4j
class AssignServiceTest extends Specification {

    @Shared AssignService assignService

    @Shared AvailableAdCacheRepository availableAdCacheRepository

    def setupSpec() {
        availableAdCacheRepository = Mock()

        assignService = new AssignService()
        assignService.availableAdCacheRepository = availableAdCacheRepository
    }


    @Unroll
    def "updateAvailableAds - 랜덤 인자로 확인 - arguments size: #args.size()"() {
        when:
        assignService.updateAvailableAds(args)

        then:
        noExceptionThrown()

        where:
        args << [
                    [],
                    null,
                    randomListOf(10, Ad),
                    randomListOf(5, Ad),
                    randomListOf(0, Ad),
                    randomListOf(2, Ad)
                ]
    }

    @Unroll
    def "extractAd - cache size 에 따른 추출 ad 확인 - size: #cacheSize"() {
        setup:
        def allAds = randomListOf(cacheSize, AvailableAdCache)
        assignService.availableAdCaches = allAds

        when:
        def result = assignService.extractAd()

        then:
        (cacheSize == 0 && result == null) || allAds.count { it.id == result.id } == 1

        where:
        cacheSize << integer(0, 10).take(10)
    }
}
