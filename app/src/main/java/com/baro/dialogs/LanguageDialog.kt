package com.baro.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.adapters.LanguageIconAdapter
import com.baro.models.Language


class LanguageDialog(var onInputListener: ImageDialog.OnInputListener?) : AppCompatDialogFragment(), LanguageIconAdapter.ItemClickListener {
    private lateinit var languagesGrid: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_language_choose, container, false)
        languagesGrid = view.findViewById(R.id.language_grid)


        var languages = ArrayList<Language>()

        languagesGrid.layoutManager = GridLayoutManager(context, languages.size)

        var adapter = LanguageIconAdapter(context, languages)
        adapter.setClickListener(this)
        languagesGrid.adapter = adapter
        return view
    }

    override fun onItemClick(view: View?, position: Int) {
        TODO("Not yet implemented")
        // TODO close dialog and pass answer back to previous activity
    }


}