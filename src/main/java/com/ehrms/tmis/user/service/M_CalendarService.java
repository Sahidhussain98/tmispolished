package com.ehrms.tmis.user.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Calendar;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_CalendarRepository;

import java.util.List;
import java.util.Optional;

@Service
public class M_CalendarService {

    @Autowired
    private M_CalendarRepository mcalendarRepository;

    @Transactional
    public M_Calendar createMcalendar(M_Calendar mcalendar) {
        if (mcalendar.getProgrameName() == null) {
            throw new IllegalArgumentException("Program cannot be null when creating a calendar.");
        }
        if (mcalendar.getTopic() == null) {
            throw new IllegalArgumentException("Topic cannot be null when creating a calendar.");
        }
        if (mcalendar.getVenueName() == null) {
            throw new IllegalArgumentException("Venue cannot be null when creating a calendar.");
        }
        return mcalendarRepository.save(mcalendar);
    }

    @Transactional
    public M_Calendar updateMcalendar(long calendarId, M_Calendar updatedProperties) {
        M_Calendar existingCalendar = mcalendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Mcalendar with id " + calendarId + " not found"));

        if (updatedProperties.getVenueName() != null) {
            existingCalendar.setVenueName(updatedProperties.getVenueName());
        } else {
            throw new IllegalArgumentException("Venue cannot be null during update.");
        }

        existingCalendar.setNatureOfStaffs(updatedProperties.getNatureOfStaffs());

        existingCalendar.setTarget(updatedProperties.getTarget());
        if (updatedProperties.getStartDate() != null) {
            existingCalendar.setStartDate(updatedProperties.getStartDate());
        } else {
            throw new IllegalArgumentException("Start Date cannot be null during update.");
        }
        if (updatedProperties.getEndDate() != null) {
            existingCalendar.setEndDate(updatedProperties.getEndDate());
        } else {
            throw new IllegalArgumentException("End Date cannot be null during update.");
        }
        if (updatedProperties.getDuration() != null) {
            existingCalendar.setDuration(updatedProperties.getDuration());
        } else {
            throw new IllegalArgumentException("Duration cannot be null during update.");
        }

        return mcalendarRepository.save(existingCalendar);
    }

    public List<M_Calendar> getAllMcalendarWithDetails() {
        return mcalendarRepository.findAll(); // Replace with eager fetch method if needed
    }

    public Optional<M_Calendar> getMcalendarByIdWithDetails(Long calendarId) {
        return mcalendarRepository.findById(calendarId); // Replace with eager fetch method if needed
    }

    public Optional<M_Calendar> getMcalendarById(Long calendarId) {
        return mcalendarRepository.findById(calendarId);
    }

    @Transactional
    public void deleteMcalendar(long calendarId) {
        if (!mcalendarRepository.existsById(calendarId)) {
            throw new EntityNotFoundException("Mcalendar with id " + calendarId + " not found, cannot delete.");
        }
        mcalendarRepository.deleteById(calendarId);
    }

    @Transactional
    public M_Calendar approveCalendar(Long calendarId) {
        M_Calendar calendar = mcalendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found with id: " + calendarId));

        if ("APPROVED".equals(calendar.getStatus())) {
            throw new IllegalStateException("Calendar event with ID " + calendarId + " is already approved.");
        }

        calendar.setStatus("APPROVED");
        return mcalendarRepository.save(calendar);
    }

    // Added method to get calendars pending approval
    public List<M_Calendar> getPendingApprovalCalendars() {

        return mcalendarRepository.findByStatusOrStatusIsNull("PENDING_APPROVAL");
    }

    // Added method to get approved calendars
    public List<M_Calendar> getApprovedCalendars() {
        return mcalendarRepository.findByStatus("APPROVED");
    }
}