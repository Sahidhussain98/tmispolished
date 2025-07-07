package com.ehrms.tmis.Users.Trainee.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_AssignTrainee;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Calendar;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_AssignTraineeRepository;

@Service
public class TraineeService {

    @Autowired
    private T_AssignTraineeRepository assignTraineeRepository;

    public List<M_Calendar> getCalendarsByEmpCd(String empCd) {
        List<T_AssignTrainee> assignments = assignTraineeRepository.findByEmpCd(empCd);
        return assignments.stream()
                .map(T_AssignTrainee::getCalendar)
                .collect(Collectors.toList());
    }

}
