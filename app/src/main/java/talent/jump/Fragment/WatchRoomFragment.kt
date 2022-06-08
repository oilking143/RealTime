package talent.jump.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import talent.jump.Events.GetVerifyMailEvent
import talent.jump.Events.msgEvent
import talent.jump.GlobalData
import talent.jump.R
import talent.jump.databinding.WatchroomFragmentBinding
import talent.jump.model.ApiTokenClient
import talent.jump.utility.EchoWebSocketListener
import talent.jump.utility.PlayerViewModel


class WatchRoomFragment: BaseFragment(){

    private var _binding: WatchroomFragmentBinding? = null
    private val binding get() = _binding!!
    val args by navArgs<WatchRoomFragmentArgs>()
    var url:String = ""
    var stId:String = ""
    var name:String = ""
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
        url = args.pullstream
        stId=args.stId
        name=args.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startSocket()
        initializePlayer()
        playVideo()

        player.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean,playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        Log.d(TAG, "onPlayerStateChanged: STATE_IDLE")
                    }
                    Player.STATE_BUFFERING -> {
                        AlertDialog.Builder(requireContext())
                            .setMessage("主播已斷開連結")
                            .setTitle("房間斷訊")
                            .setNegativeButton("了解"
                            ) { p0, p1 -> activity!!.supportFragmentManager.popBackStack() }
                            .show()
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

        binding.hostname.setText(name)

    }

    fun startSocket() {
        val request: Request = Request.Builder()
            .addHeader("Authorization", GlobalData.loginToken.access_token)
            .addHeader("x-us-platform","2")
            .url("wss://api.asiamedia.xyz/api/websocket?conn_id=").build()
        val listener = EchoWebSocketListener()
        val webSocket: WebSocket = mClient!!.newWebSocket(request, listener)
//        mClient.dispatcher.executorService.shutdown()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetmsgEvent(event: msgEvent?) {

        if(event!!.getWsMsg().contains("conn_id"))
        {
            var socketJson=JSONObject(event!!.getWsMsg())
            val viewerJson= JsonObject()
            viewerJson.addProperty("event","join_stream")
            viewerJson.addProperty("conn_id",socketJson.getJSONObject("data").get("conn_id").toString())
            viewerJson.addProperty("pass_code","abc123")
            apiclient.getSocketViewer(stId,viewerJson)
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChatEvent(event: GetVerifyMailEvent?) {

        if(event!!.GetVerifyMailData().status)
        {
            binding.pushMsg.text.clear()

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