package com.apm.test.guide.web

import com.apm.test.guide.domain.AvailableAdCache
import com.apm.test.guide.service.AssignService
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@Slf4j
@WebMvcTest(AssignController)
class AssignControllerTest extends Specification {

    @Autowired MockMvc mockMvc

    @Autowired AssignService assignService

    @Unroll
    def "url: /assign GET - #randomAdCache"() {
        given:
        ObjectMapper objectMapper = new ObjectMapper()
        assignService.extractAd() >> randomAdCache
        log.debug("randomAdCache: $randomAdCache")

        when:
        def responseBody = mockMvc.perform(get("/assign")).andReturn().response.contentAsString
        log.debug("responseBody: $responseBody")
        def extractedAd = objectMapper.readValue(responseBody, AvailableAdCache)

        then:
        extractedAd.id == randomAdCache.id

        where:
        randomAdCache << EnhancedRandom.randomStreamOf(100, AvailableAdCache)
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        AssignService assignService() {
            return detachedMockFactory.Stub(AssignService)
        }
    }
}
