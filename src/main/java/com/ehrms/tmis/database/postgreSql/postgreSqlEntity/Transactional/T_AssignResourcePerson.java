package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional;

import jakarta.persistence.*;
import lombok.*;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Calendar;

@Entity
@Table(name = "t_assign_resource_person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class T_AssignResourcePerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assign_resource_person_id")
    private Long assignResourcePersonId;

    @Column(name = "emp_cd", nullable = false)
    private String empCd;

   @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    private M_Calendar calendar;
}
