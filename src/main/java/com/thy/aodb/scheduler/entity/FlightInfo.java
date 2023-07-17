package com.thy.aodb.scheduler.entity;

public final class FlightInfo {

    private Long id;

    private String description;

    private String title;

    public FlightInfo() {

    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format(
                "FlightInfo[id=%d, description=%s, title=%s]",
                this.id,
                this.description,
                this.title
        );
    }
}