package com.ehrms.tmis.user.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_AssignTrainee;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Calendar;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_CalendarRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_AssignTraineeRepository;

@Service
@Transactional
public class AssignTraineeService {

    @Autowired
    private T_AssignTraineeRepository assignTraineeRepository;

    @Autowired
    private M_CalendarRepository calendarRepository; // Add this repository

    public T_AssignTrainee save(T_AssignTrainee trainee) {
        if (trainee.getEmpCd() == null || trainee.getEmpCd().isEmpty()) {
            throw new RuntimeException("empCd cannot be null or empty");
        }

        if (trainee.getCalendar() != null && trainee.getCalendar().getCalendarId() != null) {
            M_Calendar calendar = calendarRepository.findById(trainee.getCalendar().getCalendarId())
                    .orElseThrow(() -> new RuntimeException(
                            "Calendar not found with id: " + trainee.getCalendar().getCalendarId()));
            trainee.setCalendar(calendar);
        } else {
            throw new RuntimeException("Invalid calendar data.");
        }

        return assignTraineeRepository.save(trainee);
    }

    public AssignTraineeService(T_AssignTraineeRepository assignTraineeRepository) {
        this.assignTraineeRepository = assignTraineeRepository;
    }

    public List<T_AssignTrainee> getAssignedTraineesByCalendarId(Long calendarId) {
        return assignTraineeRepository.findByCalendarIdWithCalendar(calendarId);
    }

    public boolean isTraineeAlreadyAssigned(String empCd, Long calendarId) {
        return assignTraineeRepository.existsByEmpCdAndCalendarCalendarId(empCd, calendarId);
    }

    public void deleteById(Long assignTraineeId) {
        assignTraineeRepository.findById(assignTraineeId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignTraineeRepository.deleteById(assignTraineeId);
    }

}
