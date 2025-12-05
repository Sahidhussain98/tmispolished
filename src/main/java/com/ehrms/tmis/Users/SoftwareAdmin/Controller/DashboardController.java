package com.ehrms.tmis.Users.SoftwareAdmin.Controller;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ehrms.tmis.Users.SoftwareAdmin.Dto.DashboardDTO;
import com.ehrms.tmis.Users.SoftwareAdmin.Service.DashboardService;

@Controller
// We will put the paths on the methods instead so this file can handle different users.
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/Users/StateManager/mainadmin") // <--- Moved path here
    public String showStateDashboard(Model model) {
        populateModelWithStats(model);
        return "Users/StateManager/mainadmin";
    }
    @GetMapping("/Users/StateNodalOfficer/snodashboard") // <--- Check your Browser URL
    public String showSnoDashboard(Model model) {
        populateModelWithStats(model);
        return "Users/StateNodalOfficer/snodashboard";
    }
    @GetMapping("/Users/DistrictManagercumDNI/dnidashboard") // <--- Check your Browser URL
    public String showDNIDashboard(Model model) {
        populateModelWithStats(model);
        return "Users/DistrictManagercumDNI/dnidashboard";
    }
    @GetMapping("/Users/DMHO/dmhoadmin")
    public String showDmhoDashboard(Model model) {
        populateModelWithStats(model);
        return "Users/DMHO/dmhoadmin";
    }
    @GetMapping("/Users/DistrictProgramManager/dmndashboard")
    public String showDmnDashboard(Model model) {
        populateModelWithStats(model);
        return "Users/DistrictProgramManager/dmndashboard";
    }
    @GetMapping("/Users/DistrictManager/distdashboard")
    public String showDistrictDashboard(Model model) {
        populateModelWithStats(model);
        return "Users/DistrictManager/distdashboard";
    }
    @GetMapping("/Users/PrincipalCumStateNodalOfficer/princidashboard") // <--- New path
    public String showPrincipalDashboard(Model model) {
        populateModelWithStats(model);
        return "Users/PrincipalCumStateNodalOfficer/princidashboard";
    }
    @GetMapping("/Users/SoftwareAdmin/softwareadmin")
    public String showTmdDashboard(Model model) {
        populateModelWithStats(model);
        return "Users/SoftwareAdmin/softwareadmin";
    }
    private void populateModelWithStats(Model model) {
        LocalDate today = LocalDate.now();
        DashboardDTO stats = dashboardService.getDashboardStats(today);
        model.addAttribute("stats", stats);
        long totalUsers = 0;
        if (stats.getUsersByRole() != null) {
            totalUsers = stats.getUsersByRole().values().stream()
                    .mapToLong(Long::longValue)
                    .sum();
        }
        model.addAttribute("totalUsers", totalUsers);
    }
}