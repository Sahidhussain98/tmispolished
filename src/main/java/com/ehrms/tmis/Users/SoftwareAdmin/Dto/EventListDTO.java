package com.ehrms.tmis.Users.SoftwareAdmin.Dto;

import java.util.List;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Calendar;

public class EventListDTO {
    private List<M_Calendar> upcomingEvents;
    private List<M_Calendar> ongoingEvents;
    private List<M_Calendar> completedEvents;
    public List<M_Calendar> getUpcomingEvents() {
        return upcomingEvents;
    }
    public void setUpcomingEvents(List<M_Calendar> upcomingEvents) {
        this.upcomingEvents = upcomingEvents;
    }
    public List<M_Calendar> getOngoingEvents() {
        return ongoingEvents;
    }
    public void setOngoingEvents(List<M_Calendar> ongoingEvents) {
        this.ongoingEvents = ongoingEvents;
    }
    public List<M_Calendar> getCompletedEvents() {
        return completedEvents;
    }
    public void setCompletedEvents(List<M_Calendar> completedEvents) {
        this.completedEvents = completedEvents;
    }

    
}

