package com.capstone.arabicmorph.view.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.capstone.arabicmorph.R
import com.capstone.arabicmorph.data.JamidResponse
import com.capstone.arabicmorph.view.history.historydatabase.HistoryDatabase
import com.capstone.arabicmorph.view.jamiddetector.JamidDetectorRepository
import kotlinx.coroutines.runBlocking

class StackRemoteViewsFactory(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory {

    private val database = HistoryDatabase.getInstance(context)
    private val historyDao = database.historyDao()
    private val repository = JamidDetectorRepository(historyDao)

    private var jamidResponse: JamidResponse? = null

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        runBlocking {
            try {
                val result = repository.predictJamid("text")
                jamidResponse = if (result.isSuccess) {
                    result.getOrNull()
                } else {
                    null
                }
            } catch (e: Exception) {
                jamidResponse = null
            }
        }
    }

    override fun getCount(): Int {
        return if (jamidResponse != null) 1 else 0
    }

    override fun getViewAt(position: Int): RemoteViews {
        if (jamidResponse == null) return RemoteViews(context.packageName, R.layout.story_image_banner_widget)

        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
        remoteViews.setTextViewText(R.id.textView, jamidResponse?.prediction ?: "No Data")
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}
