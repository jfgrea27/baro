package com.baro.constants;

public enum JSONEnum {

    COURSE_NAME_KEY("course-name"),
    COURSE_UUID_KEY("course-uuid"),
    USER_UUID_KEY("user-uuid"),
    USER_NAME_KEY("user-name");

    // TODO add more as needed


    public final String key;
    JSONEnum(String key) {
        this.key = key;
    }
}
