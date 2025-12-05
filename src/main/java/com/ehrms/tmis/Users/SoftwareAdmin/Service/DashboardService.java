package com.ehrms.tmis.Users.SoftwareAdmin.Service;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.Users.SoftwareAdmin.Dto.DashboardDTO;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_CalendarRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_ProgramRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_RoleRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_VenueRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_UserRoleMappingRepository;

@Service
public class DashboardService {

    @Autowired
    private M_ProgramRepository m_ProgramRepository;
    @Autowired
    private M_VenueRepository m_VenuerRepository;
    @Autowired
    private M_CalendarRepository m_CalendarRepository;
    @Autowired
    private T_UserRoleMappingRepository t_UserRoleMappingRepository;
    @Autowired
    private M_RoleRepository m_RoleRepository;

    public DashboardDTO getDashboardStats(LocalDate referenceDate) {
        long totalPrograms = m_ProgramRepository.count();
        long totalVenues = m_VenuerRepository.count();

        // Pending = Start Date is After Today
        long pendingEvents = m_CalendarRepository.countByStartDateAfter(referenceDate);

        // New = Start Date is Today
        long newEvents = m_CalendarRepository.countByStartDateEquals(referenceDate);

        // Conducted = End Date is Before Today (Past events)
        // NOTE: Make sure countByEndDateBefore exists in your Repository!
        long conductedEvents = m_CalendarRepository.countByEndDateBefore(referenceDate);

        // Avoid divide-by-zero
        double avgCompletion = 0;
        long totalEvents = pendingEvents + newEvents + conductedEvents;
        if (totalEvents > 0) {
            avgCompletion = 100.0 * conductedEvents / totalEvents;
            avgCompletion = Math.round(avgCompletion * 10.0) / 10.0;
        }

        // Count users per roleName
        Map<String, Long> usersByRole = t_UserRoleMappingRepository.findAll().stream()
                .flatMap(mapping -> {
                    String emp = mapping.getEmpCd();
                    return Arrays.stream(mapping.getRoleIds())
                            .map(roleId -> new AbstractMap.SimpleEntry<Long, String>(roleId, emp));
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toSet())))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        e -> "ROLE_" + e.getKey(),
                        e -> (long) e.getValue().size()));

        DashboardDTO dto = new DashboardDTO();
        dto.setTotalPrograms(totalPrograms);
        dto.setTotalVenues(totalVenues);
        dto.setPendingEvents(pendingEvents);
        dto.setNewEvents(newEvents);
        dto.setConductedEvents(conductedEvents);
        dto.setAvgCompletion(avgCompletion);
        dto.setUsersByRole(usersByRole);

        return dto;
    }
}