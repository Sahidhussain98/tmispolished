package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_Resources;

@Repository
public interface T_ResouresRepository extends JpaRepository<T_Resources, Long> {

    List<T_Resources> findAllByEmpCd(String empCd);


}
