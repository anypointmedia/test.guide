package com.apm.test.guide.web

import com.apm.test.guide.domain.Campaign
import com.apm.test.guide.service.CampaignService
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import spock.mock.DetachedMockFactory

import java.nio.charset.Charset
import java.time.ZoneId

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Slf4j
@WebMvcTest(CampaignController)
class CampaignControllerTest extends Specification {

    @Autowired MockMvc mockMvc

    @Autowired CampaignService campaignService

    @Shared EnhancedRandom randomBean

    @Shared def now = new Date()

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
    def "url: /campaigns POST #randomCampaign"() {
        given:
        log.debug("randomCampaign: $randomCampaign")
        log.debug("campaignService: $campaignService")
        campaignService.save(_ as Campaign) >> true

        when:
        def expectAction = mockMvc.perform(post("/campaigns").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(randomCampaign)))

        then:
        expectAction.andExpect(status().isOk())
                    .andExpect(content().string("true"))

        where:
        randomCampaign << randomBean.objects(Campaign, 100)
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        CampaignService campaignService() {
            return detachedMockFactory.Stub(CampaignService)
        }
    }
}
