
package com.ehrms.tmis.Users.ProgramManagerCumConsultant.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ehrms.tmis.Users.ProgramManagerCumConsultant.Service.M_NatureOfStaffService;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_NatureOfStaff;

import java.util.Map;

@RestController
@RequestMapping("/api/natureofstaff")
public class M_NatureOfStaffController {

    @Autowired
    private M_NatureOfStaffService m_natureofstaffService;

    @PostMapping
    public M_NatureOfStaff addNatureofstuff(@RequestBody M_NatureOfStaff mnatureofstaff) {
        return m_natureofstaffService.saveMnatureofstuff(mnatureofstaff);
    }

    @GetMapping("/getall")
    public ResponseEntity<Iterable<M_NatureOfStaff>> getAllStaff() {
        Iterable<M_NatureOfStaff> staff = m_natureofstaffService.fetchAllStaff();
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody Map<String, String> requestBody) {
        try {
            Long id = Long.valueOf(requestBody.get("id"));
            m_natureofstaffService.deleteStaff(id);
            return ResponseEntity.noContent().build();
        } catch (NumberFormatException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<M_NatureOfStaff> updateVenue(@RequestBody M_NatureOfStaff staffDetails) {
        M_NatureOfStaff updatedStaff = m_natureofstaffService.updatestaff(staffDetails.getNatureOfStaffId(),
                staffDetails);
        return updatedStaff != null ? new ResponseEntity<>(updatedStaff, HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}