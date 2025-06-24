package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Topic;

@Repository
public interface M_TopicRepository extends JpaRepository<M_Topic, Long> {
    List<M_Topic> findByProgram_ProgramId(Long programId);

}
