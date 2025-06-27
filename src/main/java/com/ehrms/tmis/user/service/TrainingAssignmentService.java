package com.tmisehrms.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.Transactional.T_AssignTrainee;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Calendar;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_AssignTraineeRepository;
import com.tmisehrms.user.testDto.CalendarDTO;
import com.tmisehrms.user.testDto.TrainingAssignmentDTO;

// import io.jsonwebtoken.lang.Objects; // Removed incorrect import
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TrainingAssignmentService {
    
    private final T_AssignTraineeRepository assignTraineeRepository;
    
    public TrainingAssignmentService(T_AssignTraineeRepository assignTraineeRepository) {
        this.assignTraineeRepository = assignTraineeRepository;
    }
    
    public List<CalendarDTO> getAssignedCalendarsByEmpCd(String empCd) {
        List<T_AssignTrainee> assignments = assignTraineeRepository.findByEmpCd(empCd);
        
        return assignments.stream()
            .map(T_AssignTrainee::getCalendar)
            .filter(java.util.Objects::nonNull)
            .map(this::convertToCalendarDTO)
            .collect(Collectors.toList());
    }
    
    private CalendarDTO convertToCalendarDTO(M_Calendar calendar) {
    CalendarDTO dto = new CalendarDTO();
    dto.setId(calendar.getCalendarId());
    dto.setProgramName(calendar.getProgrameName() != null ? 
                      calendar.getProgrameName().getProgramName() : null);
    dto.setTopic(calendar.getTopic() != null ? 
                calendar.getTopic().getTopicName() : null);
    dto.setVenue(calendar.getVenueName() != null ? calendar.getVenueName().getVenueName() : null);
    dto.setStartDate(calendar.getStartDate() != null ? calendar.getStartDate().atStartOfDay() : null);
    dto.setEndDate(calendar.getEndDate() != null ? calendar.getEndDate().atStartOfDay() : null);
    dto.setDuration(calendar.getDuration() != null ? calendar.getDuration().toString() : null);
  
    return dto;
}

    public List<TrainingAssignmentDTO> getTrainingAssignmentsByEmpCd(String empCd) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTrainingAssignmentsByEmpCd'");
    }
}