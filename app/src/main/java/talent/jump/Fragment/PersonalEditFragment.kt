package talent.jump.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Events.UpdateEvent
import talent.jump.R
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



        binding.backIcon.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()

        }

        binding.finishBtn.setOnClickListener {

            /***
             *
             * {
            "email": "",
            "nickname": "",
            "introduction": "嗨\n測試簡介",
            "code": ""
            }
             *
             */
            var updateJson=JsonObject()
            updateJson.addProperty("nickname","${binding.nickNameBox.text}")
            updateJson.addProperty("email","${binding.emailBox.text}")
            updateJson.addProperty("introduction","${binding.introduceEnterBox.text}")

            apiclient.UpdateUser(updateJson)
        }

        binding.passwordChg.setOnClickListener {

            findNavController().navigate(R.id.action_personalEditFragment_to_passwordChangeFragment)

        }



    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateEvent(event: UpdateEvent?) {


        if(event!!.getUpdate().status)
        {

           Toast.makeText(context,"資料修改成功",Toast.LENGTH_LONG).show()

        }
    }


}