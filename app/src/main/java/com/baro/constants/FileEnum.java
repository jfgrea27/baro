package com.baro.constants;

public enum FileEnum {

    // Directories
    OFFLINE_DIRECTORY("offline"),
    USER_DIRECTORY("user"),
    TEMP_DIRECTORY("temp"),

    //Files
    META_DATA_FILE("meta-data.json"),
    PHOTO_THUMBNAIL_FILE("photo-thumbnail.jpeg");



    // TODO Add more Enums as required.

    public final String key;

     FileEnum(String key) {
        this.key = key;
    }

}
