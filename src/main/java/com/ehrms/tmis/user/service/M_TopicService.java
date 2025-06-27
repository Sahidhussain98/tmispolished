package com.tmisehrms.user.service;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Topic;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class M_TopicService {

    @Autowired
    private M_TopicRepository topicRepository;

    public Optional<M_Topic> findById(Long id) { 
        return topicRepository.findById(id);
    
    }


}