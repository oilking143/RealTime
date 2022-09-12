package talent.jump.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Activity.MainActivity
import talent.jump.Events.*
import talent.jump.GlobalData
import talent.jump.R
import talent.jump.model.ApiClient
import talent.jump.databinding.WindowFragmentBinding
import talent.jump.model.ApiTokenClient
import talent.jump.utility.CodeUtilites

class WindowFragment: BaseFragment()  {
    private var _binding: WindowFragmentBinding? = null
    private val binding get() = _binding!!
    private var isShow=false
    private var isShowConfirm=false
    var apiClient=ApiClient()
    private var emaillabel=false
    private var verifylabel=false
    private var nicknamelabel=false
    private var accountlabel=false
    private var passwordlabel=false
    private var useridlabel=false
    private var regpasslabel=false
    lateinit var pref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WindowFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        pref= requireContext().getSharedPreferences("profile", Context.MODE_PRIVATE)
        val account=pref.getString("Account", "")
        if(account!="")
        {
            binding.usernameLogin.setText(pref.getString("Account", ""))
            binding.passwordLogin.setText(pref.getString("Password", ""))
        }


        binding.inviteBtn.setOnClickListener{

            if(binding.emailEnterBox.text.isNotEmpty())
            {

                val VerifyJson= JsonObject()
                VerifyJson.addProperty("email","${binding.emailEnterBox.text}")
                apiClient.verifyMail(VerifyJson)

                object : CountDownTimer(1000, 1000) {

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
            override fun afterTextChanged(s: Editable?) {
                emaillabel = s!!.length>1
                checklabels()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.verifyCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                verifylabel = s!!.length>1
                checklabels()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.nickName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                nicknamelabel = s!!.length>1
                checklabels()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.userIdBox.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                useridlabel = s!!.length>1
                checklabels()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.passwordBox.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                regpasslabel = s!!.length>1
                checklabels()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        binding.login.setOnClickListener {

            binding.login.setTypeface(binding.login.typeface, Typeface.BOLD)
            binding.login.setTextColor(Color.parseColor("#3A3A3A"))
            binding.register.setTypeface(binding.register.typeface, Typeface.NORMAL)
            binding.register.setTextColor(Color.parseColor("#868686"))
            binding.loginPageOn.visibility=View.VISIBLE
            binding.registerPageOn .visibility=View.GONE
            binding.registerPageFrame.visibility=View.GONE
            binding.loginPageFrame.visibility=View.VISIBLE
        }

        binding.register.setOnClickListener {

            binding.register.setTypeface(binding.register.typeface, Typeface.BOLD)
            binding.register.setTextColor(Color.parseColor("#3A3A3A"))
            binding.login.setTypeface(binding.login.typeface, Typeface.NORMAL)
            binding.login.setTextColor(Color.parseColor("#868686"))
            binding.registerPageOn.visibility=View.VISIBLE
            binding.loginPageOn.visibility=View.GONE
            binding.registerPageFrame.visibility=View.VISIBLE
            binding.loginPageFrame.visibility=View.GONE

        }

//        val codwUtility=CodeUtilites().getInstance()
//
//
//
//        binding.codeIcon.setImageBitmap(codwUtility!!.createBitmap())
//
//        binding.codeIcon.setOnClickListener {
//
//            binding.codeIcon.setImageBitmap(codwUtility!!.createBitmap())
//        }

        binding.usernameLogin.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                accountlabel = s!!.length>1
                checklabels()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.passwordLogin.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                passwordlabel = s!!.length>1
                checklabels()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })




     binding.forgetPassword.setOnClickListener {
         findNavController().navigate(R.id.action_windowFragment_to_passwordConfirmFragment)
     }


    }

    private fun checklabels() {

        if(emaillabel && verifylabel && nicknamelabel)
        {
            binding.nextstepBtn.setBackgroundResource(R.drawable.login_confirm_btn_frame_enable)
            binding.nextstepBtn.isEnabled=true

            binding.nextstepBtn.setOnClickListener {

                val verifyCodeJson= JsonObject()
                verifyCodeJson.addProperty("email",binding.emailEnterBox.text.toString())
                verifyCodeJson.addProperty("code",binding.verifyCode.text.toString().trim())
                apiClient.verifyMailCode(verifyCodeJson)
            }
        }
        else
        {
            binding.nextstepBtn.setBackgroundResource(R.drawable.login_confirm_btn_frame_disable)
            binding.nextstepBtn.isEnabled=false
            binding.nextstepBtn.setOnClickListener (null)
        }

        if(passwordlabel && accountlabel)
        {
            binding.loginBtn.setBackgroundResource(R.drawable.login_confirm_btn_frame_enable)
            binding.loginBtn.isEnabled=true
            binding.loginBtn.setOnClickListener {

                val LoginJson= JsonObject()
                LoginJson.addProperty("username","${binding.usernameLogin.text}")
                LoginJson.addProperty("password","${binding.passwordLogin.text}")
                LoginJson.addProperty("grant_type","password")
                apiClient.Login(LoginJson)

            }
        }
        else
        {
            binding.loginBtn.setBackgroundResource(R.drawable.login_confirm_btn_frame_disable)
            binding.loginBtn.isEnabled=false
            binding.loginBtn.setOnClickListener(null)
        }


        if(regpasslabel && useridlabel)
        {
            binding.registerBtn.setBackgroundResource(R.drawable.login_confirm_btn_frame_enable)
            binding.registerBtn.isEnabled=true
            binding.registerBtn.setOnClickListener {

                val RegisterJson= JsonObject()
                RegisterJson.addProperty("email","${binding.emailEnterBox.text}")
                RegisterJson.addProperty("username","${binding.userIdBox.text}")
                RegisterJson.addProperty("nickname","${binding.nickName.text}")
                RegisterJson.addProperty("password","${binding.passwordBox.text}")
                ApiTokenClient().Register(RegisterJson)
            }
        }
        else
        {
            binding.registerBtn.setBackgroundResource(R.drawable.login_confirm_btn_frame_disable)
            binding.registerBtn.isEnabled=false
            binding.registerBtn.setOnClickListener(null)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent?) {

        if(event!!.GetLoginData().status)
        {
            val data=event!!.GetLoginData().data
            GlobalData.loginToken=data
            GlobalData.refresh_token=data.refresh_token
            GlobalData.access_token=data.token_type
            GlobalData.expire_at=data.expire_at
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onVerifyMailCodeEvent(event: GetVerifyMailCodeEvent?) {

        Log.d("eventCode",event!!.GetVerifyMailCodeData().data.access_token)
        if(event!!.GetVerifyMailCodeData().status)
        {
            val data=event!!.GetVerifyMailCodeData().data
            GlobalData.loginToken=data
            GlobalData.access_token=data.access_token
            GlobalData.refresh_token=data.refresh_token
            GlobalData.access_token=data.token_type
            GlobalData.expire_at=data.expire_at
            binding.registerPage2Frame.visibility=View.VISIBLE
            binding.registerPageFrame.visibility=View.GONE
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
    fun onVerifyMailEvent(event: GetVerifyMailEvent?) {

        if(event!!.GetVerifyMailData().status)
        {
            binding.inviteBtn.setBackgroundResource(R.drawable.login_getconfirm_btn_frame_pass)
            binding.inviteBtn.setTextColor(Color.parseColor("#42D248"))
            binding.inviteBtn.text=resources.getString(R.string.access_success)
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRegisterEvent(event: RegisterEvent?) {

        if(event!!.GetRegisterData().status)
        {
            val data=event!!.GetRegisterData().data
            GlobalData.access_token=data.access_token
            GlobalData.refresh_token=data.refresh_token
            GlobalData.access_token=data.token_type
            GlobalData.expire_at=data.expire_at
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        else
        {
            object : CountDownTimer(3000, 1000) {

                override fun onFinish() {
                    binding.verifyLetterToast.visibility=View.GONE
                }

                override fun onTick(millisUntilFinished: Long) {
                    binding.verifyLetterToast.visibility=View.VISIBLE
                    binding.verifyLetterInfo.text=resources.getString(R.string.register_fail)
                    binding.verifyLetterIcon.setImageResource(R.drawable.mail_fail)
                }
            }.start()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorEvent(event: errorEvent?) {
        object : CountDownTimer(3000, 1000) {

            override fun onFinish() {
                binding.verifyLetterToast.visibility=View.GONE
            }

            override fun onTick(millisUntilFinished: Long) {
                binding.verifyLetterToast.visibility=View.VISIBLE
               when(event!!.getMsg())
               {
                   "409"->{
                       binding.verifyLetterInfo.text="本帳號已經申請過了!"

                   }

                   else->{

                   }
               }
                binding.verifyLetterIcon.setImageResource(R.drawable.mail_fail)
            }
        }.start()
    }


}