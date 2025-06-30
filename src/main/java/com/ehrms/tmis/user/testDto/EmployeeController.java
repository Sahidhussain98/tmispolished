package com.ehrms.tmis.user.testDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehrms.tmis.user.service.EmployeeService;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employeeNames")
    public List<EmployeeNameDTO> getEmployeeNames() {
        return employeeService.getEmployeeNames();
    }
}
