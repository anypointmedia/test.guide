package com.apm.test.guide.repo;

import com.apm.test.guide.domain.Campaign;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends CrudRepository<Campaign, Long> {
}
