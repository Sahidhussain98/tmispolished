package com.tmisehrms.user.controller;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Venue;
import com.tmisehrms.user.service.M_VenueService;
import java.util.Collections; // Import Collections
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/venues")
public class M_VenueController {

    @Autowired
    private M_VenueService m_venueService;

    // ... (addVenue, getAllVenue, deleteUser, updateVenue remain the same) ...
    @PostMapping
    public M_Venue addVenue(@RequestBody M_Venue mvenue) {
        return m_venueService.saveMvenue(mvenue);
    }

    @GetMapping("/getall")
    public ResponseEntity<Iterable<M_Venue>> gsetAllVenue() {
        Iterable<M_Venue> venues = m_venueService.fetchAllVenue();
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody Map<String, String> requestBody) {
        try {
            Long id = Long.valueOf(requestBody.get("id"));
            m_venueService.deleteMvenue(id);
            return ResponseEntity.noContent().build();
        } catch (NumberFormatException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<M_Venue> updateVenue(@RequestBody M_Venue venueDetails) {
        M_Venue updatedVenue = m_venueService.updateVenue(venueDetails.getVenueId(), venueDetails);
        return updatedVenue != null ? new ResponseEntity<>(updatedVenue, HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/by-districts") // Using POST to accept list in body
    public ResponseEntity<List<M_Venue>> getVenuesByMultipleDistricts(@RequestBody List<Long> districtIds) {
        if (districtIds == null) { // Note: An empty list is valid, null is not.
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        System.out.println("INSIDE  bydist:" + districtIds);

        // Allow empty list input, service will handle it
        List<M_Venue> venues = m_venueService.getVenuesByDistrictIds(districtIds);
        System.out.println("VENUES" + venues);
        return ResponseEntity.ok(venues);
    }
}