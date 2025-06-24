package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import jakarta.persistence.*;
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
public class M_BankName {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long bankId;

    private String bankName;

}
