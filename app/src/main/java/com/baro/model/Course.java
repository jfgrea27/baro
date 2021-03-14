package com.baro.model;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class Course {

    private final String courseName;
    private final UUID courseUUID;

    private final User creator;

    private LocalDate updateDate;

    private URI photoThumbnail;
    private URI soundThumbnail;

    private ArrayList<Slide> slides;

    public Course(String courseName, UUID courseUUID, User creator) {
        this.courseName = courseName;
        this.courseUUID = courseUUID;
        this.creator = creator;
    }

    // TODO discuss properties
}
