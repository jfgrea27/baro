package com.baro.ui.dialogs

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.adapters.CategoryAdapter
import com.baro.constants.CategoryEnum
import com.baro.models.Category
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList


class CategoryDialog(var listener: CategoryDialog.OnCategorySelected) : AppCompatDialogFragment(){

    private var categories = ArrayList<Category>()
    private var adapter: CategoryAdapter? = null

    // UI
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var nextButton: ImageButton

    interface OnCategorySelected {
        fun onCategorySelected(chosenCategories: HashSet<Category>)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_category_choose, container, false)

        configureCategories()


        // UI
        configureRecycleView(view)
        configureNextButton(view)

        return view
    }

    private fun configureNextButton(view: View) {
        nextButton = view.findViewById(R.id.btn_next)

        nextButton.setOnClickListener{
            if(adapter != null) {
                var categories = adapter!!.getSelectedCategories()
                listener.onCategorySelected(categories)
            }
            // End dialog
            this.dismiss()
        }

    }


    private fun configureRecycleView(view: View) {
        categoryRecyclerView = view.findViewById(R.id.category_list)

        val weakContext = WeakReference<Context>(context)
        adapter = CategoryAdapter(weakContext,categories)
        categoryRecyclerView.layoutManager = LinearLayoutManager(context);
        categoryRecyclerView.adapter = adapter

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun configureCategories(){

        for (category in CategoryEnum.values()) {
            val name = category.name.toLowerCase().capitalize()
            val unicode = category.unicode
            categories.add(Category(name, unicode))
        }
    }

}

