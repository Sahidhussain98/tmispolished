package com.ehrms.tmis.user.testDto;

public class TrainingAssignmentDTO {
    private Long assignTraineeId;
    private CalendarDTO calendar;
    private String status;

    public Long getAssignTraineeId() {
        return assignTraineeId;
    }

    public void setAssignTraineeId(Long assignTraineeId) {
        this.assignTraineeId = assignTraineeId;
    }

    public CalendarDTO getCalendar() {
        return calendar;
    }

    public void setCalendar(CalendarDTO calendar) {
        this.calendar = calendar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}