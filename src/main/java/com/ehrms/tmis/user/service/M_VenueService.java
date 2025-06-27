package com.tmisehrms.user.service;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_District;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Venue;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_DistrictRepository;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_VenueRepository;
import java.util.Collections; // Import Collections
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class M_VenueService {
    @Autowired
    private M_VenueRepository m_VenueRepository;

    @Autowired
    private M_DistrictRepository m_DistrictRepository;

    // ... (saveMvenue, fetchAllVenue, deleteMvenue, updateVenue remain the same)
    // ...

    public M_Venue saveMvenue(M_Venue mvenue) {
        if (mvenue.getDistrictId() != null) {
            M_District district = m_DistrictRepository.findById(mvenue.getDistrictId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid district ID: " + mvenue.getDistrictId()));
            mvenue.setDistrict(district);
        } else {
            mvenue.setDistrict(null);
        }
        return m_VenueRepository.save(mvenue);
    }

    public Iterable<M_Venue> fetchAllVenue() {
        return m_VenueRepository.findAll();
    }

    public void deleteMvenue(Long id) {
        m_VenueRepository.deleteById(id);
    }

    public M_Venue updateVenue(Long id, M_Venue venueDetail) {
        Optional<M_Venue> venueOptional = m_VenueRepository.findById(id);
        if (venueOptional.isPresent()) {
            M_Venue venue = venueOptional.get();
            venue.setVenueName(venueDetail.getVenueName());
            if (venueDetail.getDistrictId() != null) {
                M_District district = m_DistrictRepository.findById(venueDetail.getDistrictId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Invalid district ID: " + venueDetail.getDistrictId()));
                venue.setDistrict(district);
            } else {
                venue.setDistrict(null);
            }
            return m_VenueRepository.save(venue);
        } else {
            return null;
        }
    }

    // Method for single district (keep if needed elsewhere)
    public List<M_Venue> getVenuesByDistrictId(Long districtId) {
        return m_VenueRepository.findByDistrict_DistrictId(districtId);
    }

    // ++ New method for multiple districts ++
    public List<M_Venue> getVenuesByDistrictIds(List<Long> districtIds) {
        System.out.println("enetered the getVenuesByDistrictIds" + districtIds);
        if (districtIds == null || districtIds.isEmpty()) {
            return Collections.emptyList(); // Return empty if no IDs provided
        }
        List<M_Venue> venue = m_VenueRepository.findByDistrict_DistrictIdIn(districtIds);
        System.out.println("venue list " + venue);
        return venue;
    }

    public Optional<M_Venue> findById(Long id) {
        return m_VenueRepository.findById(id);
    }
}