package talent.jump.Fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.squareup.moshi.Json
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import talent.jump.Events.SingleEvent
import talent.jump.databinding.FragmrntEditPersonalBinding
import talent.jump.model.ApiTokenClient


class PersonalEditFragment:BaseFragment() {
    private var _binding: FragmrntEditPersonalBinding? = null
    private val binding get() = _binding!!
    private var apiclient= ApiTokenClient()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmrntEditPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onStart() {
//        super.onStart()
//        EventBus.getDefault().register(this)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        EventBus.getDefault().unregister(this)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.getVerify.setOnClickListener{
            apiclient.VerifyPersonalMailSend()
        }

        binding.backIcon.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()

        }



    }



}