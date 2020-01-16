package com.versilistyson.welldone.repository

import android.app.Application
import com.versilistyson.welldone.presentation.MyApplication
import com.versilistyson.welldone.data.remote.WellDoneApi
import com.versilistyson.welldone.data.remote.dto.SensorRecentResponse
import com.versilistyson.welldone.util.BASE_URL
import retrofit2.Response

class DashboardRepository(application: Application) {

    val dashboardClient = StandardClient<WellDoneApi>(application as MyApplication, BASE_URL).create(WellDoneApi::class.java)

    suspend fun fetchAllSensors(): Response<List<SensorRecentResponse>> {
        return dashboardClient.getSensors()
    }
}