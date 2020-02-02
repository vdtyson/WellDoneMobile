package com.versilistyson.welldone.presentation.di.dashboard.module

import com.versilistyson.welldone.data.api.log.LogApi
import com.versilistyson.welldone.data.api.sensor.SensorApi
import com.versilistyson.welldone.data.api.user.UserAuthenticationApi
import com.versilistyson.welldone.data.api.user.UserDetailsApi
import com.versilistyson.welldone.data.db.WellDoneDatabase
import com.versilistyson.welldone.data.db.log.LogDao
import com.versilistyson.welldone.data.db.user.UserDetailsDao
import com.versilistyson.welldone.domain.framework.repository.AuthenticationRepository
import com.versilistyson.welldone.domain.framework.repository.LogRepository
import com.versilistyson.welldone.domain.framework.repository.SensorRepository
import com.versilistyson.welldone.domain.framework.repository.UserDetailRepository
import com.versilistyson.welldone.domain.framework.usecases.log.GetLogsUseCase
import com.versilistyson.welldone.domain.framework.usecases.sensor.GetCacheSensorStreamUseCase
import com.versilistyson.welldone.domain.framework.usecases.sensor.GetFreshSensorStreamUseCase
import com.versilistyson.welldone.domain.framework.usecases.user.GetUserDetailsUseCase
import com.versilistyson.welldone.domain.framework.usecases.user.SignInUseCase
import com.versilistyson.welldone.presentation.di.dashboard.DashboardActivityScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit

@InternalCoroutinesApi
@Module
class DashboardModule {

    @DashboardActivityScope
    @Provides
    fun provideInterceptor(token: String): Interceptor =
        object: Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", token)
                    .build()
                return chain.proceed(request)
            }
        }

    @DashboardActivityScope
    @Provides
    fun provideOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder, interceptor: Interceptor): OkHttpClient.Builder =
        okHttpClientBuilder.addInterceptor(interceptor)

    @DashboardActivityScope
    @Provides
    fun provideUserDetailsApi(retrofit: Retrofit): UserDetailsApi =
        retrofit.create(UserDetailsApi::class.java)

    @DashboardActivityScope
    @Provides
    fun provideUserDetailsDao(wellDoneDatabase: WellDoneDatabase): UserDetailsDao =
        wellDoneDatabase.userDao()

    @DashboardActivityScope
    @Provides
    fun provideSensorApi(retrofit: Retrofit): SensorApi =
        retrofit.create(SensorApi::class.java)

    @DashboardActivityScope
    @Provides
    fun provideSensorDao(wellDoneDatabase: WellDoneDatabase) =
        wellDoneDatabase.sensorDao()

    @DashboardActivityScope
    @Provides
    fun provideLogApi(retrofit: Retrofit): LogApi =
        retrofit.create(LogApi::class.java)

    @DashboardActivityScope
    @Provides
    fun provideLogDao(wellDoneDatabase: WellDoneDatabase): LogDao =
        wellDoneDatabase.logDao()

    @DashboardActivityScope
    @Provides
    fun provideUserDetailsUseCase(userDetailsRepository: UserDetailRepository) =
        GetUserDetailsUseCase(userDetailsRepository)

    @DashboardActivityScope
    @Provides
    fun provideLogUseCase(logRepository: LogRepository) =
        GetLogsUseCase(logRepository)

    @DashboardActivityScope
    @Provides
    fun provideSignInUseCase(repository: AuthenticationRepository): SignInUseCase =
        SignInUseCase(repository)

    @DashboardActivityScope
    @Provides
    fun provideFreshSensorUseCase(sensorRepository: SensorRepository): GetFreshSensorStreamUseCase {
        return GetFreshSensorStreamUseCase(sensorRepository)
    }

    @DashboardActivityScope
    @Provides
    fun provideCacheSensorUseCase(sensorRepository: SensorRepository): GetCacheSensorStreamUseCase {
        return GetCacheSensorStreamUseCase(sensorRepository)
    }
}