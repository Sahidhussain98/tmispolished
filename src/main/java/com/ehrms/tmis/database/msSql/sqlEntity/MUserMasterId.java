package com.ehrms.tmis.database.msSql.sqlEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class MUserMasterId implements Serializable {
    private static final long serialVersionUID = -901338479204831240L;
    @Column(name = "StateId", nullable = false)
    private Integer stateId;

    @Column(name = "EmpCd", nullable = false, length = 20)
    private String empCd;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        MUserMasterId entity = (MUserMasterId) o;
        return Objects.equals(this.stateId, entity.stateId) &&
                Objects.equals(this.empCd, entity.empCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateId, empCd);
    }

}