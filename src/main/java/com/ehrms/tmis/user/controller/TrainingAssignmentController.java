package com.ehrms.tmis.user.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehrms.tmis.user.service.TrainingAssignmentService;
import com.ehrms.tmis.user.testDto.CalendarDTO;

@RestController
@RequestMapping("/api/assigntrainee")
public class TrainingAssignmentController {

    private final TrainingAssignmentService trainingAssignmentService;

    public TrainingAssignmentController(TrainingAssignmentService trainingAssignmentService) {
        this.trainingAssignmentService = trainingAssignmentService;
    }

    @GetMapping("/getByEmpCd/{empCd}")
    public ResponseEntity<List<CalendarDTO>> getAssignedCalendarsByEmpCd(@PathVariable String empCd) {
        List<CalendarDTO> calendars = trainingAssignmentService.getAssignedCalendarsByEmpCd(empCd);
        return ResponseEntity.ok(calendars);
    }
}