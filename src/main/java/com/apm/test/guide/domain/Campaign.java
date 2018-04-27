package com.apm.test.guide.domain;

import com.google.common.collect.Lists;
import lombok.Data;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Campaign {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date startAt;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date endAt;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private CampaignStatus status = CampaignStatus.WAITING;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "campaign")
    private List<Ad> ads = Lists.newArrayList();

    public boolean validate() {
        boolean result = true;
        if (startAt.compareTo(DateTime.now().plusDays(1).toDate()) < 0 || endAt.compareTo(startAt) < 1) {
            result = false;
        }
        return result;
    }
}
