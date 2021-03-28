package com.baro.constants


enum class FileEnum(// TODO Add more Enums as required.
        val key: String?) {
    // Directories
    OFFLINE_DIRECTORY("offline"),
    USER_DIRECTORY("user"),
    COURSE_DIRECTORY("course"),
    SLIDE_DIRECTORY("slide"),
    META_DATA_FILE("meta-data.json"),
    PHOTO_THUMBNAIL_FILE("photo-thumbnail.jpeg"),
    DOWNLOAD_DIRECTORY("download"),
    VIDEO_EXTENSION(".mp4");


}