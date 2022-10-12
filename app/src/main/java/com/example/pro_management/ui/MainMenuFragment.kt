package com.example.pro_management.ui

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.pro_management.R
import com.example.pro_management.util.MainmenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.greenrobot.eventbus.EventBus

class MainMenuFragment: BottomSheetDialogFragment(){

    companion object {
        const val TAG = "MainMenu"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val view = inflater.inflate(R.layout.layout_bottom_sheet_mainmenu, container, false)

        val btn_sortBy = view.findViewById<CardView>(R.id.main_menu_item_sort_by)

        btn_sortBy?.setOnClickListener{
                EventBus.getDefault().post(MainmenuItem("Sort by"))
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