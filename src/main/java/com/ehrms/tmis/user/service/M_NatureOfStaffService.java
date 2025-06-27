package com.tmisehrms.user.service;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_NatureOfStaff;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_NatureOfStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List; // Added
import java.util.Optional;

@Service
public class M_NatureOfStaffService {
    @Autowired
    private M_NatureOfStaffRepository m_NatureOfStaffRepository;

    public M_NatureOfStaff saveMnatureofstuff(M_NatureOfStaff mnatureofstaff) {
        return m_NatureOfStaffRepository.save(mnatureofstaff);
    }

    public Iterable<M_NatureOfStaff> fetchAllStaff() {
        return m_NatureOfStaffRepository.findAll();
    }

    public void deleteStaff(Long id) {
        m_NatureOfStaffRepository.deleteById(id);
    }

    public M_NatureOfStaff updatestaff(Long id, M_NatureOfStaff userDetails) {
        Optional<M_NatureOfStaff> staffOptional = m_NatureOfStaffRepository.findById(id);
        if (staffOptional.isPresent()) {
            M_NatureOfStaff staff = staffOptional.get();
            staff.setNatureOfStaffName(userDetails.getNatureOfStaffName());
            return m_NatureOfStaffRepository.save(staff);
        } else {
            return null;
        }
    }

    public Optional<M_NatureOfStaff> findById(Long id) { // Changed return type to Optional for consistency
        return m_NatureOfStaffRepository.findById(id);
    }

    public List<M_NatureOfStaff> findAllById(List<Long> ids) { // Added
        return m_NatureOfStaffRepository.findAllById(ids);
    }
}