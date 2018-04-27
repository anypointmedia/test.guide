package com.apm.test.guide.repo;

import com.apm.test.guide.domain.AvailableAdCache;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AvailableAdCacheRepository extends CrudRepository<AvailableAdCache, Long> {

    List<AvailableAdCache> findByCampaignId(long campaignId);
}
