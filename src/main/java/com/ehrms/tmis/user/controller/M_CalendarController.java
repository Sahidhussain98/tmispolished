package com.tmisehrms.user.controller;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.*;
import com.tmisehrms.user.service.*;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("api/calendar")
public class M_CalendarController {

    @Autowired
    private M_CalendarService mCalendarService;

    @Autowired
    private M_NatureOfStaffService mNatureOfStaffService;

    @Autowired
    private M_DistrictService mDistrictService;

    @Autowired
    private M_ProgramService mProgramService;

    @Autowired
    private M_TopicService mTopicService;

    @Autowired
    private M_VenueService mVenueService;

    @PostMapping(consumes = { "multipart/form-data", "application/x-www-form-urlencoded" })
    public ResponseEntity<?> createMcalendar(
            @RequestParam("programId") Long programId,
            @RequestParam("topicId") Long topicId,
            @RequestParam("natureOfStaff") List<Long> nosIDs,
            @RequestParam("district") List<Long> districtIds,
            @RequestParam("venueId") Long venueId,
            @RequestParam(value = "target", required = false) Long target,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("duration") Long duration,
            @RequestParam("trainingLevel") String trainingLevel) {
        try {
            M_Calendar mcalendar = new M_Calendar();

            M_Program program = mProgramService.findById(programId)
                    .orElseThrow(() -> new RuntimeException("Program not found with ID: " + programId));
            mcalendar.setProgrameName(program);

            M_Topic topic = mTopicService.findById(topicId)
                    .orElseThrow(() -> new RuntimeException("Topic not found with ID: " + topicId));
            mcalendar.setTopic(topic);

            M_Venue venue = mVenueService.findById(venueId)
                    .orElseThrow(() -> new RuntimeException("Venue not found with ID: " + venueId));
            mcalendar.setVenueName(venue);

            Set<M_NatureOfStaff> setNOS = new HashSet<>();
            if (nosIDs != null && !nosIDs.isEmpty()) {
                List<M_NatureOfStaff> foundNOS = mNatureOfStaffService.findAllById(nosIDs);
                if (foundNOS.size() != nosIDs.size()) {
                    System.err.println("Warning: Some NatureOfStaff IDs not found.");
                }
                setNOS.addAll(foundNOS);
            }
            mcalendar.setNatureOfStaffs(setNOS);

            Set<M_District> setDistrict = new HashSet<>();
            if (districtIds != null && !districtIds.isEmpty()) {
                List<M_District> foundDistricts = mDistrictService.findAllById(districtIds);
                if (foundDistricts.size() != districtIds.size()) {
                    System.err.println("Warning: Some District IDs not found.");
                }
                setDistrict.addAll(foundDistricts);
            }
            mcalendar.setDistrict(setDistrict);

            mcalendar.setTarget(target);
            mcalendar.setStartDate(startDate);
            mcalendar.setEndDate(endDate);
            mcalendar.setDuration(duration);

            mcalendar.setTrainingLevel(trainingLevel);

            M_Calendar createdMcalendar = mCalendarService.createMcalendar(mcalendar);
            return new ResponseEntity<>(createdMcalendar, HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println("Error in createMcalendar: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating calendar: " + e.getMessage());
        }
    }

    @PutMapping(value = "/update/{calendarId}", consumes = { "multipart/form-data",
            "application/x-www-form-urlencoded" })
    public ResponseEntity<?> updateMcalendar(
            @PathVariable Long calendarId,
            @RequestParam("natureOfStaff") List<Long> nosIDs,
            @RequestParam("venueId") Long venueId,
            @RequestParam(value = "target", required = false) Long target,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("duration") Long duration,
            @RequestParam("trainingLevel") String trainingLevel) {
        try {
            M_Calendar propertiesToUpdate = new M_Calendar();

            M_Venue venue = mVenueService.findById(venueId)
                    .orElseThrow(() -> new RuntimeException("Venue not found with ID: " + venueId));
            propertiesToUpdate.setVenueName(venue);

            propertiesToUpdate.setTarget(target);
            propertiesToUpdate.setStartDate(startDate);
            propertiesToUpdate.setEndDate(endDate);
            propertiesToUpdate.setDuration(duration);
            propertiesToUpdate.setTrainingLevel(trainingLevel);

            Set<M_NatureOfStaff> setNOS = new HashSet<>();
            if (nosIDs != null && !nosIDs.isEmpty()) {
                List<M_NatureOfStaff> foundNOS = mNatureOfStaffService.findAllById(nosIDs);
                if (foundNOS.size() != nosIDs.size()) {
                    System.err.println("Warning: Some NatureOfStaff IDs not found during update.");
                }
                setNOS.addAll(foundNOS);
            }
            propertiesToUpdate.setNatureOfStaffs(setNOS);

            M_Calendar updatedMcalendar = mCalendarService.updateMcalendar(calendarId, propertiesToUpdate);
            return new ResponseEntity<>(updatedMcalendar, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("Error in updateMcalendar for ID " + calendarId + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating calendar: " + e.getMessage());
        }
    }

    @GetMapping("/getCalendarById/{calendarId}")
    public ResponseEntity<M_Calendar> getCalendarById(@PathVariable Long calendarId) {
        Optional<M_Calendar> calendar = mCalendarService.getMcalendarByIdWithDetails(calendarId);
        return calendar.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/all")
    public ResponseEntity<List<M_Calendar>> getAllMcalendar(
            @RequestBody(required = false) M_Calendar filter) {
        List<M_Calendar> calendars = mCalendarService.getAllMcalendarWithDetails(filter);
        return ResponseEntity.ok(calendars);
    }

    // @GetMapping("/all")
    // public ResponseEntity<List<M_Calendar>> getAllMcalendar() {
    // List<M_Calendar> calendars = mCalendarService.getAllMcalendarWithDetails();
    // return new ResponseEntity<>(calendars, HttpStatus.OK);
    // }

    @DeleteMapping("/delete/{calendarId}")
    public ResponseEntity<Void> deleteMcalendar(@PathVariable Long calendarId) {
        try {
            mCalendarService.deleteMcalendar(calendarId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Error deleting calendar with ID " + calendarId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getCalendar")
    public ResponseEntity<M_Calendar> getMcalendarByIdFromBody(@RequestBody Map<String, String> requestBody) {
        try {
            Long calendarId = Long.valueOf(requestBody.get("calendarId"));
            Optional<M_Calendar> mcalendar = mCalendarService.getMcalendarByIdWithDetails(calendarId);
            return mcalendar.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (NumberFormatException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            System.err.println("Error in getMcalendarByIdFromBody: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/approve/{calendarId}")
    public ResponseEntity<?> approveCalendarEvent(@PathVariable Long calendarId) {
        try {
            M_Calendar approvedCalendar = mCalendarService.approveCalendar(calendarId);
            return ResponseEntity.ok(approvedCalendar);
        } catch (EntityNotFoundException e) { // Catch specific exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) { // Catch specific exception for already approved
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error approving calendar ID " + calendarId + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error approving calendar: " + e.getMessage());
        }
    }

    // Added endpoint to get approved calendars (for the "another page")
    @GetMapping("/approved")
    public ResponseEntity<?> getApprovedCalendars() {
        try {
            List<M_Calendar> calendars = mCalendarService.getApprovedCalendars();
            return ResponseEntity.ok(calendars);
        } catch (Exception e) {
            e.printStackTrace(); // or use a logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong: " + e.getMessage());
        }
    }

}