package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional;


import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Calendar;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_assign_trainee")
public class T_AssignTrainee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignTraineeId;

    @ManyToOne
    @JoinColumn(name = "calendarId", nullable = false)
    private M_Calendar calendar;

    @Column(name = "empCd")
    private String empCd;

    public Long getCalendarId() {
        return calendar != null ? calendar.getCalendarId() : null;
    }

}
