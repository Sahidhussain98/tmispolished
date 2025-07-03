package com.ehrms.tmis.Users.SoftwareAdmin.Dto;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private long totalPrograms;
    private long totalVenues;
    private double avgCompletion;       // as percentage, e.g. 75.0
    private long pendingEvents;         // events after the given date
    private long newEvents;             // events on the given date
    private long conductedEvents;       // events before the given date
    private Map<String, Long> usersByRole;

    // getters + setters (or use Lombok @Data)
}