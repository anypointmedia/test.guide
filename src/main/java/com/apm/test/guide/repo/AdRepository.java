package com.apm.test.guide.repo;

import com.apm.test.guide.domain.Ad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends CrudRepository<Ad, Long> {

    @Query("select distinct a from Ad a left join a.hourTarget h where a.campaign.status = com.apm.test.guide.domain.CampaignStatus.PUBLISHING and a.campaign.startAt <= CURRENT_TIMESTAMP " +
            "and a.campaign.endAt >= CURRENT_TIMESTAMP and a.startAt <= CURRENT_TIMESTAMP and a.endAt >= CURRENT_TIMESTAMP " +
            "and (h.hours is null or h.hours = '' or h.hours like CONCAT('%|', FUNCTION('HOUR', CURRENT_TIMESTAMP), '|%'))")
    List<Ad> fetchPublishingAds();

    @Query("select distinct a from Ad a left join a.hourTarget h where a.campaign.status = com.apm.test.guide.domain.CampaignStatus.FINISHED")
    List<Ad> fetchFinishedAds();
}
