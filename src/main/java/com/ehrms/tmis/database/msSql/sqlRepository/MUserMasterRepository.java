package com.ehrms.tmis.database.msSql.sqlRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.msSql.sqlEntity.MUserMaster;
import com.ehrms.tmis.database.msSql.sqlEntity.MUserMasterId;

import java.util.List;
import java.util.Optional;

@Repository
public interface MUserMasterRepository extends JpaRepository<MUserMaster, MUserMasterId> {

        // 1) Find by entire composite key + pwdtemp
        Optional<MUserMaster> findByIdAndPwdtemp(MUserMasterId mUserMasterId, String pwdtemp);

        // 2) Find by entire composite key + pwd (if you want to check the non-temp
        // password)
        Optional<MUserMaster> findByIdAndPwd(MUserMasterId mUserMasterId, String pwd);

        // 3) Find by entire composite key only
        Optional<MUserMaster> findById(MUserMasterId mUserMasterId);

        // 4) (Explicit @Query) Find by empCd + stateId + pwdtemp
        // (this already correctly navigates into id.empCd and id.stateId)
        @Query("SELECT m " +
                        "  FROM MUserMaster m " +
                        " WHERE m.id.empCd   = :empCd " +
                        "   AND m.id.stateId = :stateId " +
                        "   AND m.pwdtemp    = :pwdtemp")
        Optional<MUserMaster> findByEmpCdAndStateIdAndPwdtemp(
                        @Param("empCd") String empCd,
                        @Param("stateId") Integer stateId,
                        @Param("pwdtemp") String pwdtemp);

        // 5) Find by stateId + empCd (both inside the EmbeddedId)
        Optional<MUserMaster> findByIdStateIdAndIdEmpCd(Integer stateId, String empCd);

        // 6) CORRECTED: Find by empCd (inside id) and pwdtemp (on the entity).
        // You must navigate into id.empCd, so the method is named
        // findByIdEmpCdAndPwdtemp(...)
        Optional<MUserMaster> findByIdEmpCdAndPwdtemp(String empCd, String pwdtemp);

        // 7) CORRECTED: Find all rows whose composite-id.empCd = :empCd
        List<MUserMaster> findByIdEmpCd(String empCd);
}
