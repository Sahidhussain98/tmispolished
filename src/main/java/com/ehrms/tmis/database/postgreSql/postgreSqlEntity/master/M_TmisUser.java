package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class M_TmisUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "emp_cd")
    private String empCd;

    @Column(name = "full_name")
    private String fullName;

    private String tmisCode;

    private String tmisUserName;

    private String tmisPassword;

    private String userDescription;

    private String accountNumber;

    private String ifscCode;

    @ManyToOne
    @JoinColumn(name = "bankId")
    private M_BankName bankName;

    @Transient
    private Long bankId;

    public void setEmpCd(String empCd) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEmpCd'");
    }

    public String getEmpCd() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmpCd'");
    }

    public String getFullName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFullName'");
    }

}
