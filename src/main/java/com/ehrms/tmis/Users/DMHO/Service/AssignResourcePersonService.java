package com.ehrms.tmis.Users.DMHO.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_AssignResourcePerson;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Calendar;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_CalendarRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_AssignResourcePersonRepository;

@Service
@Transactional
public class AssignResourcePersonService {

    @Autowired
    private T_AssignResourcePersonRepository assignResourcePersonRepository;

    @Autowired
    private M_CalendarRepository calendarRepository;

    public T_AssignResourcePerson save(T_AssignResourcePerson resourcePerson) {
        if (resourcePerson.getEmpCd() == null || resourcePerson.getEmpCd().isEmpty()) {
            throw new RuntimeException("empCd cannot be null or empty");
        }

        if (resourcePerson.getCalendar() == null || resourcePerson.getCalendar().getCalendarId() == null) {
            throw new RuntimeException("Invalid calendar data");
        }

        M_Calendar calendar = calendarRepository.findById(resourcePerson.getCalendar().getCalendarId())
                .orElseThrow(() -> new RuntimeException(
                        "Calendar not found with id: " + resourcePerson.getCalendar().getCalendarId()));
        resourcePerson.setCalendar(calendar);

        return assignResourcePersonRepository.save(resourcePerson);
    }

    public boolean isResourcePersonAlreadyAssigned(String empCd, Long calendarId) {
        return assignResourcePersonRepository.existsByEmpCdAndCalendarCalendarId(empCd, calendarId);
    }

    public List<T_AssignResourcePerson> getAssignedResourcePersonsByCalendarId(Long calendarId) {
        return assignResourcePersonRepository.findByCalendarIdWithCalendar(calendarId);
    }

    public void deleteById(Long assignId) {
        assignResourcePersonRepository.findById(assignId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignResourcePersonRepository.deleteById(assignId);
    }

    public AssignResourcePersonService(T_AssignResourcePersonRepository assignResourcePersonRepository) {
        this.assignResourcePersonRepository = assignResourcePersonRepository;
    }
}
