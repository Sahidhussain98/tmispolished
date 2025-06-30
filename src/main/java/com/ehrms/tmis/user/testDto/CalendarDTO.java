package com.ehrms.tmis.user.testDto;

import java.time.LocalDateTime;

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