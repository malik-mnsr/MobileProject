package com.hai811i.mobileproject.entity;

import java.time.LocalDateTime;

public class GoogleEvent {
    private String id;
    private String summary;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String htmlLink;

    public GoogleEvent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getHtmlLink() {
        return htmlLink;
    }

    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }

    public GoogleEvent(String id, String summary, LocalDateTime startTime, LocalDateTime endTime, String htmlLink) {
        this.id = id;
        this.summary = summary;
        this.startTime = startTime;
        this.endTime = endTime;
        this.htmlLink = htmlLink;
    }


    // Constructor, getters and setters
}
