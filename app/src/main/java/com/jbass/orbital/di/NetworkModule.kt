package com.jbass.orbital.di


import com.jbass.orbital.data.remote.RealTimeClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(WebSockets) {
                // Keep the connection alive with Pings
                pingInterval =  20_000.milliseconds
                contentConverter = KotlinxWebsocketSerializationConverter(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Provides
    @Singleton
    fun provideRealTimeClient(client: HttpClient): RealTimeClient {
        return RealTimeClient(client)
    }
}