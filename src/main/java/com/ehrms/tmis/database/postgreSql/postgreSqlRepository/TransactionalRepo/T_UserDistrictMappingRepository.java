package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_UserDistrictMapping;

@Repository
public interface T_UserDistrictMappingRepository extends JpaRepository<T_UserDistrictMapping, Long> {

    List<T_UserDistrictMapping> findByUserId_UserId(Long UserId);

    List<T_UserDistrictMapping> findByUserId_UserIdAndDistrictId_DistrictId(Long UserId, Long districtId);

    void deleteByUserId_UserIdAndDistrictId_DistrictId(Long UserId, Long districtId);
}
