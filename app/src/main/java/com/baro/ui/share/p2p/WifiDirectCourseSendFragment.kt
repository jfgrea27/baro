package com.baro.ui.share.p2p

import androidx.fragment.app.Fragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.baro.R
import com.baro.constants.AppTags
import com.baro.constants.FileEnum
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.FileHelper
import com.baro.helpers.interfaces.OnCourseSent
import com.baro.models.Course
import java.net.InetAddress
import java.nio.file.Paths


class WifiDirectCourseSendFragment : Fragment(), OnCourseSent{

    // UI
    private lateinit var courseThumbnail: ImageView
    private lateinit var sendButton: ImageButton


    // Model
    private lateinit var course: Course
    private lateinit var destinationInetAddress: InetAddress


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            destinationInetAddress = (it.getSerializable(DESTINATION_INET_ADDRESS) as InetAddress?)!!
            course = (it.getParcelable(COURSE_OBJECT) as Course?)!!
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.fragment_wifi_direct_send_course, container, false)

        configureCourseThumbnail(view)
        configureSendButton(view)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureSendButton(view: View) {
        sendButton = view.findViewById(R.id.btn_send)
        sendButton.setOnClickListener {
            val receiverEndPoint =
                WifiDirectEndpoint(
                    AsyncHelpers.ReceiveCourseAsyncTask.PORT_GET_COURSE,
                    destinationInetAddress)

            val coursePath = Paths.get(
                activity?.getExternalFilesDir(null).toString(),
                FileEnum.USER_DIRECTORY.key,
                FileEnum.COURSE_DIRECTORY.key,
                course.getCourseUUID().toString()
            )
            val courseFile = coursePath.toFile()

            val tempCourseZipFilePath = Paths.get(
                activity?.getExternalFilesDir(null).toString(),
                FileEnum.TEMP_ZIP_FOLDER.key,
                course.getCourseUUID().toString() + ".zip"
            )
            val courseTempZipFile = FileHelper.createFileAtPath(tempCourseZipFilePath)
            val zippedCourseFile = FileHelper.compressDirectory(courseFile, courseTempZipFile!!)

            val sendCourse = AsyncHelpers.SendCourseAsyncTask(receiverEndPoint, this)
            val taskParams = AsyncHelpers.SendCourseAsyncTask.TaskParams(zippedCourseFile, course.getCourseUUID())
            sendCourse.execute(taskParams)

        }
    }


    private fun configureCourseThumbnail(view: View) {
        courseThumbnail = view.findViewById(R.id.img_wifi_status)

        // TODO set this to the thumbnail of the course - using UUID to to retrieve folder etc..
    }


    companion object {
        private val COURSE_OBJECT = AppTags.COURSE_OBJECT.name
        private val DESTINATION_INET_ADDRESS = AppTags.DESTINATION_INET_ADDRESS.name

        @JvmStatic
        fun newInstance(course: Course?, destinationInetAddress: InetAddress?) =
            WifiDirectCourseSendFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(COURSE_OBJECT, course)
                    putSerializable(DESTINATION_INET_ADDRESS, destinationInetAddress)

                }
            }
    }

    override fun onCourseSent(result: Boolean?) {
        if (result == true) {
            Toast.makeText(
                activity?.applicationContext,
                "DEBUG: Course Sent",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}