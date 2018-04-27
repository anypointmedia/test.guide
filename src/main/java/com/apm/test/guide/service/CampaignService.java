package com.apm.test.guide.service;

import com.apm.test.guide.domain.Campaign;
import com.apm.test.guide.repo.CampaignRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CampaignService {

    @Setter(onMethod = @__({ @Autowired }))
    private CampaignRepository campaignRepository;

    @Transactional
    public boolean save(Campaign campaign) {
        boolean success;
        if (success = campaign.validate()) {
            campaignRepository.save(campaign);
        }
        log.debug("success: {}", success);
        return success;
    }

}
