package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class M_NatureOfStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long natureOfStaffId;

    private String natureOfStaffName;

}
