package com.hai811i.mobileproject.entity;

import java.util.List;

public class Page<T> {
    private List<T> content;
    private int number;
    private int size;
    private int totalPages;
    private long totalElements;

    // Getters and setters
    public List<T> getContent() {
        return content;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    // ... setters if needed
}