package com.tmisehrms.user.testDto;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.CalendarDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationDTO {
    private String empCd;
    private String title;
    private String message;

    private CalendarDetails calendarDetails;
}