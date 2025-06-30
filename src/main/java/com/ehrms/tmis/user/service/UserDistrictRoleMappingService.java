package com.ehrms.tmis.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_UserDistrictRoleMapping;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_District;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_User;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.UserDistrictRoleMappingRepository;

import java.util.List;

@Service
public class UserDistrictRoleMappingService {

    @Autowired
    private UserDistrictRoleMappingRepository repository;

    public T_UserDistrictRoleMapping saveMapping(T_UserDistrictRoleMapping mapping) {
        return repository.save(mapping);
    }

    public List<T_UserDistrictRoleMapping> getMappingsByUser(M_User user) {
        return repository.findByUser(user);
    }

    public List<T_UserDistrictRoleMapping> getMappingsByUserAndDistrict(M_User user, M_District district) {
        return repository.findByUserAndDistrict(user, district);
    }

    public List<T_UserDistrictRoleMapping> getMappingsByDistrict(M_District district) {
        return repository.findByDistrict(district);
    }

    public List<T_UserDistrictRoleMapping> getMappingsByRole(M_Role role) {
        return repository.findByRole(role);
    }

    public void deleteMapping(Long id) {
        repository.deleteById(id);
    }
}
