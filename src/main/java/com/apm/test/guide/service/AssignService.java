package com.apm.test.guide.service;

import com.apm.test.guide.domain.Ad;
import com.apm.test.guide.domain.AvailableAdCache;
import com.apm.test.guide.repo.AvailableAdCacheRepository;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AssignService {

    @Setter(onMethod = @__({ @Autowired }))
    private AvailableAdCacheRepository availableAdCacheRepository;

    @Setter(AccessLevel.PACKAGE)
    private List<AvailableAdCache> availableAdCaches;

    @PostConstruct
    public void loadLocalCache() {
        availableAdCaches = Lists.newArrayList(availableAdCacheRepository.findAll());
    }

    public void updateAvailableAds(List<Ad> ads) {
        availableAdCacheRepository.deleteAll();
        if (ads != null && !ads.isEmpty()) {
            List<AvailableAdCache> updatableAds = AvailableAdCache.build(ads);
            availableAdCacheRepository.saveAll(updatableAds);
            availableAdCaches = updatableAds;
        }
    }

    public AvailableAdCache extractAd() {
        AvailableAdCache extractedAd = null;
        if (!availableAdCaches.isEmpty()) {
            Collections.shuffle(availableAdCaches);
            extractedAd = availableAdCaches.get(0);
        }
        return extractedAd;
    }
}
