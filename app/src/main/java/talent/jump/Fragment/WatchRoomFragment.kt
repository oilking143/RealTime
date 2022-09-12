package talent.jump.Fragment

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import talent.jump.Events.*
import talent.jump.GlobalData
import talent.jump.R
import talent.jump.databinding.WatchroomFragmentBinding
import talent.jump.model.ApiTokenClient
import talent.jump.utility.EchoWebSocketListener
import talent.jump.utility.PlayerViewModel


class WatchRoomFragment: BaseFragment(){

    private var _binding: WatchroomFragmentBinding? = null
    private val binding get() = _binding!!
    var url:String = ""
    var stId:String = ""
    var connId:String = ""
    var roomname:String = ""
    var username:String = ""
    private lateinit var viewModel: PlayerViewModel
    private lateinit var player: ExoPlayer
    var mClient = OkHttpClient()
    val TAG="TestLog"
    var apiclient=ApiTokenClient()
    var streamFrag=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WatchroomFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        releaseResources()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        arguments?.let {
            url = it.getString("pullstream")!!
            stId = it.getString("stId")!!
            roomname = it.getString("roomname")!!
            username = it.getString("username")!!

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startSocket()
        initializePlayer()


        player.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean,playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        Log.d(TAG, "onPlayerStateChanged: STATE_IDLE")
                    }
                    Player.STATE_BUFFERING -> {
//                        AlertDialog.Builder(requireContext())
//                            .setMessage("主播已斷開連結")
//                            .setTitle("房間斷訊")
//                            .setNegativeButton("了解"
//                            ) { p0, p1 -> activity!!.supportFragmentManager.popBackStack() }
//                            .show()
                    }
                    Player.STATE_READY -> {
                        Log.d(TAG, "onPlayerStateChanged: STATE_READY")
                    }
                    Player.STATE_ENDED -> {
                        Log.d(TAG, "onPlayerStateChanged: STATE_ENDED")
                    }
                }
            }
        })

        binding.msgSend.setOnClickListener {

            val msgJson=JsonObject()
            msgJson.addProperty("type",0)
            msgJson.addProperty("content","${binding.pushMsg.text}")
          apiclient.CreateMsg(stId,msgJson)
        }

        binding.hostname.setText(username)
        binding.roomName.setText("房名:${roomname}")

        binding.heartBtn.setOnClickListener {
            Log.d("ConnId",connId)

            val heartJson= JsonObject()
            heartJson.addProperty("event","join_stream")
            heartJson.addProperty("conn_id",connId)
            heartJson.addProperty("pass_code","abc123")
            apiclient.giveHearts(stId,heartJson)

        }

    }

    fun startSocket() {
        val request: Request = Request.Builder()
            .addHeader("Authorization", GlobalData.loginToken.access_token)
            .addHeader("x-us-platform","2")
            .url("wss://api.minilive.xyz/api/websocket?conn_id=").build()
        val listener = EchoWebSocketListener()
        val webSocket: WebSocket = mClient!!.newWebSocket(request, listener)
//        mClient.dispatcher.executorService.shutdown()

        val connJson=JsonObject()
        val dataJson=JsonObject()
        connJson.addProperty("event","auth")
        dataJson.addProperty("access_token",GlobalData.loginToken.access_token)
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
    fun onHeartEvent(event: HeartEvent?) {

        val amTranslate: Animation = TranslateAnimation(0.0f, getRandom(-6000,1000), 0.0f, -10000.0f)
        amTranslate.duration = 3000
        amTranslate.repeatCount = 0



        val amAlpha: Animation = AlphaAnimation(1.0f, 0.0f)
        amAlpha.duration = 1500
        amAlpha.repeatCount = 0

        val amSet = AnimationSet(false)
        amSet.addAnimation(amTranslate)
        amSet.addAnimation(amAlpha)

        val imageView = ImageView(context)
        imageView.id = View.generateViewId()
        imageView.setImageResource(R.drawable.icon_heart)
        binding.motherLayout.addView(imageView)
        val set = ConstraintSet()
        set.clone(binding.motherLayout)
        set.connect(imageView.id, ConstraintSet.END, R.id.animator, ConstraintSet.END)
        set.connect(imageView.id, ConstraintSet.BOTTOM, R.id.animator, ConstraintSet.BOTTOM)
        set.connect(imageView.id, ConstraintSet.START, R.id.animator, ConstraintSet.START)
        set.connect(imageView.id, ConstraintSet.TOP, R.id.animator, ConstraintSet.TOP)
        set.applyTo(binding.motherLayout)

        imageView.startAnimation(amSet)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetmsgEvent(event: msgEvent?) {

        if(event!!.getWsMsg().contains("conn_id"))
        {
            var socketJson=JSONObject(event!!.getWsMsg())
            val viewerJson= JsonObject()
            connId=socketJson.getJSONObject("data").get("conn_id").toString()
            viewerJson.addProperty("event","join_stream")
            viewerJson.addProperty("conn_id",socketJson.getJSONObject("data").get("conn_id").toString())
            viewerJson.addProperty("pass_code","abc123")
            apiclient.getSocketViewer(stId,viewerJson)
        }
        else
        {
            Log.d("Socket",event!!.getWsMsg())
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onViewerEvent(event: ViewerEvent?) {

        Log.d("urlEvent",event!!.getViewerResponse().data.push)
        url = event!!.getViewerResponse().data.pull
        arguments?.let {
            stId = it.getString("stId")!!
        }
        roomname=event!!.getViewerResponse().data.display_name
        playVideo()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChatEvent(event: GetVerifyMailEvent?) {

        if(event!!.GetVerifyMailData().status)
        {
            binding.pushMsg.text.clear()

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorEvent(event: errorEvent?) {

        Log.d("event_look",event!!.getMsg())

        if(event!!.getMsg()=="Forbidden")
        {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }


    private fun initializePlayer() {

        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()

        val trackSelector = DefaultTrackSelector(
            requireContext(),
            videoTrackSelectionFactory
        )
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())
        player = ExoPlayer.Builder(requireContext()).build()
        binding.surfaceView.player = player

    }

    private fun playVideo() {

        val item: MediaItem = MediaItem.Builder()
            .setUri(url)
            .setMimeType(MimeTypes.AUDIO_AAC)
            .build()
        player.setMediaItem(item)
        player.prepare()
        player.play()
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

            holder.message.text="test$position"

        }

        override fun getItemCount(): Int {
        return 5
        }

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            val message=itemView.findViewById<TextView>(R.id.message_item)

        }

    }

    private fun releaseResources() {
        if (player != null) {
            if (player.isPlaying) {
                player.stop()
            }
            player.release()

        }
    }

}