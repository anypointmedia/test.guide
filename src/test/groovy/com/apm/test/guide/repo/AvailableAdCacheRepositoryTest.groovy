package com.apm.test.guide.repo

import com.apm.test.guide.domain.AvailableAdCache
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.spock.Testcontainers
import spock.genesis.generators.values.LongGenerator
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static spock.genesis.Gen.*

@Slf4j
@SpringBootTest
@Testcontainers
class AvailableAdCacheRepositoryTest extends Specification {

    static final REDIS_PORT = 6379
    static final MYSQL_PORT = 3306

    @Shared
    DockerComposeContainer composeContainer =
            new DockerComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
                    .withExposedService("test_redis", REDIS_PORT)
                    .withExposedService("test_mysql", MYSQL_PORT)
                    .withPull(false)
                    .withLocalCompose(true)

    @Autowired
    AvailableAdCacheRepository availableAdCacheRepository

    def setupSpec() {
        System.setProperty("spring.redis.host", composeContainer.getServiceHost("test_redis", REDIS_PORT) as String)
        def tempRedisPort = composeContainer.getServicePort("test_redis", REDIS_PORT) as String
        System.setProperty("spring.redis.port", tempRedisPort)
        System.setProperty("spring.datasource.url",
                "jdbc:mysql://" + composeContainer.getServiceHost("test_mysql", MYSQL_PORT) + ":"
                        + composeContainer.getServicePort("test_mysql", MYSQL_PORT) + "/apmdb?useSSL=false&useUnicode=true&characterEncoding=utf8")
        System.setProperty("spring.datasource.hikari.connection-timeout", "500")

    }

    @Unroll
    def "random data test - findByCampaignId"() {
        given:
        availableAdCacheRepository.deleteAll()
        List<AvailableAdCache> cacheAds = type([
                                id: getLong(),
                                campaignId: new LongGenerator(1, 10),
                                creativeUri: string(200)
                ], AvailableAdCache
                        ).take(cacheSize).realized
        availableAdCacheRepository.saveAll(cacheAds)

        when:
        def resultAds = availableAdCacheRepository.findByCampaignId(campaignId)
        def expectedAds = cacheAds.findAll { it.campaignId == campaignId }

        then:
        resultAds.size() == expectedAds.size()
        resultAds.count { resultAd -> expectedAds.find { it.campaignId == resultAd.campaignId } != null } == expectedAds.size()

        where:
        cacheSize << integer(0, 100).take(100)
        campaignId << integer(1, 10).take(100)
    }
}
