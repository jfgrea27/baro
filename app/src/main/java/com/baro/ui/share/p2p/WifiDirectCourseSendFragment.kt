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
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.interfaces.OnCourseReceived
import com.baro.helpers.interfaces.OnCourseSent
import kotlinx.android.synthetic.main.fragment_wifi_direct_send_course.*
import java.io.File
import java.net.InetAddress


class WifiDirectCourseSendFragment : Fragment(), OnCourseSent{

    // UI
    private lateinit var courseThumbnail: ImageView
    private lateinit var sendButton: ImageButton
    private lateinit var progressBar: ProgressBar


    // Model
    private lateinit var courseZipFile: File // TODO retrieve this from previous activity
    private lateinit var destinationInetAddress: InetAddress


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            destinationInetAddress = (it.getSerializable(DESTINATION_INET_ADDRESS) as InetAddress?)!!
//            courseZipFile = it.getSerializable(COURSE_ZIP_FILE) as File?

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.fragment_wifi_direct_send_course, container, false)

        configureCourseThumbnail(view)
        configureProgressBar(view)
        configureSendButton(view)
        return view
    }

    private fun configureSendButton(view: View) {
        sendButton = view.findViewById(R.id.btn_send)
        sendButton.setOnClickListener {
            val receiverEndPoint =
                WifiDirectEndpoint(
                    AsyncHelpers.ReceiveCourseAsyncTask.PORT_GET_COURSE,
                    destinationInetAddress)
            val sendCourse = AsyncHelpers.SendCourseAsyncTask(receiverEndPoint, this)
            sendCourse.execute()

        }
    }

    private fun configureProgressBar(view: View) {
        progressBar = view.findViewById(R.id.progressbar)

    }

    private fun configureCourseThumbnail(view: View) {
        courseThumbnail = view.findViewById(R.id.img_course_thumbnail)

        // TODO set this to the thumbnail of the course - using UUID to to retrieve folder etc..
    }


    companion object {
        private val COURSE_ZIP_FILE = AppTags.COURSE_ZIP_FILE.name
        private val DESTINATION_INET_ADDRESS = AppTags.DESTINATION_INET_ADDRESS.name

        @JvmStatic
        fun newInstance(courseZipFile: File?, destinationInetAddress: InetAddress?) =
            WifiDirectCourseSendFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(COURSE_ZIP_FILE, courseZipFile)
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