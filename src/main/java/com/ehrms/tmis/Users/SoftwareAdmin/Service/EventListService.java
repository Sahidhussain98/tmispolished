package com.ehrms.tmis.Users.SoftwareAdmin.Service;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Calendar;
import com.ehrms.tmis.Users.SoftwareAdmin.Dto.EventListDTO;

@Service
public class EventListService{

    @Autowired
    private com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_CalendarRepository m_CalendarRepository;

    public EventListDTO getEventLists(LocalDate referenceDate) {
    List<M_Calendar> upcoming = m_CalendarRepository.findByStartDateAfter(referenceDate);
    List<M_Calendar> ongoing = m_CalendarRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(referenceDate, referenceDate);
    List<M_Calendar> completed = m_CalendarRepository.findByEndDateBefore(referenceDate);

    EventListDTO dto = new EventListDTO();
    dto.setUpcomingEvents(upcoming);
    dto.setOngoingEvents(ongoing);
    dto.setCompletedEvents(completed);
    return dto;
}

}
