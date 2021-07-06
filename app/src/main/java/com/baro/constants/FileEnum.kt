package com.baro.constants


enum class FileEnum(
        val key: String?) {
    // Directories
    OFFLINE_DIRECTORY("offline"),
    TEMP_ZIP_FOLDER("temp"),
    USER_DIRECTORY("user"),
    LEARN_DIRECTORY("learn"),
    COURSE_DIRECTORY("course"),
    SLIDE_DIRECTORY("slide"),
    META_DATA_FILE("meta-data.json"),
    PHOTO_THUMBNAIL_FILE("photo-thumbnail.jpeg"),
    DOWNLOAD_DIRECTORY("download"),
    VIDEO_EXTENSION(".mp4");


}