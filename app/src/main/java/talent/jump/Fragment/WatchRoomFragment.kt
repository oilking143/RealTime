package talent.jump.Fragment

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import talent.jump.R
import talent.jump.databinding.WatchroomFragmentBinding
import talent.jump.utility.PlayerViewModel


class WatchRoomFragment: BaseFragment(){

    private var _binding: WatchroomFragmentBinding? = null
    private val binding get() = _binding!!
    private val url = "rtmp://stream.purelive.xyz/live/test1"
    private lateinit var viewModel: PlayerViewModel
    private lateinit var player: SimpleExoPlayer
    val TAG="TestLog"
    var streamFrag=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WatchroomFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePlayer()
        playVideo()
        player.playWhenReady = true
        player.prepare()
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
    }


    private fun initializePlayer() {

        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()

        val trackSelector = DefaultTrackSelector(
            requireContext(),
            videoTrackSelectionFactory
        )
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())

        player = SimpleExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector)
            .setBandwidthMeter(DefaultBandwidthMeter.getSingletonInstance(requireContext()))
            .build()

        binding.surfaceView.player = player
        binding.surfaceView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
        val diapatcher=MyDefaultControlDispatcher()
        binding.surfaceView.setControlDispatcher(diapatcher)
    }

    private fun playVideo() {
        val videoSource = ProgressiveMediaSource.Factory(RtmpDataSourceFactory())
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
        player.setMediaSource(videoSource)
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

    inner  class MyDefaultControlDispatcher : DefaultControlDispatcher() {
        override fun dispatchSetPlayWhenReady(player: Player, playWhenReady: Boolean): Boolean {
            super.dispatchSetPlayWhenReady(player, playWhenReady)


            if (playWhenReady && player.playbackState == Player.STATE_READY) {
                player.playWhenReady = true
            }
            else if(player.playbackState==Player.STATE_ENDED)
            {
                AlertDialog.Builder(requireContext())
                    .setMessage("主播已斷開連結")
                    .setTitle("房間斷訊")
                    .show()
            }
            return true
        }
    }

}