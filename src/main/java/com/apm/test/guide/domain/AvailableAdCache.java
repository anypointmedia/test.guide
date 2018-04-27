package com.apm.test.guide.domain;

import com.apm.test.guide.domain.converter.PipeDelimiter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RedisHash("available_ads")
@NoArgsConstructor
@AllArgsConstructor
public class AvailableAdCache implements Serializable {

    @Id
    private Long id;

    @Indexed
    private Long campaignId;

    public String creativeUri;

    public static AvailableAdCache build(Ad ad) {
        Campaign campaign = ad.getCampaign();
        campaign.setAds(null);

        StringBuilder hourTargetBuilder = new StringBuilder();

        PipeDelimiter pipeDelimiter = new PipeDelimiter();
        pipeDelimiter.getJoiner()
                .appendTo(hourTargetBuilder.append(pipeDelimiter.getDelimiter()), ad.getHourTarget().getHours())
                .append(pipeDelimiter.getDelimiter());

        return new AvailableAdCache(ad.getId(), campaign.getId(), ad.getCreativeUri());
    }

    public static List<AvailableAdCache> build(List<Ad> ads) {
        return ads.stream().map(AvailableAdCache::build).collect(Collectors.toList());
    }
}
