package com.apm.test.guide.schedule;

import com.apm.test.guide.domain.Ad;
import com.apm.test.guide.repo.AdRepository;
import com.apm.test.guide.service.AssignService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile({"prod"})
public class AssignScheduler {

    @Setter(onMethod = @__({ @Autowired }))
    private AdRepository adRepository;

    @Setter(onMethod = @__({ @Autowired }))
    private AssignService assignService;

    @Scheduled(fixedDelay = 60_000 * 5)
    public void updatePublishingCampaigns() {
        log.info("start to update publishing campaigns");
        List<Ad> ads = adRepository.fetchPublishingAds();
        assignService.updateAvailableAds(ads);
    }
}
