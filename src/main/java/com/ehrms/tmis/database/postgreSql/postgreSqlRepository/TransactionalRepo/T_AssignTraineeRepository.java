package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_AssignTrainee;

@Repository
public interface T_AssignTraineeRepository extends JpaRepository<T_AssignTrainee, Long> {

    @Query("SELECT at FROM T_AssignTrainee at WHERE at.calendar.calendarId = :calendarId")
    List<T_AssignTrainee> findByCalendarId(@Param("calendarId") Long calendarId);

    @Query("SELECT at FROM T_AssignTrainee at JOIN FETCH at.calendar WHERE at.calendar.calendarId = :calendarId")
    List<T_AssignTrainee> findByCalendarIdWithCalendar(@Param("calendarId") Long calendarId);

    boolean existsByEmpCdAndCalendarCalendarId(String empCd, Long calendarId);

    List<T_AssignTrainee> findByEmpCd(String empCd);

    // Optional<T_AssignTrainee> deleteByEmpCdAndassignTraineeId(String empCd, Long
    // assignTraineeId);

}
