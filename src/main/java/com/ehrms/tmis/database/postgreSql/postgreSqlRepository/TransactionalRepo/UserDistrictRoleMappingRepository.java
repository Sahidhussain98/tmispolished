package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_UserDistrictRoleMapping;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_District;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_User;

import java.util.List;

@Repository
public interface UserDistrictRoleMappingRepository extends JpaRepository<T_UserDistrictRoleMapping, Long> {
    List<T_UserDistrictRoleMapping> findByUser(M_User user);

    List<T_UserDistrictRoleMapping> findByUserAndDistrict(M_User user, M_District district);

    List<T_UserDistrictRoleMapping> findByDistrict(M_District district);

    List<T_UserDistrictRoleMapping> findByRole(M_Role role);
}
