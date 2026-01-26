package com.jbass.orbital.di


import com.jbass.orbital.data.remote.RealTimeClient
import com.jbass.orbital.data.repository.RealTimeRepositoryImpl
import com.jbass.orbital.domain.repository.RealTimeClientRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealTimeModule {

    @Singleton // Provide always the same instance
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        // Run this code when providing an instance of CoroutineScope
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideRealTimeClient(
        httpClient: HttpClient,
        scope: CoroutineScope
    ): RealTimeClient =
        RealTimeClient(httpClient,scope)

    @Provides
    @Singleton
    fun provideRealTimeRepository(
        client: RealTimeClient
    ): RealTimeClientRepository =
        RealTimeRepositoryImpl(client)
}
