package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class M_Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long venueId;

    private String venueName;

    @ManyToOne
    @JoinColumn(name = "districtId")
    private M_District district;

    @Transient
    private Long districtId;

}
