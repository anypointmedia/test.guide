package com.apm.test.guide.domain;

import com.apm.test.guide.domain.converter.StringWithPipeToHoursConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@ToString(exclude = "ad")
public class AdHourTarget {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Ad ad;

    @Convert(converter = StringWithPipeToHoursConverter.class)
    private List<Byte> hours;
}
