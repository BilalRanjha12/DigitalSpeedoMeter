package com.leeddev.digitalspeedometer.HistoryUtils

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leeddev.digitalspeedometer.DBUtils.DbHelper
import com.leeddev.digitalspeedometer.Model.HistoryLocationModel
import com.leeddev.digitalspeedometer.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity() {
    lateinit var rvText: RecyclerView
    lateinit var adapter: HistoryAdapter
    lateinit var dbhelper: DbHelper

    private lateinit var list: ArrayList<HistoryLocationModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_rv)

        dbhelper = DbHelper(this)
        rvText = findViewById(R.id.rv_list_items)
        rvText.layoutManager = LinearLayoutManager(this)
        list = dbhelper.allContacts
        adapter = HistoryAdapter(list)
        rvText.adapter = adapter
    // getDataFromDb()

    }



//        val info = HistoryLocationModel()

        // val info =HistoryLocationModel()


        // info.DataInfo(formattedDate, tvDownload.getText().toString(), tvUpload.getText().toString())
        /* info.getDate()
         info.getMaxSpeed()
         info.getAvgSpeed()
         info.getDuration()
         info.getDistance()
 */
//        dbhelper.insertContact(info)
    }
