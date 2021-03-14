package com.baro.model;

public enum Constant {

    COURSE_NAME_KEY("course-name"),
    COURSE_UUID_KEY("course-uuid"),
    USER_UUID_KEY("user-uuid"),
    USER_NAME_KEY("user-name");

    // TODO add more as needed


    public final String key;
    Constant(String key) {
        this.key = key;
    }
}
