package com.ehrms.tmis.securityAndAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.database.msSql.sqlEntity.MUserMaster;
import com.ehrms.tmis.database.msSql.sqlEntity.MUserMasterId;
import com.ehrms.tmis.database.msSql.sqlRepository.MUserMasterRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MUserMasterService {
    /**
     *
     */
    @Autowired
    private MUserMasterRepository mUserMasterRepository;

    // public MUserMasterService(MUserMasterRepository mUserMasterRepository) {
    // this.mUserMasterRepository = mUserMasterRepository;
    // }

    public List<MUserMaster> getAllMUserMasters() {
        return mUserMasterRepository.findAll();
    }

    public Optional<MUserMaster> getMUserMasterByIdAndPwdTemp(MUserMasterId id, String pwdtemp) {
        return mUserMasterRepository.findByIdAndPwdtemp(id, pwdtemp);
    }

    public Optional<MUserMaster> getMUserMasterById(MUserMasterId id) {
        return mUserMasterRepository.findById(id);
    }

    public Optional<MUserMaster> getMUserMasterByIdAndPwd(MUserMasterId id, String pwd) {
        System.out.println("Entered getMUserMasterByIdAndPwd  ID:" + id + " Password " + pwd);
        return mUserMasterRepository.findByIdAndPwd(id, pwd);
    }
}
