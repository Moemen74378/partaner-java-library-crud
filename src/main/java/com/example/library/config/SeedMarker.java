package com.example.library.config;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/** Remembers initialization even if the user deletes every sample book. */
@Entity
public class SeedMarker {
    @Id
    private String name;

    protected SeedMarker() {
    }

    public SeedMarker(String name) {
        this.name = name;
    }
}
