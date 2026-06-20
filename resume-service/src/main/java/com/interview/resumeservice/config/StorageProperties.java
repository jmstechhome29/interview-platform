package com.interview.resumeservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageProperties {

    @Value("${app.storage.location:/data/resumes}")
    private String location;

    public String getLocation() { return location; }
}
