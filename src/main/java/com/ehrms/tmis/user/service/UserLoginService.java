package com.ehrms.tmis.user.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.database.msSql.sqlEntity.MUserMaster;
import com.ehrms.tmis.database.msSql.sqlRepository.MUserMasterRepository;

@Service
public class UserLoginService {

    @Autowired
    private MUserMasterRepository userRepo;

    public MUserMaster authenticate(String empCd, String pwdtemp) {
        // Find all users with the same empCd
        List<MUserMaster> users = userRepo.findByIdEmpCd(empCd);

        if (users.isEmpty()) {
            return null; // No user found
        }

        // Check if any user's pwdtemp matches
        Optional<MUserMaster> matchedUser = users.stream()
                .filter(user -> pwdtemp.equals(user.getPwdtemp()))
                .findFirst();

        return matchedUser.orElse(null); // Return matched user or null
    }
}
