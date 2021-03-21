package com.baro.constants


enum class FileEnum(// TODO Add more Enums as required.
        val key: String?) {
    // Directories
    OFFLINE_DIRECTORY("offline"),
    USER_DIRECTORY("user"),
    TEMP_DIRECTORY("temp"),
    COURSE_DIRECTORY("course"),
    META_DATA_FILE("meta-data.json"),
    PHOTO_THUMBNAIL_FILE("photo-thumbnail.jpeg"),
    DOWNLOAD_DIRECTORY("download")

}