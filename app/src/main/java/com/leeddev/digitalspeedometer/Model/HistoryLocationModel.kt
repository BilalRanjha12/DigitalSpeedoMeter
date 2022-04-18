package com.leeddev.digitalspeedometer.Model

import android.graphics.drawable.Icon
import android.widget.TextView
import java.util.*
import java.util.Date as Date

data class HistoryLocationModel
    (
    private var Date: String,
    private var MaxSpeed: String,
    private var AvgSpeed: String,
    private var Duration: String,
    private var Distance: String
)
{
    fun getDate(): String {
        return Date
    }

    fun setDate(Date: String) {
        this.Date = Date
    }

    fun getMaxSpeed(): String {
        return MaxSpeed
    }

    fun setMaxSpeed(MaxSpeed: String) {
        this.MaxSpeed = MaxSpeed
    }

    fun getAvgSpeed():String {
        return AvgSpeed
    }

    fun setAvgSpeed(AvgSpeed: String){
        this. AvgSpeed = AvgSpeed
    }
    fun getDuration(): String {
        return Duration
    }

    fun setDuration(tvDuration: String) {
        this.Duration = Duration
    }

    fun getDistance(): String {
        return Distance
    }

    fun setDistance(tvDistance: String) {
        this.Distance = Distance
    }

}
