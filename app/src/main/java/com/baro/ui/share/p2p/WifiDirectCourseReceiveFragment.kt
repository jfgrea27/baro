package com.baro.ui.share.p2p

import androidx.fragment.app.Fragment
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.baro.R
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.interfaces.OnCourseReceived
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference


class WifiDirectCourseReceiveFragment : Fragment(), OnCourseReceived{

    // UI
    private lateinit var progressBar: ProgressBar

    private var courseSize: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        receiveCourse()
    }

    private fun receiveCourse() {
        val weakContext = WeakReference<Context>(activity)

        val receiveCourse = AsyncHelpers.ReceiveCourseAsyncTask(weakContext,this)
        receiveCourse.execute()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.fragment_wifi_direct_receive_course, container, false)

        configureProgressBar(view)

        return view
    }

    private fun configureProgressBar(view: View) {
        progressBar = view.findViewById(R.id.progressbar)
    }


    companion object {
        @JvmStatic
        fun newInstance() = WifiDirectCourseReceiveFragment()
    }

    override fun onCourseReceived(result: Boolean?) {

        Toast.makeText(
            activity?.applicationContext,
            "DEBUG: Received FIle, Extracted = " + result,
            Toast.LENGTH_LONG
        ).show()

    }

    override fun setProgressCourseSize(courseSize: Long) {
        progressBar.max = courseSize.toInt()
    }

    override fun setProgress (currentSize: Int) {
        progressBar.progress = currentSize

    }
}