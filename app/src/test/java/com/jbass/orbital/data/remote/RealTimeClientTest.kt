package com.jbass.orbital.data.remote


import com.jbass.orbital.domain.model.device.DeviceState
import com.jbass.orbital.domain.model.message.ServerMessage
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class RealTimeClientTest {

    // Shared JSON config matching your app
    private val jsonConfig = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
        classDiscriminator = "classType" // MATCHING YOUR MODULE
    }

    @Test
    fun `connect updates deviceState when server sends valid StateUpdate`() = runTest(timeout = 5.seconds) {
        testApplication {
            // 1. MOCK SERVER SETUP - REMOVE .launch
            application {
                install(WebSockets) {
                    contentConverter = KotlinxWebsocketSerializationConverter(jsonConfig)
                }
                routing {
                    webSocket("/orbital/device") {
                        // Create dummy data
                        val mockDevice =

                        // Send Update
                        val msg = ServerMessage.FullStateUpdate(listOf(mockDevice))
                        send(Frame.Text(jsonConfig.encodeToString<ServerMessage>(msg)))

                        // Keep socket open
                        for (frame in incoming) {}
                    }
                }
            }

            // 2. CLIENT SETUP
            val httpClient = createClient {
                install(io.ktor.client.plugins.websocket.WebSockets) {
                    contentConverter = KotlinxWebsocketSerializationConverter(jsonConfig)
                }
            }

            // Use backgroundScope so the connection job survives until test ends
            val realTimeClient = RealTimeClient(httpClient, backgroundScope)

            // 3. EXECUTE
            realTimeClient.connect("/orbital/device")

            // 4. ASSERT
            // Wait for the first non-empty emission
            val devices = realTimeClient.deviceState.first { it.isNotEmpty() }

            assertEquals(1, devices.size)
            assertEquals("light-1", devices[0].id)
            assertTrue((devices[0].state as DeviceState.OnOff).isOn)
        }
    }

//    @Test
//    fun `sendCommand sends correct JSON to server`() = testApplication {
//        // 1. MOCK SERVER (Echo Chamber)
//        application {
//            install(WebSockets) { contentConverter = KotlinxWebsocketSerializationConverter(jsonConfig) }
//            routing {
//                webSocket("/orbital/device") {
//                    // Receive command from client
//                    val frame = incoming.receive() as Frame.Text
//                    val text = frame.readText()
//                    // Echo it back via a trick or just log it?
//                    // In unit tests, we can throw assertion errors directly here!
//                    if (!text.contains("req-123")) error("Wrong Request ID")
//                }
//            }
//        }
//
//        // 2. CLIENT SETUP
//        val httpClient = createClient {
//            install(io.ktor.client.plugins.websocket.WebSockets) {
//                contentConverter = KotlinxWebsocketSerializationConverter(jsonConfig)
//            }
//        }
//        val realTimeClient = RealTimeClient(httpClient, backgroundScope)
//
//        realTimeClient.connect("/orbital/device")
//
//        // Wait for connection to be ready
//        realTimeClient.connectionStatus.first { it is ConnectionState.Connected }
//
//        // 3. EXECUTE
//        val cmd = ClientMessage.Command(
//            requestId = "req-123",
//            deviceId = "light-1",
//            newState = DeviceState.OnOff(false)
//        )
//        realTimeClient.sendCommand(cmd)
//
//        // 4. ASSERT happens inside the server block above or by checking no exceptions were thrown
//    }
//
//    @Test
//    fun `Edge Case - garbage data emits UiError ParsingError`() = testApplication {
//        application {
//            install(WebSockets)
//            routing {
//                webSocket("/orbital/device") {
//                    // Send Garbage
//                    send(Frame.Text("THIS IS NOT JSON"))
//                    for(f in incoming) {}
//                }
//            }
//        }
//
//        val httpClient = createClient {
//            install(io.ktor.client.plugins.websocket.WebSockets) {
//                contentConverter = KotlinxWebsocketSerializationConverter(jsonConfig)
//            }
//        }
//        val realTimeClient = RealTimeClient(httpClient, backgroundScope)
//        realTimeClient.connect("/orbital/device")
//
//        // ASSERT
//        val error = realTimeClient.uiErrors.first()
//        assertTrue(error is UiError.ParsingError)
//        assertEquals("THIS IS NOT JSON", (error as UiError.ParsingError).rawMessage)
//    }
//
//    @Test
//    fun `Edge Case - server rejection emits UiError CommandRejected`() = testApplication {
//        application {
//            install(WebSockets) { contentConverter = KotlinxWebsocketSerializationConverter(jsonConfig) }
//            routing {
//                webSocket("/orbital/device") {
//                    // Send a "Failure ACK" immediately
//                    val nack = ServerMessage.CommandAck(
//                        requestId = "req-bad",
//                        success = false,
//                        errorCode = ErrorCode.DEVICE_OFFLINE,
//                        message = "Device unplugged"
//                    )
//                    send(Frame.Text(jsonConfig.encodeToString<ServerMessage>(nack)))
//                    for(f in incoming) {}
//                }
//            }
//        }
//
//        val httpClient = createClient {
//            install(io.ktor.client.plugins.websocket.WebSockets) {
//                contentConverter = KotlinxWebsocketSerializationConverter(jsonConfig)
//            }
//        }
//        val realTimeClient = RealTimeClient(httpClient, backgroundScope)
//        realTimeClient.connect("/orbital/device")
//
//        // ASSERT
//        val error = realTimeClient.uiErrors.first()
//        assertTrue(error is UiError.CommandRejected)
//        assertEquals("Device unplugged", (error as UiError.CommandRejected).message)
//    }
}