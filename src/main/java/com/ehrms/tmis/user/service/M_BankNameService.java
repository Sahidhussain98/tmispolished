package com.ehrms.tmis.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_BankName;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_BankNameRepository;

import java.util.List;

@Service
public class M_BankNameService {

    @Autowired
    private M_BankNameRepository m_BankNameRepository;

    public List<M_BankName> findAllBanks() {
        return m_BankNameRepository.findAll();

    }

    public List<M_BankName> getAllBanks() {
        return m_BankNameRepository.findAll();
    }
}