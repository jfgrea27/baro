package com.baro.ui.share.p2p

import android.app.Activity
import androidx.fragment.app.Fragment
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.RequiresApi
import com.baro.R
import com.baro.adapters.DeviceAdapter
import com.baro.constants.AppTags
import com.baro.helpers.AsyncHelpers
import com.baro.helpers.interfaces.OnCourseReceived
import java.io.File
import java.lang.ref.WeakReference
import java.net.InetAddress


class WifiDirectCourseReceiveFragment : Fragment(), OnCourseReceived{

    // UI
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        receiveCourse()
    }

    private fun receiveCourse() {
        val receiveCourse = AsyncHelpers.ReceiveCourseAsyncTask(this)
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

    override fun onCourseReceived(result: String?) {

        Toast.makeText(
            activity?.applicationContext,
            "DEBUG: Received: $result",
            Toast.LENGTH_LONG
        ).show()
    }

}