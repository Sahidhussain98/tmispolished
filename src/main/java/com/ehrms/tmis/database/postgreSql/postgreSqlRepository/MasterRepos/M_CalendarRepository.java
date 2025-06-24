package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Added for custom query
import org.springframework.data.repository.query.Param; // Added for custom query parameter
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Calendar;

import java.util.List;
import java.util.Optional; // Added for Optional return type

@Repository
public interface M_CalendarRepository extends JpaRepository<M_Calendar, Long> {

    List<M_Calendar> findByDistrictDistrictId(Long districtId);

    // Added method to find calendars by status
    List<M_Calendar> findByStatus(String status);

    // Added method to find calendars by PENDING_APPROVAL status or if status is
    // null
    List<M_Calendar> findByStatusOrStatusIsNull(String status1);

    // Added custom query for eagerly fetching all associations for findAll
    @Query("SELECT c FROM M_Calendar c LEFT JOIN FETCH c.programeName LEFT JOIN FETCH c.topic LEFT JOIN FETCH c.venueName LEFT JOIN FETCH c.natureOfStaffs LEFT JOIN FETCH c.district LEFT JOIN FETCH c.resourcePerson")
    List<M_Calendar> findAllEagerly();

    // Added custom query for eagerly fetching all associations for findById
    @Query("SELECT c FROM M_Calendar c LEFT JOIN FETCH c.programeName LEFT JOIN FETCH c.topic LEFT JOIN FETCH c.venueName LEFT JOIN FETCH c.natureOfStaffs LEFT JOIN FETCH c.district LEFT JOIN FETCH c.resourcePerson WHERE c.calendarId = :id")
    Optional<M_Calendar> findByIdEagerly(@Param("id") Long id);
}