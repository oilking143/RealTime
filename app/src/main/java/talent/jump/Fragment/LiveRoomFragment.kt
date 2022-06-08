package talent.jump.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import talent.jump.R
import talent.jump.databinding.LiveroomFragmentBinding
import talent.jump.utility.PlayerViewModel
import talent.jump.utility.TuberInterfaceDialog


class LiveRoomFragment: BaseFragment(), SurfaceHolder.Callback, ConnectCheckerRtmp {

    private var _binding: LiveroomFragmentBinding? = null
    private val binding get() = _binding!!
    private var ownerFlag: Boolean = false
    private var rtmpCamera1: RtmpCamera1? = null
    var streamFrag=false
    var url=""
    private lateinit var viewModel: PlayerViewModel
    val args by navArgs<LiveRoomFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LiveroomFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        var tube= context?.let { TuberInterfaceDialog(it) }

            tube!!.show()
            binding.surfaceView.holder.addCallback(this)
            rtmpCamera1 = RtmpCamera1(binding.surfaceView, this)
            binding.startStream.setOnClickListener {

                if (!rtmpCamera1!!.isStreaming) {
                    binding.startStream.text = "Stop"

                    if (rtmpCamera1!!.isRecording || prepareEncoders()) {
                        rtmpCamera1!!.startStream(url)
                        rtmpCamera1!!.switchCamera()
                    } else {
                        Toast.makeText(
                            context, "Error preparing stream, This device cant do it",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.startStream.text = "Start"
                    }
                } else {
                    binding.startStream.text = "Start"
                    rtmpCamera1!!.stopStream()
                }
            }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        url=args.pushstream
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
        TODO("Not yet implemented")
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