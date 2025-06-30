package com.ehrms.tmis.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Menu;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Process;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.user.service.M_MenuService;
import com.ehrms.tmis.user.service.M_ProcessService;
import com.ehrms.tmis.user.service.M_RoleService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/userRPM")
public class RoleProcessMenuController {

    // APIS RELATED TO MENU
    @Autowired
    private M_MenuService menuService;

    @PostMapping("/menu/createorupdatemenu")
    public ResponseEntity<M_Menu> createUpdateMenus(
            @RequestBody @Validated M_Menu requestbody) {
        System.out.println(requestbody);
        M_Menu savedMenu = menuService.createOrUpdateMenuItem(requestbody);
        System.out.println("created: " + savedMenu);
        return new ResponseEntity<>(savedMenu, HttpStatus.CREATED);
    }

    @GetMapping("/menu/getall")
    public ResponseEntity<Iterable<M_Menu>> getAllUsers() {
        Iterable<M_Menu> menus = menuService.getAllMenus();
        System.out.println("getAllMenus: " + menus);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @GetMapping("/menu/getmenu")
    public ResponseEntity<M_Menu> getMenuById(@RequestBody Map<String, String> requestBody) {
        Long id = Long.valueOf(requestBody.get("id"));
        Optional<M_Menu> menu = menuService.getMenuById(id);
        return menu.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete user by ID
    @DeleteMapping("/menu/deletemenu")
    public ResponseEntity<Void> deleteUser(@RequestBody Map<String, String> requestBody) {
        Long id = Long.valueOf(requestBody.get("id"));
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }

    // Update user fields
    @PutMapping("/menu/updatemenu")
    public ResponseEntity<M_Menu> updateUser(@RequestBody M_Menu menuDetails) {
        if (menuDetails.getMenuCode() == null) {
            return ResponseEntity.badRequest().body(null); // Ensure the ID is provided
        }
        M_Menu updatedMenu = menuService.updateMenu(menuDetails.getMenuID(), menuDetails);
        return updatedMenu != null ? new ResponseEntity<>(updatedMenu, HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // APIS FOR Process

    @Autowired
    private M_ProcessService processService;

    @PostMapping("/process/createorupdateprocess")
    public ResponseEntity<M_Process> createUpdateProcess(
            @RequestBody @Validated M_Process requestbody) {
        M_Process savedProcess = processService.createOrUpdateProcessItem(requestbody);
        return new ResponseEntity<>(savedProcess, HttpStatus.CREATED);
    }

    @GetMapping("/process/getall")
    public ResponseEntity<Iterable<M_Process>> getAllProcess() {
        Iterable<M_Process> process = processService.getAllProcess();
        return new ResponseEntity<>(process, HttpStatus.OK);
    }

    @GetMapping("/process/getprocess")
    public ResponseEntity<M_Process> getProcessById(@RequestBody Map<String, String> requestBody) {
        Long id = Long.valueOf(requestBody.get("id"));
        Optional<M_Process> menu = processService.getProcessById(id);
        return menu.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete user by ID
    @DeleteMapping("/process/deleteprocess")
    public ResponseEntity<Void> deleteProcess(@RequestBody Map<String, String> requestBody) {
        Long id = Long.valueOf(requestBody.get("id"));
        processService.deleteProcess(id);
        return ResponseEntity.noContent().build();
    }

    // Update user fields
    @PutMapping("/process/updateprocess")
    public ResponseEntity<M_Process> updateProcess(@RequestBody M_Process processDetails) {
        System.out.println("request body for update: " + processDetails);
        if (processDetails.getProcessCode() == null) {
            return ResponseEntity.badRequest().body(null); // Ensure the ID is provided
        }
        M_Process updatedProcess = processService.updateProcess(processDetails.getProcessId(), processDetails);
        return updatedProcess != null ? new ResponseEntity<>(updatedProcess, HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // APIS FOR Role

    @Autowired
    private M_RoleService m_RoleService;

    @PostMapping("/role/createorupdaterole")
    public ResponseEntity<M_Role> createRoleWithProcesses(@RequestBody M_Role roleWithProcessIds) {
        // Extract the list of process IDs from the incoming request
        List<Long> processIds = roleWithProcessIds.getProcesses().stream()
                .map(M_Process::getProcessId) // Assuming processes have IDs
                .toList();

        // Create role with processes and return the saved role
        M_Role savedRole = m_RoleService.createRoleWithProcesses(roleWithProcessIds, processIds);
        return ResponseEntity.ok(savedRole);
    }

    @GetMapping("/role/getall")
    public ResponseEntity<Iterable<M_Role>> getAllRoles() {
        Iterable<M_Role> role = m_RoleService.getAllRoles();
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @GetMapping("/role/getrole")
    public ResponseEntity<M_Role> getRoleById(@RequestBody Map<String, String> requestBody) {
        Long id = Long.valueOf(requestBody.get("id"));
        Optional<M_Role> role = m_RoleService.getRoleById(id);
        return role.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete user by ID
    @DeleteMapping("/role/deleterole")
    public ResponseEntity<Void> deleteRole(@RequestBody Map<String, String> requestBody) {
        Long id = Long.valueOf(requestBody.get("roleId"));
        m_RoleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    // Update user fields
    @PutMapping("/role/updaterole")
    public ResponseEntity<M_Role> updateRole(@RequestBody M_Role roleDetails) {
        System.out.println("roleDetails = " + roleDetails);
        if (roleDetails.getRoleCode() == null) {
            return ResponseEntity.badRequest().body(null); // Ensure the ID is provided
        }
        M_Role updatedRole = m_RoleService.updateRole(roleDetails.getRoleId(), roleDetails);
        return updatedRole != null ? new ResponseEntity<>(updatedRole, HttpStatus.OK)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}