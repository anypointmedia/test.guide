package com.apm.test.guide.web;

import com.apm.test.guide.domain.Campaign;
import com.apm.test.guide.service.CampaignService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    @Setter(onMethod = @__({ @Autowired }))
    private CampaignService campaignService;

    @PostMapping
    public boolean save(Campaign campaign) {
        log.debug("campaignService: {}", campaignService);
        return campaignService.save(campaign);
    }
}
