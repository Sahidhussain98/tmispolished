package com.tmisehrms.user.service;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Menu;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Process;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_MenuRepository;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_ProcessRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class M_MenuService {

    @Autowired
    private M_MenuRepository m_menuRepository;

    @Autowired
    private M_ProcessRepository m_processRepository;

    public M_Menu createOrUpdateMenuItem(M_Menu menu) {

        return m_menuRepository.save(menu);
    }

    // Get user by ID
    public Optional<M_Menu> getMenuById(Long id) {
        return m_menuRepository.findById(id);
    }

    // Get all users
    public Iterable<M_Menu> getAllMenus() {
        return m_menuRepository.findAll();
    }

    // Delete menu by ID
    @Transactional
    public void deleteMenu(Long id) {
        // Find the menu to be deleted
        Optional<M_Menu> menuOptional = m_menuRepository.findById(id);
        if (menuOptional.isPresent()) {
            M_Menu menu = menuOptional.get();

            // Find all processes associated with this menu
            List<M_Process> processes = m_processRepository.findByMenu(menu);

            // Remove the menu association from each process
            for (M_Process process : processes) {
                process.setMenu(null);
                m_processRepository.save(process);
            }

            // Delete the menu
            m_menuRepository.deleteById(id);
        }
    }

    // Update user fields
    public M_Menu updateMenu(Long id, M_Menu userDetails) {
        Optional<M_Menu> menuOptional = m_menuRepository.findById(id);
        if (menuOptional.isPresent()) {
            M_Menu menu = menuOptional.get();
            menu.setMenuName(userDetails.getMenuName());
            menu.setMenuCode(userDetails.getMenuCode());
            return m_menuRepository.save(menu);
        } else {
            return null;
        }
    }
}
