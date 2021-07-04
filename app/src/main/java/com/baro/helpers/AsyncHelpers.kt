package com.baro.helpers

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.baro.constants.CategoryEnum
import com.baro.constants.FileEnum
import com.baro.constants.JSONEnum
import com.baro.helpers.interfaces.*
import com.baro.helpers.interfaces.OnCreatorCourseCredentialsLoad
import com.baro.models.Country
import com.baro.models.Course
import com.baro.models.User
import com.baro.ui.share.p2p.WifiDirectEndpoint
import org.json.JSONArray
import java.io.*
import java.lang.ref.WeakReference
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AsyncHelpers {
    /**
     * With this class you can call the following methods:
     * VerifyUserCredentials(callback)
     */

    ///////////////////////////////////////////////////////////////////////////////////////////////


    @RequiresApi(api = Build.VERSION_CODES.O)
    fun verifyUserCredentials(file: File?): User? {


        val userMetaDataPath = Paths.get(file.toString(),
            FileEnum.USER_DIRECTORY.key,
            FileEnum.META_DATA_FILE.key)


        val userThumbnailPath = Paths.get(file.toString(),
            FileEnum.USER_DIRECTORY.key,
            FileEnum.PHOTO_THUMBNAIL_FILE.key)

        val userMetaDataFile = FileHelper.getFileAtPath(userMetaDataPath)
        val userThumbnailFile = FileHelper.getFileAtPath(userThumbnailPath)

        var user: User? = null

        if (userMetaDataFile != null) {
            val contentMetaData = FileHelper.readFile(userMetaDataFile)
            val jsonMetaData = contentMetaData?.let { JSONHelper.createJSONFromString(it) }

            user = if (userThumbnailFile != null && userThumbnailFile.length() > 0) {
                User(
                    UUID.fromString(jsonMetaData?.get(JSONEnum.USER_UUID_KEY.key) as String),
                    jsonMetaData[JSONEnum.USER_NAME_KEY.key] as String,
                    userThumbnailFile
                )
            } else {
                User(
                    UUID.fromString(jsonMetaData?.get(JSONEnum.USER_UUID_KEY.key) as String),
                    jsonMetaData[JSONEnum.USER_NAME_KEY.key] as String)
            }
        }
        return user

    }



    ////////////////////////////////////////////////////////////////////////////////////////////////

    @RequiresApi(Build.VERSION_CODES.P)
    fun userCredentialsSave(username: String, path: String, photoUri: Uri?, weakReference: WeakReference<Context>): Boolean {
        savePhotoUri(photoUri, weakReference.get())
        return saveCredentials(username, path)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun saveCredentials(username: String, path: String): Boolean {

        val credentialDetails = HashMap<String?, String?>()
        val userUUID = UUID.randomUUID()
        credentialDetails[JSONEnum.USER_NAME_KEY.key] = username
        credentialDetails[JSONEnum.USER_UUID_KEY.key] = userUUID.toString()
        val jsonCredentials = JSONHelper.createJSONFromHashMap(credentialDetails)
        val userMetaDataPath = Paths.get(
            path,
            FileEnum.USER_DIRECTORY.key,
            FileEnum.META_DATA_FILE.key
        )
        val userMetaDataFile = FileHelper.createFileAtPath(userMetaDataPath)
        return FileHelper.writeToFile(userMetaDataFile, jsonCredentials.toString())
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private fun savePhotoUri(photoUri: Uri?, context: Context?) {
        if (photoUri != null) {
            if (context != null) {
                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, photoUri))
                val userThumbnailPicturePath = Paths.get(
                    context.getExternalFilesDir(null).toString(),
                    FileEnum.USER_DIRECTORY.key,
                    FileEnum.PHOTO_THUMBNAIL_FILE.key)
                val file = File(userThumbnailPicturePath.toString())
                FileHelper.writeBitmapToFile(file, bitmap)}


        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////


    @RequiresApi(Build.VERSION_CODES.P)
    fun loadUserThumbnail(thumbnailFile: File?, weakReferenceContentResolver: WeakReference<ContentResolver>): Bitmap? {
        val content = weakReferenceContentResolver.get()
        return if (thumbnailFile != null && thumbnailFile.exists()) {
            val source = ImageDecoder.createSource(content!!, Uri.fromFile(thumbnailFile))
            ImageDecoder.decodeBitmap(source)
        } else {
            null
        }
        }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @RequiresApi(Build.VERSION_CODES.P)
    fun courseCredentialsSave(course: Course?, imageUri: Uri?, context: WeakReference<Context>): Boolean {
        if (course != null) {
            saveCredentials(course, context)
        }

        // Save Photo URI
        savePhotoUri(imageUri, course, context)

        return true



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveCredentials(course: Course, context: WeakReference<Context>) {

        val course = course
        val courseMetadata = HashMap<String?, String?>()

        courseMetadata[JSONEnum.USER_NAME_KEY.key] = course?.getCreator()?.getUserUUID().toString()
        courseMetadata[JSONEnum.COURSE_NAME_KEY.key] = course?.getCourseName()
        courseMetadata[JSONEnum.COURSE_UUID_KEY.key] = course?.getCourseUUID().toString()
        courseMetadata[JSONEnum.COURSE_CREATION_DATETIME.key] = course.getCreationDate()?.toString()
        val isoCode = course.getCourseCountry()?.getIsoCode()
        if (isoCode == null) {
            courseMetadata[JSONEnum.COURSE_LANGUAGE.key] = "null"
        } else {
            courseMetadata[JSONEnum.COURSE_LANGUAGE.key] = course.getCourseCountry()?.getIsoCode()
        }
        courseMetadata[JSONEnum.COURSE_CATEGORY.key] = JSONArray(course.getCourseCategory().toString()).toString()
        courseMetadata[JSONEnum.COURSE_SLIDES.key] = JSONArray(course.getSlides().toString()).toString()

        val courseMetaDataPath = Paths.get(
            context.get()?.getExternalFilesDir(null).toString(),
            FileEnum.USER_DIRECTORY.key,
            FileEnum.COURSE_DIRECTORY.key,
            course?.getCourseUUID().toString(),
            FileEnum.META_DATA_FILE.key
        )

        val courseMetaDataFile = FileHelper.createFileAtPath(courseMetaDataPath)
        val courseJSONMeta = JSONHelper.createJSONFromHashMap(courseMetadata)


        FileHelper.writeToFile(courseMetaDataFile, courseJSONMeta.toString())
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun savePhotoUri(photoUri: Uri?, course: Course?, context: WeakReference<Context>) {
        if (photoUri != null) {

            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.get()?.contentResolver!!, photoUri))
            val userThumbnailPicturePath = Paths.get(context.get()?.getExternalFilesDir(null).toString(),
                FileEnum.USER_DIRECTORY.key,
                FileEnum.COURSE_DIRECTORY.key,
                course?.getCourseUUID().toString(),
                FileEnum.PHOTO_THUMBNAIL_FILE.key)
            val file = File(userThumbnailPicturePath.toString())

            FileHelper.writeBitmapToFile(file, bitmap)
        }
    }


    class CreatorCourseCredentialsLoad(private var callback: OnCreatorCourseCredentialsLoad) : AsyncTask<CreatorCourseCredentialsLoad.TaskParams, Void?, ArrayList<Pair<Course, Uri?>>>() {
        @RequiresApi(Build.VERSION_CODES.P)
        override fun doInBackground(vararg params: TaskParams?): ArrayList<Pair<Course, Uri?>>? {
            var courses = ArrayList<Pair<Course, Uri?>>()

            val path = params[0]?.path
            val coursesFile = FileHelper.createDirAtPath(path)
            val user = params[0]?.user
            if (coursesFile.listFiles() != null) {
                for (courseFolder in coursesFile.listFiles()) {

                    if (courseFolder.isDirectory) {
                        val jsonFilePath = Paths.get(courseFolder.toString(), FileEnum.META_DATA_FILE.key)

                        val jsonFile = jsonFilePath.toFile()

                        // Retrieve content file
                        val contents = FileHelper.readFile(jsonFile)

                        if (contents != null) {
                            val jsonContents = JSONHelper.createJSONFromString(contents!!)

                            // Course UUID
                            val courseUUID = jsonContents?.get(JSONEnum.COURSE_UUID_KEY.key)
                            // Course Name
                            val courseName = jsonContents?.get(JSONEnum.COURSE_NAME_KEY.key)
                            // Category
                            val categoryJSON = JSONArray(jsonContents?.get(JSONEnum.COURSE_CATEGORY.key).toString())
                            // Language
                            val language = jsonContents?.get(JSONEnum.COURSE_LANGUAGE.key)
                            // Timestamp
                            val courseCreationTimestamp = jsonContents?.get(JSONEnum.COURSE_CREATION_DATETIME.key).toString().toLong()
                            // Slides
                            val slidesJSON = JSONArray(jsonContents?.get(JSONEnum.COURSE_SLIDES.key).toString())

                            // Creation course
                            val course = Course(UUID.fromString(courseUUID as String?), user)

                            //  Adding course name
                            course.setCourseName(courseName as String)
                            // Adding Course Category
                            course.setCourseCategory(CategoryEnum.getCategoriesFromJSONArray(categoryJSON as JSONArray))
                            // Adding Course language
                            if (language.toString() == "null") {
                                course.setCourseCountry(Country(null))
                            } else {
                                course.setCourseCountry(Country(language.toString()))

                            }
                            // Adding timestamp
                            course.setCreationDate(courseCreationTimestamp)

                            // Adding Slides
                            course.setSlidesFromJSONArray(slidesJSON)

                            // Thumbnail
                            val imagePath = Paths.get(courseFolder.toString(), FileEnum.PHOTO_THUMBNAIL_FILE.key)
                            val imageFile = imagePath.toFile()
                            val imageUri = Uri.fromFile(imageFile)

                            val pair = Pair<Course, Uri>(course, imageUri)
                            courses.add(pair)
                        }
                    }
                }
            }

            var arrayListResults = ArrayList<Pair<Course, Uri?>>()

            if (courses.size > 1) {
                courses.sortedBy { it.first }
            }
            for (course in courses) {
                arrayListResults.add(course)
            }
            return arrayListResults

        }

        @RequiresApi(Build.VERSION_CODES.P)
        override fun onPostExecute(result: ArrayList<Pair<Course, Uri?>>) {
            callback.onCreatorCourseCredentialsLoad(result)
        }

        class TaskParams(var path: Path?, var user: User?)
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////

    fun videoUriSave(outputFile: File?, videoUri: Uri?, weakReferenceContentResolver: WeakReference<ContentResolver>): File? {
        if (outputFile != null && outputFile.exists()) {
            if (videoUri != null) {
                return FileHelper.copyVideoToFile(outputFile, videoUri, weakReferenceContentResolver.get())
            }
        }
        return null
    }


    fun updateJSONFile(courseMetaFile: File, slideHashMap: java.util.HashMap<String, java.util.ArrayList<String>>): Boolean {

        val contents = FileHelper.readFile(courseMetaFile)

        val jsonContents = JSONHelper.createJSONFromString(contents!!)

        for ((key, value) in slideHashMap) {
            jsonContents!!.put(key, value)
        }
        return FileHelper.writeToFile(courseMetaFile, jsonContents.toString())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteCourse(rootDir: File?, course: Course): Boolean {
        if (rootDir?.exists() == true) {
            val coursePath = Paths.get(rootDir.toString(),
            FileEnum.USER_DIRECTORY.key,
            FileEnum.COURSE_DIRECTORY.key,
            course.getCourseUUID().toString())
            val courseFile = coursePath.toFile()
            FileHelper.deleteFile(courseFile)
            return true} else {return false}
    }



    interface OnCourseDeleted {
        fun onCourseDeleted(result: Course?)
    }

    class GroupOwnerReceiveClientInetAddressAsyncTask(
        private var callback: OnClientInetAddressReceived

    ) :
        AsyncTask<String?, Void, InetAddress?>() {

        companion object {
            val PORT_GET_CLIENT_INET = 8988
        }

        @Override
        override fun onPostExecute(result: InetAddress?) {
            callback.onClientInetAddressReceived(result)
        }

        override fun doInBackground(vararg p0: String?): InetAddress? {
            return try {
                val serverSocket = ServerSocket(PORT_GET_CLIENT_INET)
                val client = serverSocket.accept();
                val clientInetAddress = client.inetAddress
                serverSocket.close();
                return clientInetAddress
            } catch (e: IOException) {
                null;
            }
        }
    }

    class ClientSendInetAddressAsyncTask(
        private var serverEndPoint: WifiDirectEndpoint,
        private var callback: OnClientInetAddressSent
    ) :
        AsyncTask<Void?, InetAddress?, InetAddress?>() {
        private val SOCKET_TIMEOUT = 5000
        override fun doInBackground(vararg p0: Void?): InetAddress? {
            val socket = Socket()
            try {
                socket.bind(null)
                socket.connect(
                    InetSocketAddress(serverEndPoint.ip, serverEndPoint.port),
                    SOCKET_TIMEOUT
                )
            } catch (e: IOException) {

            } finally {
                if (socket != null) {
                    if (socket.isConnected) {
                        try {
                            socket.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            return socket.inetAddress
        }


        @Override
        override fun onPostExecute(result: InetAddress?) {
            callback.onClientInetAddressRSent()
        }
    }


    class ReceiveCourseAsyncTask(
        private var weakContext: WeakReference<Context>,
        private var callback: OnCourseReceived
    ) :
        AsyncTask<Void?, Boolean?, Boolean?>() {
        companion object {
            val PORT_GET_COURSE = 9989
        }

        @Override
        override fun onPostExecute(result: Boolean?) {
            callback.onCourseReceived(result)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg p0: Void?): Boolean? {
            return try {
                val serverSocket = ServerSocket(PORT_GET_COURSE)
                val client = serverSocket.accept();


                val inputStream = client.getInputStream();

                val dis = DataInputStream(inputStream)
                val courseSize = dis.readLong()
                val courseUUID = dis.readUTF()
                callback.setProgressCourseSize(courseSize)
                val courseZipPath = Paths.get(
                    weakContext.get()?.getExternalFilesDir(null).toString(),
                    FileEnum.LEARN_DIRECTORY.key,
                    "$courseUUID.zip"
                )
                val coursePath = Paths.get(
                    weakContext.get()?.getExternalFilesDir(null).toString(),
                    FileEnum.LEARN_DIRECTORY.key,
                    courseUUID
                )
                val courseTempZipFile = FileHelper.createFileAtPath(courseZipPath)

                writeToFile(inputStream, courseTempZipFile!!);
                serverSocket.close();
                FileHelper.unzip(courseTempZipFile ,coursePath.toString())
            } catch (e: IOException) {
                false
            }
        }

        // TODO remove this
        private fun writeToFile(inputStream: InputStream?, courseZipFile: File): Boolean {
            val buf = ByteArray(1024)
            var len: Int
            var currentSize = 0
            try {
                val outStream: OutputStream = FileOutputStream(courseZipFile)
                while (inputStream!!.read(buf).also { len = it } != -1) {
                    currentSize += 1024
                    outStream.write(buf, 0, len)
                    callback.setProgress(currentSize)
                }
                callback.setProgress(currentSize)
                inputStream.close()
            } catch (e: IOException) {
                Log.d("Not saved file", e.toString())
                return false
            }
            return true
        }

    }


    class SendCourseAsyncTask(
        private var receiverEndPoint: WifiDirectEndpoint,
        private var callback: OnCourseSent
    ) :
        AsyncTask<SendCourseAsyncTask.TaskParams?, Boolean?, Boolean?>() {
        private val SOCKET_TIMEOUT = 5000
        override fun doInBackground(vararg p0: SendCourseAsyncTask.TaskParams?): Boolean? {
            val courseZipFile = p0[0]?.courseZipFile
            val courseUUID = p0[0]?.courseUUID
            val socket = Socket()

            try {
                socket.bind(null)
                socket.connect(
                    InetSocketAddress(receiverEndPoint.ip, receiverEndPoint.port),
                    SOCKET_TIMEOUT
                )
                val stream: OutputStream = socket.getOutputStream()


                // Send course UUID
                val dos = DataOutputStream(stream)
                courseZipFile?.length()?.let { dos.writeLong(it) }
                dos.flush()
                dos.writeUTF(courseUUID.toString())
                dos.flush()

                var `is`: InputStream? = null
                try {
                    `is` = FileInputStream(courseZipFile)
                } catch (e: FileNotFoundException) {
                }
                copyFile(`is`, stream)
            } catch (e: IOException) {
                Log.i("FAILED SEND COURSE", e.toString())
                return false
            } finally {
                if (socket != null) {
                    if (socket.isConnected) {
                        try {
                            socket.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            return true
        }

        private fun copyFile(inputStream: InputStream?, out: OutputStream): Boolean {
            val buf = ByteArray(1024)
            var len: Int
            try {
                while (inputStream?.read(buf).also { len = it!! } != -1) {
                    out.write(buf, 0, len)
                }
                out.close()
                inputStream?.close()
            } catch (e: IOException) {
                return false
            }
            return true
        }
        class TaskParams(
            var courseZipFile: File?,
            var courseUUID: UUID?
        )

    }

}
