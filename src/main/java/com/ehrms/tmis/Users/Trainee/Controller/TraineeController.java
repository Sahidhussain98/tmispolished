package com.ehrms.tmis.Users.Trainee.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehrms.tmis.Users.Trainee.Service.TraineeService;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_AssignTrainee;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Calendar;
import com.ehrms.tmis.securityAndAuthentication.jwt.JwtHelper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/trainee")
public class TraineeController {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/calendar")
    public ResponseEntity<List<M_Calendar>> getCalendarsByEmpCd(HttpServletRequest request) {

        String jwt = extractJwtFromCookie(request);

        if (jwt == null || jwt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String empCd;
        try {
            empCd = jwtHelper.getUsernameFromToken(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(401).build(); // unauthorized
        }
        System.out.println("Employee Code: " + empCd);
        List<M_Calendar> calendars = traineeService.getCalendarsByEmpCd(empCd);
        System.out.println("Calendars: " + calendars);
        return ResponseEntity.ok(calendars);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;
        for (Cookie cookie : request.getCookies()) {
            if ("JWT".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
