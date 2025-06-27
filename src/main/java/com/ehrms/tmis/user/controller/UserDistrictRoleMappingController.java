// package com.tmisehrms.user.controller;

// import
// com.tmisehrms.database.postgreSql.postgreSqlEntity.Transactional.T_UserDistrictRoleMapping;
// import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_User;
// import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_District;
// import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Role;
// import com.tmisehrms.user.service.UserDistrictRoleMappingService;
// import com.tmisehrms.user.service.M_UserService;
// import com.tmisehrms.user.service.M_DistrictService;
// import com.tmisehrms.user.service.M_RoleService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Optional;

// @RestController
// @RequestMapping("/api/user-district-role")
// public class UserDistrictRoleMappingController {

// @Autowired
// private UserDistrictRoleMappingService mappingService;

// @Autowired
// private M_UserService userService;

// @Autowired
// private M_DistrictService districtService;

// @Autowired
// private M_RoleService roleService;

// // Create new mapping
// @PostMapping("/assign")
// public ResponseEntity<?> assignRoleToUserInDistrict(@RequestBody
// MappingRequest request) {
// Optional<M_User> userOpt = userService.getUserById(request.getUserId());
// Optional<M_District> districtOpt = Optional.empty();
// Optional<M_Role> roleOpt = Optional.empty();

// try {
// districtOpt =
// Optional.ofNullable(districtService.findById(request.getDistrictId()));
// roleOpt = roleService.getRoleById(request.getRoleId());
// } catch (Exception e) {
// // Log or handle if needed
// }

// if (userOpt.isEmpty() || districtOpt.isEmpty() || roleOpt.isEmpty()) {
// return ResponseEntity.badRequest().body("Invalid User, District or Role ID");
// }

// T_UserDistrictRoleMapping mapping = new T_UserDistrictRoleMapping();
// mapping.setUserId(userOpt.get());
// mapping.setDistrictId(districtOpt.get());
// mapping.setRoleId(roleOpt.get());

// mappingService.saveMapping(mapping);
// return ResponseEntity.ok("Mapping saved successfully");
// }

// // Get all mappings of a user
// @GetMapping("/user/{userId}")
// public ResponseEntity<?> getMappingsByUser(@PathVariable Long userId) {
// Optional<M_User> userOpt = userService.getUserById(userId);
// if (userOpt.isEmpty()) {
// return ResponseEntity.badRequest().body("User not found");
// }
// List<T_UserDistrictRoleMapping> mappings =
// mappingService.getMappingsByUser(userOpt.get());
// return ResponseEntity.ok(mappings);
// }

// // Delete mapping by id
// @DeleteMapping("/{id}")
// public ResponseEntity<?> deleteMapping(@PathVariable Long id) {
// boolean deleted = mappingService.deleteMapping(id);
// if (deleted) {
// return ResponseEntity.ok("Mapping deleted successfully");
// } else {
// return ResponseEntity.badRequest().body("Mapping not found with id: " + id);
// }
// }

// // DTO class for incoming assign request
// public static class MappingRequest {
// private Long userId;
// private Long districtId;
// private Long roleId;

// // Getters and setters
// public Long getUserId() {
// return userId;
// }

// public void setUserId(Long userId) {
// this.userId = userId;
// }

// public Long getDistrictId() {
// return districtId;
// }

// public void setDistrictId(Long districtId) {
// this.districtId = districtId;
// }

// public Long getRoleId() {
// return roleId;
// }

// public void setRoleId(Long roleId) {
// this.roleId = roleId;
// }
// }
// }
