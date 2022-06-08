package talent.jump.utility

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.greenrobot.eventbus.EventBus
import talent.jump.Events.msgEvent


public class EchoWebSocketListener : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {

    }

    override fun onMessage(webSocket: WebSocket, message: String) {
        EventBus.getDefault().post(msgEvent(message))
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {

        EventBus.getDefault().post(msgEvent("Receive Bytes : " + bytes.hex()))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(CLOSE_STATUS, null)
        EventBus.getDefault().post(msgEvent("Closing Socket : $code / $reason"))

    }

    override fun onFailure(webSocket: WebSocket, throwable: Throwable, response: Response?) {
        EventBus.getDefault().post(msgEvent(throwable.message.toString()))
    }

    companion object {
        private const val CLOSE_STATUS = 1000
    }
}