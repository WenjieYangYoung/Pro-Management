package com.example.pro_management.ui

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import com.example.pro_management.R
import com.example.pro_management.util.MessageSortBy
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.greenrobot.eventbus.EventBus

class SubmenuSortbyFragment:BottomSheetDialogFragment() {
    companion object{
        val TAG = "submenu_sort_by"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.layout_bottom_submenu_sort_by, container, false)
        val btn_importance = view.findViewById<CardView>(R.id.main_submenu_sort_by_importance)
        val btn_duedate = view.findViewById<CardView>(R.id.main_submenu_sort_by_duetime)
        val btn_alphabet = view.findViewById<CardView>(R.id.main_submenu_sort_by_alphabet)
        val btn_sortby_back = view.findViewById<Button>(R.id.btn_sortby_back)

        btn_importance?.setOnClickListener {
                EventBus.getDefault().post(MessageSortBy("Importance"))
        }

        btn_duedate?.setOnClickListener{
                EventBus.getDefault().post(MessageSortBy("Due Date"))
        }
        btn_alphabet?.setOnClickListener {
                EventBus.getDefault().post(MessageSortBy("Alphabet"))
        }

        btn_sortby_back?.setOnClickListener{
                EventBus.getDefault().post(MessageSortBy("Back to main"))
        }


        return view
    }

    override fun onResume() {
        super.onResume()

        val display = activity?.windowManager?.defaultDisplay

        val metrics = DisplayMetrics()
        display?.getMetrics(metrics)

        super.getDialog()?.window?.setLayout(700, -1)

    }
}