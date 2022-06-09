package com.example.myapplication00

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.myapplication00.databinding.FragmentCalendarBinding
import java.time.LocalDate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer


class CalendarFragment : Fragment(), OnItemClick {
    private lateinit var binding : FragmentCalendarBinding
    private val myViewModel: MyViewModel by activityViewModels()
    lateinit var mydb: DBHelper
    lateinit var activity : Activity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container,false)
        mydb = DBHelper(activity)



        val view = inflater.inflate(R.layout.fragment_calendar, null)
        val regbtn = binding.regbtn
        regbtn.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, registerActivity::class.java)
                startActivity(intent)
            }
        })

        val monthListManager = LinearLayoutManager(requireActivity().applicationContext, LinearLayoutManager.HORIZONTAL, false)
        val monthListAdapter = AdapterMonth(requireActivity().applicationContext, this)

        binding.calendarCustom.apply {
            layoutManager = monthListManager
            adapter = monthListAdapter
            scrollToPosition(Int.MAX_VALUE/2)
        }
        val snap = PagerSnapHelper()
        snap.attachToRecyclerView(binding.calendarCustom)

        myViewModel.selectedDate.observe(viewLifecycleOwner, Observer {
            if(it != ""){ //나중에 it으로 db에 해당 date의 diary 정보가 있는지 확인
                binding.diaryBox.isVisible = true
                binding.diaryBoxNone.isVisible = false
                binding.diaryDate.text = it
                binding.diaryDate.text = it
            } else {
                binding.diaryBox.isVisible = false
                binding.diaryBoxNone.isVisible = true
            }
        })

        binding.diaryBox.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, ShowDiaryActivity::class.java)
                //temp
                //val date = view.findViewById<TextView>(R.id.diaryDate)
                intent.putExtra("date", binding.diaryDate.text)
                startActivity(intent)
            }
        })

        return binding.root
    }

    override fun setDate(date: String) {
        myViewModel.setLiveData(date)
    }

    override fun onStart() {
        super.onStart()
        val date: String = LocalDate.now().toString()
        val daily_goal = mydb.findDailyGoal(date)
        val weekly_goal = mydb.findWeeklyGoal(date)
        binding.dailygoaltext.text = "소주 "+daily_goal+"병 이하"
        binding.weeklygoaltext.text = "소주 "+weekly_goal+"병 이하"
    }
}

