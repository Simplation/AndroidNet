package com.simplation.net

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.simplation.net.exception.RequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = TestRepository()

        findViewById<Button>(R.id.btn_1).setOnClickListener {
            thread(start = true) {
                Log.i(TAG, "异步请求开始")
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val historyList = repository.fetchHistoryList()
                        Log.i(TAG, "historyList = $historyList")
                    } catch (e: RequestException) {
                        Log.e(TAG, "e = $e")
                    }
                }
                Log.i(TAG, "异步请求结束")
            }
        }

        findViewById<Button>(R.id.btn_2).setOnClickListener {
            thread(start = true) {
                try {
                    Log.i(TAG, "同步请求开始")
                    val journalismList = repository.fetchJournalismList()
                    Log.i(TAG, "journalismList = $journalismList")
                    Log.i(TAG, "同步请求结束")
                } catch (e: RequestException) {
                    Log.e(TAG, "e = $e")
                }
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }

}