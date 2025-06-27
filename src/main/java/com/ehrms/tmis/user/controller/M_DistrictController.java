package com.tmisehrms.user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_District;
import com.tmisehrms.user.service.M_DistrictService;

@RestController
@RequestMapping("/api/district")
public class M_DistrictController {

    @Autowired
    private M_DistrictService m_DistrictService;

    @PostMapping
    public M_District addVenue(@RequestBody M_District district) {
        return m_DistrictService.saveDistrict(district);
    }

    @GetMapping("/getall")
    public ResponseEntity<Iterable<M_District>> getAllVenue() {
        Iterable<M_District> venues = m_DistrictService.fetchAllDistrict();
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody Map<String, String> requestBody) {
        try {
            Long id = Long.valueOf(requestBody.get("id"));
            m_DistrictService.deleteDistrict(id);
            return ResponseEntity.noContent().build();
        } catch (NumberFormatException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<M_District> updateVenue(@RequestBody M_District districtDetails) {
        M_District updatedDistrict = m_DistrictService.updateDistrict(districtDetails.getDistrictId(), districtDetails);
        return updatedDistrict != null ? new ResponseEntity<>(
                updatedDistrict, HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}