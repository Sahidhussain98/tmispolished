package com.ehrms.tmis.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Topic;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_TopicRepository;

import java.util.Optional;

@Service
public class M_TopicService {

    @Autowired
    private M_TopicRepository topicRepository;

    public Optional<M_Topic> findById(Long id) {
        return topicRepository.findById(id);

    }

}