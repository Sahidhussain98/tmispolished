package com.tmisehrms.user.testDto;

import java.time.LocalDateTime;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.CalendarDetails;

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
public class NotificationDTO {
    private Long id;
    private String empCd;
    private String title;
    private String message;

    private boolean isRead;
    private CalendarDetails calendarDetails;
    private LocalDateTime createdAt;
}