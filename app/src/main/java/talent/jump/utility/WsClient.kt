package talent.jump.utility

import android.util.Log
import okhttp3.*
import okio.ByteString
import talent.jump.GlobalData

class WsClient(
    private val client: OkHttpClient,
    private val onReceive: (input: String) -> Unit
) {
    var session: WebSocket? = null

    fun connect() {
        val request = Request.Builder()
            .addHeader("Authorization",GlobalData.loginToken.access_token)
            .addHeader("x-us-platform","2")
            .url("wss://api.asiamedia.xyz/api/websocket?conn_id=/wss")
            .build()
        client.newWebSocket(request, object: WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                session = webSocket
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                onReceive(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            }

            override fun onClosing(
                webSocket: WebSocket,
                code: Int,
                reason: String
            ) {
            }

            override fun onClosed(
                webSocket: WebSocket,
                code: Int,
                reason: String
            ) {
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                     Log.d("Error",response!!.message)
            }
        })
    }


    fun send(message: String) {
        session?.send(message)
    }
}