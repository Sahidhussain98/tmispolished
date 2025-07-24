package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_AssignResourcePerson;


public interface T_AssignResourcePersonRepository extends JpaRepository<T_AssignResourcePerson, Long> {

    boolean existsByEmpCdAndCalendarCalendarId(String empCd, Long calendarId);

    @Query("SELECT ar FROM T_AssignResourcePerson ar JOIN FETCH ar.calendar WHERE ar.calendar.calendarId = :calendarId")
    List<T_AssignResourcePerson> findByCalendarIdWithCalendar(Long calendarId);

    List<T_AssignResourcePerson> findByEmpCd(String empCd);
    
}
