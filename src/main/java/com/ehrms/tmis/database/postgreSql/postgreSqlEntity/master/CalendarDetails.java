package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarDetails {
    private String programName;
    private String topic;
    private String venue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String duration;
}
