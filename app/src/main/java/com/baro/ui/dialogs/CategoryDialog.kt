package com.baro.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.adapters.CategoryAdapter
import com.baro.adapters.CountryAdapter
import com.baro.models.Category
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList


class CategoryDialog(var listener: CategoryDialog.OnCategorySelected) : AppCompatDialogFragment(){

    private var categories = ArrayList<Category>()

    // UI
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var deleteAllButton: ImageButton
    private lateinit var nextButton: ImageButton

    interface OnCategorySelected {
        fun onCategorySelected(chosenCategories: ArrayList<String>)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_category_choose, container, false)

        configureCategories()


        // UI
        configureRecycleView(view)
        configureDeleteAllButton(view)





        return view
    }

    private fun configureDeleteAllButton(view: View) {
        deleteAllButton = view.findViewById(R.id.btn_delete_all)

        deleteAllButton.setOnClickListener{
            for (category in categories) {
                category.isSelected = false
            
            }
        }
    }

    private fun configureRecycleView(view: View) {
        categoryRecyclerView = view.findViewById(R.id.category_list)

        val weakContext = WeakReference<Context>(context)
        var adapter = CategoryAdapter(weakContext,categories)

        categoryRecyclerView.adapter = adapter

    }

    private fun configureCategories(){

        val stringCategories = resources.getStringArray(R.array.Categories)
        for (category in stringCategories) {
            categories.add(Category(category))
        }
    }
//    private var chosenCategories = ArrayList<String>()
//    private lateinit var categories: Array<String>
//    fun build() {
//
//        categories = resources.getStringArray(R.array.Categories)
//
//
//        val selected = BooleanArray(categories.size){false}
//
//        val mBuilder = context?.let { AlertDialog.Builder(it) }
//        mBuilder?.setTitle(R.string.choose_Categories)
//
//        mBuilder?.setMultiChoiceItems(categories, selected) { _, which, isChecked ->
//            if (isChecked) {
//                if (chosenCategories[which] == null) {
//                    chosenCategories.add(categories[which])
//                } else {
//                    chosenCategories.remove(categories[which])
//                }
//            }
//        }
//
//        mBuilder?.setCancelable(false)
//        val onclick = DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
//            listener.onCategorySelected(chosenCategories)
//        }
//
//        mBuilder?.setPositiveButton("OK", onclick)
//        mBuilder?.show()
//    }

}

