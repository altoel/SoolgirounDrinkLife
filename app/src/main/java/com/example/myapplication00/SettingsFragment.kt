package com.example.myapplication00

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SettingsFragment : Fragment() {

    var menuData = ArrayList<String>()
    lateinit var activity : Activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }
    lateinit var adapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        initData()
        val dialog =CustomDialog(activity)
        val recyclerView = view.findViewById<RecyclerView>(R.id.settingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SettingsAdapter(menuData)
        recyclerView.adapter = adapter

        adapter.itemClickListener = object : SettingsAdapter.ItemClickListener {
            override fun onClickItem(index: Int) {
                when(index) {
                    0 -> {
                        if(menuData[0] == "다크모드") {
                            MySharedPreferences.setBoolean(context, "switchState", true)
                            ThemeManager.applyTheme(ThemeManager.ThemeMode.DARK)

                        } else {
                            MySharedPreferences.setBoolean(context, "switchState", false)
                            ThemeManager.applyTheme(ThemeManager.ThemeMode.LIGHT)
                        }
                    }

                    1 -> {

                    }

                    2 -> {
                        val intent = Intent(activity, SetGoal::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
        return view
    }

    private fun initData() {

        menuData.add("다크모드")
        menuData.add("앱 초기화")
        menuData.add("목표 수정")
        menuData.add("도움말")

        val mode = MySharedPreferences.getBoolean(context, "switchState")
        if(mode) {
            menuData[0] = "라이트모드"
        } else {
            menuData[0] = "다크모드"
        }

    }

}