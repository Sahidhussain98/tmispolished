package com.tmisehrms.user.controller;

import com.tmisehrms.user.service.UserDistrictMappingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-district-mapping")
public class UserDistrictMappingController {

    @Autowired
    private UserDistrictMappingService mappingService;

    /**
     * Assigns multiple roles to a user within a specific district.
     *
     * @param userId     ID of the user
     * @param districtId ID of the district
     * @param roleIds    List of role IDs to assign
     * @return Success response
     */
    @PostMapping("/assign")
    public ResponseEntity<?> assignRolesToUserInDistrict(
            @RequestParam Long userId,
            @RequestParam Long districtId,
            @RequestBody List<Long> roleIds) {

        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }
        if (districtId == null) {
            return ResponseEntity.badRequest().body("District ID is required");
        }
        if (roleIds == null || roleIds.isEmpty()) {
            return ResponseEntity.badRequest().body("At least one roleId must be provided");
        }

        try {
            mappingService.assignRolesToUserInDistrict(userId, districtId, roleIds);
            return ResponseEntity.ok("Roles assigned successfully to user " + userId + " in district " + districtId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Error assigning roles: " + ex.getMessage());
        }
    }
}
