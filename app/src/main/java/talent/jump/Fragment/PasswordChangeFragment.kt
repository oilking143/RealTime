package talent.jump.Fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Events.UpdateEvent
import talent.jump.R
import talent.jump.databinding.FragmentPasswordChangeBinding
import talent.jump.model.ApiTokenClient

class PasswordChangeFragment:Fragment() {
    private var _binding: FragmentPasswordChangeBinding? = null
    private val binding get() = _binding!!
    private  var oldflag=false
    private  var newflag=false
    private var apiclient=ApiTokenClient()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordChangeBinding.inflate(inflater, container, false)
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

        binding.codeIconOld.setOnClickListener {

            if(oldflag)
            {
                binding.codeIconOld.setImageResource(R.drawable.eye_invisible)
                binding.passwordOld.inputType=InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            else
            {
                binding.codeIconOld.setImageResource(R.drawable.eye_open)
                binding.passwordOld.inputType=InputType.TYPE_CLASS_TEXT
            }

        }

        binding.codeIconNew.setOnClickListener {

            if (newflag) {
                binding.codeIconNew.setImageResource(R.drawable.eye_invisible)
                binding.passwordOld.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                binding.codeIconNew.setImageResource(R.drawable.eye_open)
                binding.passwordOld.inputType = InputType.TYPE_CLASS_TEXT
            }

        }

        binding.changeBtn.setOnClickListener {


            val updateJson=JsonObject()
            updateJson.addProperty("old_password","${binding.passwordOld.text}")
            updateJson.addProperty("new_password","${binding.passwordNew.text}")
            apiclient.UpdatePassword(updateJson)
        }

        binding.backIcon.setOnClickListener {

            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetUserEvent(event: UpdateEvent?) {


        if(event!!.getUpdate().status)
        {
            object : CountDownTimer(3000, 1000) {

                override fun onFinish() {
                    binding.verifyLetterToast.visibility=View.GONE
                    requireActivity().supportFragmentManager.popBackStack()
                }

                override fun onTick(millisUntilFinished: Long) {
                    binding.verifyLetterToast.visibility=View.VISIBLE
                    binding.verifyLetterInfo.text=resources.getString(R.string.password_success)
                    binding.verifyLetterIcon.setImageResource(R.drawable.mail_toast_icon)
                }
            }.start()
        }
    }
}