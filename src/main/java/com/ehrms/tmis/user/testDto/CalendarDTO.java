package com.tmisehrms.user.testDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Program;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Topic;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Venue;

import lombok.Data;

@Data
public class CalendarDTO {
    private Long id;
    private String programName;
    private String topic;
    private String venue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String duration;
  
    
   
}