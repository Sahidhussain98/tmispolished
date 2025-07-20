package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_Resources;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class M_Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long calendarId;

    @ManyToOne
    @JoinColumn(name = "programId")
    private M_Program programeName;

    @ManyToMany
    @JoinTable(name = "mt_calendar_natureofStaff", joinColumns = @JoinColumn(name = "calendarId"), inverseJoinColumns = @JoinColumn(name = "natureOfStaffId"))
    private Set<M_NatureOfStaff> natureOfStaffs;

    private Long target;

    @ManyToOne
    @JoinColumn(name = "venueId")
    private M_Venue venueName;

    @ManyToOne
    @JoinColumn(name = "resourcePersonId")
    private T_Resources resourcePerson;

    @ManyToMany
    @JoinTable(name = "mt_calendar_district", joinColumns = @JoinColumn(name = "calendarId"), inverseJoinColumns = @JoinColumn(name = "districtId"))
    private Set<M_District> district;

    // New relationship mapping to M_Topic
    @ManyToOne
    @JoinColumn(name = "topicId")
    private M_Topic topic;

    private LocalDate startDate;
    private LocalDate endDate;

    private String trainingLevel;

    private String status;

    private Long duration;
    @Transient
    private Long programId;

    @Transient
    private Long natureOfStaffId;

    @Transient
    private Long venueId;

    @Transient
    private Long resourcePersonId;

    @Transient
    private Long topicId;

    @Transient
    private Long districtId;

}
