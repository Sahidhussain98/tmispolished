package com.ehrms.tmis.Users.SoftwareAdmin.Controller;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ehrms.tmis.Users.SoftwareAdmin.Dto.EventListDTO;
import com.ehrms.tmis.Users.SoftwareAdmin.Service.EventListService;

@RestController
@RequestMapping("/api/events")
public class EventListController {

    @Autowired
    private EventListService eventListService;

    @GetMapping("/list")
    public ResponseEntity<EventListDTO> getEventLists(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        EventListDTO dto = eventListService.getEventLists(date);
        return ResponseEntity.ok(dto);
    }
}

