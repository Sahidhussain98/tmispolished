package com.ehrms.tmis.user.service;

import java.util.List; // Added
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_District;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_DistrictRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class M_DistrictService {

    @Autowired
    private M_DistrictRepository m_DistrictRepository;

    public M_District saveDistrict(M_District mvenue) {
        return m_DistrictRepository.save(mvenue);
    }

    public Iterable<M_District> fetchAllDistrict() {
        return m_DistrictRepository.findAll();
    }

    public void deleteDistrict(Long id) {
        m_DistrictRepository.deleteById(id);
    }

    public M_District updateDistrict(Long id, M_District districtDetails) {
        Optional<M_District> districtOptional = m_DistrictRepository.findById(id);
        if (districtOptional.isPresent()) {
            M_District district = districtOptional.get();
            district.setDistrictName(districtDetails.getDistrictName());
            return m_DistrictRepository.save(district);
        } else {
            return null;
        }
    }

    public M_District findById(Long id) {
        return m_DistrictRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("District not found with ID: " + id));
    }

    public List<M_District> findAllById(List<Long> ids) { // Added
        return m_DistrictRepository.findAllById(ids);
    }
}