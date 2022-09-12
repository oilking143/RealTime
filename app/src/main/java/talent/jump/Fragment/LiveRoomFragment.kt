package talent.jump.Fragment

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import talent.jump.Events.CreateStreamEvent
import talent.jump.Events.ViewerEvent
import talent.jump.Events.msgEvent
import talent.jump.GlobalData
import talent.jump.R
import talent.jump.databinding.LiveroomFragmentBinding
import talent.jump.model.ApiTokenClient
import talent.jump.utility.EchoWebSocketListener
import talent.jump.utility.PlayerViewModel
import talent.jump.utility.TuberInterfaceDialog


class LiveRoomFragment: BaseFragment(), SurfaceHolder.Callback, ConnectCheckerRtmp {

    private var _binding: LiveroomFragmentBinding? = null
    private val binding get() = _binding!!
    private var ownerFlag: Boolean = false
    private var rtmpCamera1: RtmpCamera1? = null
    var streamFrag=false
    var url=""
    var connId:String = ""
    var stId:String = ""
    var mClient = OkHttpClient()
    private lateinit var viewModel: PlayerViewModel
//    val args by navArgs<LiveRoomFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LiveroomFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        var tube= activity?.let { TuberInterfaceDialog(it) }

            tube!!.show()
            binding.surfaceView.holder.addCallback(this)
            rtmpCamera1 = RtmpCamera1(binding.surfaceView, this)



    }

    private fun StartStream()
    {
        if (!rtmpCamera1!!.isStreaming) {
//            binding.startStream.text = "Stop"

            if (rtmpCamera1!!.isRecording || prepareEncoders()) {
                Log.d("realurl",url)
                rtmpCamera1!!.startStream(url)
                rtmpCamera1!!.switchCamera()
            } else {
                Toast.makeText(
                    context, "Error preparing stream, This device cant do it",
                    Toast.LENGTH_SHORT
                ).show()
//                binding.startStream.text = "Start"
            }
        } else {
//            binding.startStream.text = "Start"
            rtmpCamera1!!.stopStream()
        }
    }

    private fun prepareEncoders(): Boolean {
        val resolution = rtmpCamera1!!.resolutionsBack[0]
        val width = resolution.width
        val height = resolution.height
        return rtmpCamera1!!.prepareVideo(
            width, height, 30,
            2500 * 1024,
            CameraHelper.getCameraOrientation(context)
        ) && rtmpCamera1!!.prepareAudio(
            128 * 1024,
           44100,
            true,
            false,
            false
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    inner class messageAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_message_view, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var holder=holder as ViewHolder

            holder.message.text="testestestdwlkfmgjlk;gjgw;l__$position"

        }

        override fun getItemCount(): Int {
        return 5
        }

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            val message=itemView.findViewById<TextView>(R.id.message_item)

        }

    }


    private fun startSocket() {
        val request: Request = Request.Builder()
            .addHeader("Authorization", GlobalData.loginToken.access_token)
            .addHeader("x-us-platform","2")
            .url("wss://api.minilive.xyz/api/websocket?conn_id=").build()
        val listener = EchoWebSocketListener()
        val webSocket: WebSocket = mClient!!.newWebSocket(request, listener)
//        mClient.dispatcher.executorService.shutdown()

        val connJson= JsonObject()
        val dataJson= JsonObject()
        connJson.addProperty("event","auth")
        dataJson.addProperty("access_token", GlobalData.loginToken.access_token)
        dataJson.addProperty("conn_id","")
        dataJson.addProperty("sequence","0")
        connJson.add("data",dataJson)
        Log.d("authdata",connJson.toString())
        webSocket.send(connJson.toString())

        object : CountDownTimer(30000, 1000) {

            override fun onFinish() {
                webSocket.send("{\n" +
                        "    \"event\": \"ping\"\n" +
                        "}")
                start()
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun CreateStreamEvent(event: CreateStreamEvent?) {

       Log.d("event_renew",event!!.GetStreamResponse().data.id)
        stId=event!!.GetStreamResponse().data.id
        startSocket()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetmsgEvent(event: msgEvent?) {

        if(event!!.getWsMsg().contains("conn_id"))
        {
            var socketJson= JSONObject(event!!.getWsMsg())
            val viewerJson= JsonObject()
            connId=socketJson.getJSONObject("data").get("conn_id").toString()
            viewerJson.addProperty("event","join_stream")
            viewerJson.addProperty("conn_id",connId)
            viewerJson.addProperty("pass_code","abc123")
            ApiTokenClient().getSocketViewer(stId,viewerJson)
        }
        else
        {
            Log.d("Socket",event!!.getWsMsg())
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onViewerEvent(event: ViewerEvent?) {

        url = event!!.getViewerResponse().data.push
        StartStream()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        rtmpCamera1!!.startPreview()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

        rtmpCamera1!!.stopPreview()
    }

    override fun onAuthErrorRtmp() {
        TODO("Not yet implemented")
    }

    override fun onAuthSuccessRtmp() {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailedRtmp(reason: String) {


    }

    override fun onConnectionStartedRtmp(rtmpUrl: String) {
    }

    override fun onConnectionSuccessRtmp() {
        requireActivity().runOnUiThread {
            Toast.makeText(
                context,
                "Connection success",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDisconnectRtmp() {

    }

    override fun onNewBitrateRtmp(bitrate: Long) {

    }



}