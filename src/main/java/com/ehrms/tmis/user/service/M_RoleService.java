package com.tmisehrms.user.service;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Process;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_ProcessRepository;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class M_RoleService {

    @Autowired
    private M_RoleRepository m_RoleRepository;

    @Autowired
    private M_ProcessRepository m_processRepository;

    public M_Role createOrUpdateRoleItem(M_Role role) {
        return m_RoleRepository.save(role);
    }

    @Transactional
    public M_Role createRoleWithProcesses(M_Role role, List<Long> processIds) {
        Set<M_Process> processSet = new HashSet<>();
        for (Long processId : processIds) {
            M_Process process = m_processRepository.findById(processId)
                    .orElseThrow(() -> new RuntimeException("Process not found with id: " + processId));
            processSet.add(process);
        }

        // Set the processes on the role
        role.setProcesses(processSet);

        // Set the role on each process
        for (M_Process process : processSet) {
            process.getRoles().add(role); // Update the bidirectional relationship
        }

        // Save the role and processes
        return m_RoleRepository.save(role); // This will save both the role and processes due to cascading
    }

    // Get user by ID
    public Optional<M_Role> getRoleById(Long id) {
        return m_RoleRepository.findById(id);
    }

    // Get all users
    public Iterable<M_Role> getAllRoles() {
        return m_RoleRepository.findAll();
    }

    @Transactional
    public void deleteRole(Long id) {
        M_Role role = m_RoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Remove the role from all related processes
        for (M_Process process : role.getProcesses()) {
            process.getRoles().remove(role);
        }

        // Clear the processes from the role
        role.getProcesses().clear();

        // Save the role to update the join table
        m_RoleRepository.save(role);

        // Call another function after saving changes
        m_RoleRepository.deleteById(id);

    }

    // Update user fields
    public M_Role updateRole(Long id, M_Role roleDetails) {
        // System.out.println("roleDetails = " + roleDetails);
        Optional<M_Role> roleOptional = m_RoleRepository.findById(id);
        if (roleOptional.isPresent()) {
            M_Role role = roleOptional.get();
            role.setRoleCode(roleDetails.getRoleCode());
            role.setRoleName(roleDetails.getRoleName());
            role.setRoleDescription(roleDetails.getRoleDescription());
            role.setLandingPage(roleDetails.getLandingPage());
            // Remove the role from all related processes
            for (M_Process process : role.getProcesses()) {
                process.getRoles().remove(role);
            }

            // Clear the processes from the role
            role.getProcesses().clear();

            List<Long> processIds = roleDetails.getProcesses().stream()
                    .map(M_Process::getProcessId) // Assuming processes have IDs
                    .toList();

            Set<M_Process> processSet = new HashSet<>();
            for (Long processId : processIds) {
                M_Process process = m_processRepository.findById(processId)
                        .orElseThrow(() -> new RuntimeException("Process not found with id: " + processId));
                processSet.add(process);
            }

            // Set the processes on the role
            role.setProcesses(processSet);

            // Set the role on each process
            for (M_Process process : processSet) {
                process.getRoles().add(role); // Update the bidirectional relationship
            }

            return m_RoleRepository.save(role);
        } else {
            return null;
        }
    }
}
