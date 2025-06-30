package com.ehrms.tmis.user.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_District;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_User;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_DistrictRepository;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_UserRepository;

import java.util.Map;
import java.util.Optional;

@Controller
public class M_UserController {

    @Autowired
    private M_UserRepository userRepository;

    @Autowired
    private M_DistrictRepository districtRepository;

    // === LOGIN ===
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request, HttpSession session) {
        String email = request.get("email");
        String password = request.get("password");
        String enteredCaptcha = request.get("captcha");

        String storedCaptcha = (String) session.getAttribute("captcha");
        if (storedCaptcha == null || !storedCaptcha.equalsIgnoreCase(enteredCaptcha)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid CAPTCHA"));
        }

        Optional<M_User> userOpt = userRepository.findByEmailAndPassword(email, password);
        if (userOpt.isPresent()) {
            session.setAttribute("loggedInUser", userOpt.get());
            return ResponseEntity.ok(Map.of("message", "Login Successful"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }
    }

    // === REGISTRATION PAGE ===
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // returns register.html (Thymeleaf)
    }

    // === REGISTER USER ===
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> request) {
        try {
            String userName = request.get("userName").toString();
            String email = request.get("email").toString();
            String password = request.get("password").toString();
            String address = request.get("address").toString();
            Long districtId = Long.parseLong(request.get("districtId").toString());

            Optional<M_District> districtOpt = districtRepository.findById(districtId);
            if (districtOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid District ID");
            }

            M_User user = new M_User();
            user.setUserName(userName);
            user.setEmail(email);
            user.setPassword(password);
            user.setAddress(address);
            user.setDistrict(districtOpt.get());

            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering user: " + e.getMessage());
        }
    }

    // === GET LOGGED-IN USER DETAILS ===
    @GetMapping("/get-user-details")
    @ResponseBody
    public ResponseEntity<?> getUserDetails(HttpSession session) {
        M_User user = (M_User) session.getAttribute("loggedInUser");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
        }

        Map<String, Object> userData = Map.of(
                "userId", user.getUserId(),
                "userName", user.getUserName(),
                "email", user.getEmail(),
                "address", user.getAddress(),
                "district", user.getDistrict().getDistrictName());

        return ResponseEntity.ok(userData);
    }

    // === LOGOUT ===
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    // === SAMPLE PAGE ROUTES ===
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/studentform")
    public String studentForm() {
        return "studentform";
    }
}
