package com.ehrms.tmis.Users.SoftwareAdmin.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehrms.tmis.Users.SoftwareAdmin.Dto.DashboardDTO;
import com.ehrms.tmis.Users.SoftwareAdmin.Service.DashboardService;


@RestController
@RequestMapping("/Sadmin")
public class DashboardController {
    @Autowired
      private  DashboardService dashboardService;


       @GetMapping("/DashboardData")
    public ResponseEntity<DashboardDTO> getDashboard(
        @RequestParam(value = "date", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date) {

        LocalDate refDate = (date != null ? date : LocalDate.now());
        DashboardDTO stats = dashboardService.getDashboardStats(refDate);
        return ResponseEntity.ok(stats);
    }
  
}
