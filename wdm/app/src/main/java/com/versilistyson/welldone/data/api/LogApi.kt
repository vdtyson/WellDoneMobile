package com.versilistyson.welldone.data.api

import com.squareup.moshi.Json
import com.versilistyson.welldone.data.db.log.LogData
import com.versilistyson.welldone.data.db.log.PictureData
import com.versilistyson.welldone.data.util.Mappable
import retrofit2.Response
import retrofit2.http.*

interface LogApi {

    @GET("api/log/{sensorId}")
    suspend fun getLogs(@Path("sensorId") sensorId: Long): Response<List<Dto.Log>>

    @POST("api/logs")
    suspend fun addLog(@Body log: Dto.Log): Response<Any>

    @PUT("api/logs/{logId}")
    suspend fun updateLog(@Path("logId") logId: Long): Response<Dto.Log>

    @DELETE("api/logs/log")
    suspend fun deleteLog(@Path("logId") logId: Long): Response<Dto.Log>

    sealed class Dto {
        class Log(
            @Json(name = "id") val logId: Long,
            @Json(name = "sensor_id") val sensorId: Long,
            @Json(name = "date_filed") val dateFiled: String,
            @Json(name = "last_modified") val lastModified: String,
            @Json(name = "status") val status: Int,
            @Json(name = "comment") val comment: String,
            @Json(name = "picture_details") val pictures: List<LogPicture>,
            @Json(name = "operator_id") val operatorId: Long
        ): Mappable<LogData>, Dto() {
            override fun map(): LogData =
                LogData(
                    logId = logId,
                    sensorId = sensorId,
                    dateFiled = dateFiled,
                    lastModified = lastModified,
                    status = status,
                    comment = comment,
                    operatorId = operatorId
                )
            fun mapToPictureData(picPosition: Int) =  object : Mappable<PictureData> {
                override fun map() =
                    PictureData(
                        pictureLink = pictures[picPosition].imageUri,
                        subtitle = pictures[picPosition].subtitle,
                        logId = logId
                    )
            }
        }
        data class LogPicture(
            @Json(name = "image_uri") val imageUri: String,
            @Json(name = "subtitle") val subtitle: String
        )
        data class LogToPost(
            @Json(name = "sensor_id") val sensorId: Long,
            @Json(name = "comment") val comment: String
        ) : Dto()
    }

}