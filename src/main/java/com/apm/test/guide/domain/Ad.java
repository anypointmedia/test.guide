package com.apm.test.guide.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString(exclude = "campaign")
@Entity
public class Ad {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Campaign campaign;

    @Temporal(TemporalType.TIMESTAMP)
    public Date startAt;

    @Temporal(TemporalType.TIMESTAMP)
    public Date endAt;

    public String creativeUri;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "ad")
    public AdHourTarget hourTarget;
}
