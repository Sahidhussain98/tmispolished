package com.ehrms.tmis.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Menu;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Process;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_MenuRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_ProcessRepository;

import java.util.Optional;

@Service
public class M_ProcessService {

    @Autowired
    private M_ProcessRepository m_processRepository;

    @Autowired
    private M_MenuRepository m_menuRepository;

    public M_Process createOrUpdateProcessItem(M_Process process) {
        if (process.getMenuID() != null) {
            // Fetch the menu entity
            M_Menu menu = m_menuRepository.findById(process.getMenuID())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid menu ID: " + process.getMenuID()));

            // Set the menu relationship
            process.setMenu(menu);
        } else {
            // If menu ID is null, set the menu to null
            process.setMenu(null);
        }

        // Save the process
        return m_processRepository.save(process);
    }

    // Get user by ID
    public Optional<M_Process> getProcessById(Long id) {
        return m_processRepository.findById(id);
    }

    // Get all users
    public Iterable<M_Process> getAllProcess() {
        return m_processRepository.findAll();
    }

    // Delete user by ID
    // public void deleteProcess(Long id) {
    // m_processRepository.deleteById(id);
    // }

    @Transactional
    public void deleteProcess(Long id) {
        M_Process process = m_processRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Process not found with id: " + id));

        // Remove the role from all related processes
        for (M_Role role : process.getRoles()) {
            role.getProcesses().remove(process);
        }

        // Clear the processes from the role
        process.getRoles().clear();

        // Save the role to update the join table
        m_processRepository.save(process);

        // Call another function after saving changes
        m_processRepository.deleteById(id);

    }

    // Update user fields
    @Transactional
    public M_Process updateProcess(Long id, M_Process userDetails) {
        Optional<M_Process> ProcessOptional = m_processRepository.findById(id);
        if (ProcessOptional.isPresent()) {
            M_Process process = ProcessOptional.get();
            process.setProcessCode(userDetails.getProcessCode());
            process.setProcessName(userDetails.getProcessName());
            process.setPageURL(userDetails.getPageURL());

            // Fetch the menu entity
            M_Menu menu = m_menuRepository.findById(userDetails.getMenuID())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid menu ID: " + process.getMenuID()));

            process.setMenu(menu);

            // process.setMenuID(userDetails.getMenuID());
            return m_processRepository.save(process);
        } else {
            return null;
        }
    }
}