package talent.jump.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Activity.MainActivity
import talent.jump.Events.ForgetPassEvent
import talent.jump.Events.GetVerifyMailEvent
import talent.jump.Events.LoginEvent
import talent.jump.Events.ResetEvent
import talent.jump.GlobalData
import talent.jump.R
import talent.jump.databinding.FragmrntPasswordConfirmBinding
import talent.jump.model.ApiClient
import talent.jump.model.ApiTokenClient

class PasswordConfirmFragment:Fragment() {
    private var _binding: FragmrntPasswordConfirmBinding? = null
    private val binding get() = _binding!!
    var apiClient= ApiClient()
    var apiToken= ApiTokenClient()
    private var emaillabel=false
    private var verifylabel=false
    private var passwordlabel=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmrntPasswordConfirmBinding.inflate(inflater, container, false)
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


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backIcon.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.getConfirmBtn.setOnClickListener{

            if(binding.emailEnterBox.text.isNotEmpty())
            {

                val ForgetJson= JsonObject()
                ForgetJson.addProperty("email","${binding.emailEnterBox.text}")
                apiClient.frogetPassword(ForgetJson)

                object : CountDownTimer(3000, 1000) {

                    override fun onFinish() {
                        binding.verifyLetterToast.visibility=View.GONE
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        binding.verifyLetterToast.visibility=View.VISIBLE
                        binding.verifyLetterInfo.text=resources.getString(R.string.prepare_mail)
                        binding.verifyLetterIcon.setImageResource(R.drawable.mail_toast_icon)
                    }
                }.start()
            }
            else
            {
                object : CountDownTimer(3000, 1000) {

                    override fun onFinish() {
                        binding.verifyLetterToast.visibility=View.GONE
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        binding.verifyLetterToast.visibility=View.VISIBLE
                        binding.verifyLetterInfo.text=resources.getString(R.string.mail_fail)
                        binding.verifyLetterIcon.setImageResource(R.drawable.mail_fail)
                    }
                }.start()

            }


        }

        binding.emailEnterBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             }

            override fun afterTextChanged(s: Editable?) {
                emaillabel = s!!.length>1
                checklabels()
             }

        })

        binding.confirmBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                verifylabel = s!!.length>1
                checklabels()
            }

        })

        binding.newPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                passwordlabel = s!!.length>1
                checklabels()
            }

        })

    }

    private fun checklabels() {

        if(emaillabel && verifylabel && passwordlabel)
        {
            binding.nextstepBtn.isEnabled=true
            binding.nextstepBtn.setBackgroundResource(R.drawable.login_confirm_btn_frame_enable)
            binding.nextstepBtn.setOnClickListener {
                val updateJson= JsonObject()
                updateJson.addProperty("email","${binding.emailEnterBox.text}")
                updateJson.addProperty("code","${binding.confirmBox.text}")
                apiClient.updatePassword(updateJson)

                object : CountDownTimer(3000, 1000) {

                    override fun onFinish() {
                        binding.verifyLetterToast.visibility=View.GONE
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        binding.verifyLetterToast.visibility=View.VISIBLE
                        binding.verifyLetterInfo.text=resources.getString(R.string.prepare_mail)
                        binding.verifyLetterIcon.setImageResource(R.drawable.mail_toast_icon)
                    }
                }.start()
            }
        }
        else
        {
            binding.nextstepBtn.isEnabled=false
            binding.nextstepBtn.setOnClickListener(null)
            binding.nextstepBtn.setBackgroundResource(R.drawable.login_confirm_btn_frame_disable)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onForgetPassEvent(event: ForgetPassEvent?) {

        if(event!!.GetforgetLetter().status)
        {
            binding.getConfirmBtn.setBackgroundResource(R.drawable.login_getconfirm_btn_frame_pass)
            binding.getConfirmBtn.setTextColor(Color.parseColor("#42D248"))
            binding.getConfirmBtn.text=resources.getString(R.string.access_success)

            object : CountDownTimer(3000, 1000) {

                override fun onFinish() {
                    binding.verifyLetterToast.visibility=View.GONE
                }

                override fun onTick(millisUntilFinished: Long) {
                    binding.verifyLetterToast.visibility=View.VISIBLE
                    binding.verifyLetterInfo.text=resources.getString(R.string.mail_success)
                    binding.verifyLetterIcon.setImageResource(R.drawable.mail_toast_icon)
                }
            }.start()
        }
        else
        {
            object : CountDownTimer(3000, 1000) {

                override fun onFinish() {
                    binding.verifyLetterToast.visibility=View.GONE
                }

                override fun onTick(millisUntilFinished: Long) {
                    binding.verifyLetterToast.visibility=View.VISIBLE
                    binding.verifyLetterInfo.text=resources.getString(R.string.access_fail)
                    binding.verifyLetterIcon.setImageResource(R.drawable.mail_fail)
                }
            }.start()
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent?) {

        if(event!!.GetLoginData().status)
        {
            val data=event!!.GetLoginData().data
            GlobalData.loginToken=data


            Log.d("globaldata",GlobalData.loginToken.access_token)
//            val intent = Intent(activity, MainActivity::class.java)
//            startActivity(intent)
            val resetJson= JsonObject()
            resetJson.addProperty("password","${binding.newPassword.text}")
            apiToken.ResrtPassword(resetJson)

        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onResetEvent(event: ResetEvent?) {

        if(event!!.GetLoginData().status)
        {
            val intent = Intent(activity, LoginEvent::class.java)
            startActivity(intent)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
    }

}