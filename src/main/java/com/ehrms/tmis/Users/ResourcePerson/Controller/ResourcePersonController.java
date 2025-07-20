package com.ehrms.tmis.Users.ResourcePerson.Controller;

import com.ehrms.tmis.Users.ResourcePerson.Servcie.ResourcePersonService;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_Resources;
import com.ehrms.tmis.securityAndAuthentication.jwt.JwtHelper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resource-person")
public class ResourcePersonController {

  @Autowired
  private ResourcePersonService resourcePersonService;

  @Autowired
  private JwtHelper jwtHelper;

  @PostMapping("/upload")
  public ResponseEntity<?> uploadResources(
      @RequestParam("files") MultipartFile[] files,
      HttpServletRequest request) {

    String empCd = extractEmpCdFromRequest(request);
    if (empCd == null) {
      return ResponseEntity.status(401).body("Invalid or missing token");
    }

    try {
      List<T_Resources> savedResources = resourcePersonService.saveResources(empCd, files);
      return ResponseEntity.ok(savedResources);
    } catch (IOException e) {
      return ResponseEntity.internalServerError().body("Failed to save resources");
    }
  }

  private String extractEmpCdFromRequest(HttpServletRequest request) {
    String jwt = extractJwtFromCookie(request);
    if (jwt == null) return null;

    try {
      return jwtHelper.getUsernameFromToken(jwt); // username is empCd
    } catch (Exception e) {
      return null;
    }
  }

  private String extractJwtFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) return null;

    for (Cookie cookie : request.getCookies()) {
      if ("JWT".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
@GetMapping("/files")
public ResponseEntity<?> getUploadedResources(HttpServletRequest request) {
  String empCd = extractEmpCdFromRequest(request);
  if (empCd == null) {
    return ResponseEntity.status(401).body("Unauthorized");
  }

  List<T_Resources> resources = resourcePersonService.getResourcesByEmpCd(empCd);
  return ResponseEntity.ok(resources);
}

@GetMapping("/download/{id}")
public ResponseEntity<byte[]> downloadResource(@PathVariable Long id) {
  T_Resources resource = resourcePersonService.getResourceById(id);
  if (resource == null) {
    return ResponseEntity.notFound().build();
  }

  return ResponseEntity.ok()
      .header("Content-Disposition", "attachment; filename=\"" + resource.getFileName() + "\"")
      .header("Content-Type", resource.getFileType())
      .body(resource.getResource());
}


}
