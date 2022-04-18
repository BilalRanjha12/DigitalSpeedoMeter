package com.leeddev.digitalspeedometer.HistoryUtils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leeddev.digitalspeedometer.Model.HistoryLocationModel
import com.leeddev.digitalspeedometer.R
import java.text.SimpleDateFormat
//import com.orbitalsonic.speedometer.DBUtils.DbHelper
//import com.orbitalsonic.speedometer.Model.HistoryLocationModel
//import com.orbitalsonic.speedometer.R
import java.util.*

class HistoryAdapter(arrayList: ArrayList<HistoryLocationModel>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val speedList = arrayList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.history_rv_design, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var speedInfo: HistoryLocationModel = speedList[position]
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = speedInfo.getDate().toLong()
        holder.tvDate.text = speedList[position].getDate()
        holder.tvMaximumSpeed.text = speedList[position].getMaxSpeed()
        holder.tvAvgSpeed.text = speedList[position].getAvgSpeed()
        holder.tvDuration.text = speedList[position].getDuration()
        holder.tvDistance.text = speedList[position].getDistance()
    }

    override fun getItemCount(): Int {
        return speedList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDate: TextView = itemView.findViewById(R.id.tv_date)
        var tvMaximumSpeed: TextView = itemView.findViewById(R.id.tv_max_speed)
        var tvAvgSpeed: TextView = itemView.findViewById(R.id.tv_avg_speed)
        var tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
        var tvDistance: TextView = itemView.findViewById(R.id.tv_distance)
    }
}

