package com.hai811i.mobileproject.dto;

import java.time.LocalDateTime;

public class SlotDTO {
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private boolean reserved;

    // Constructor
    public SlotDTO(Long id, LocalDateTime startAt, LocalDateTime endAt, boolean reserved) {
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
        this.reserved = reserved;
    }

    // Getters
    public Long getId() { return id; }
    public LocalDateTime getStartAt() { return startAt; }
    public LocalDateTime getEndAt() { return endAt; }
    public boolean isReserved() { return reserved; }

    // Setters (optional)
    public void setId(Long id) { this.id = id; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
    public void setReserved(boolean reserved) { this.reserved = reserved; }
}
