package talent.jump.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Activity.MainActivity
import talent.jump.Events.LoginEvent
import talent.jump.Events.RegisterEvent
import talent.jump.GlobalData
import talent.jump.R
import talent.jump.databinding.WindowFragmentBinding
import talent.jump.model.ApiClient
import talent.jump.utility.CodeUtilites

class WindowFragment: BaseFragment()  {
    private var _binding: WindowFragmentBinding? = null
    private val binding get() = _binding!!
    private var isShow=false
    private var isShowConfirm=false
    var apiClient=ApiClient()
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

        binding.showConfirmPassword.setOnClickListener {
            if(isShowConfirm)
            {
                isShowConfirm=false
                binding.showConfirmPassword.setBackgroundResource(R.drawable.eye_invisible)
                binding.passwordConfirm.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            else
            {
                isShowConfirm=true
                binding.showConfirmPassword.setBackgroundResource(R.drawable.eye_open)
                binding.passwordConfirm.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }

        }

        binding.showPassword.setOnClickListener {

            if(isShow)
            {
                isShow=false
                binding.showPassword.setBackgroundResource(R.drawable.eye_invisible)
                binding.passwordRegister.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            else
            {
                isShow=true
                binding.showPassword.setBackgroundResource(R.drawable.eye_open)
                binding.passwordRegister.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }

        }


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

        val codwUtility=CodeUtilites().getInstance()



        binding.codeIcon.setImageBitmap(codwUtility!!.createBitmap())

        binding.codeIcon.setOnClickListener {

            binding.codeIcon.setImageBitmap(codwUtility!!.createBitmap())
        }

        binding.loginBtn.setOnClickListener {

            val entry=binding.entryCode.text
            if(codwUtility!!.getCode()=="$entry")
            {
                val LoginJson= JsonObject()
                LoginJson.addProperty("Account",binding.usernameLogin.text.toString())
                LoginJson.addProperty("Password",md5(binding.passwordLogin.text.toString()))
                apiClient.Login(LoginJson)

            }
            else
            {
                Toast.makeText(context,"認證碼錯誤",Toast.LENGTH_LONG).show()
            }

        }


        binding.registerBtn.setOnClickListener {

            val registerJson=JsonObject()
            registerJson.addProperty("promotecode","mudb")
            registerJson.addProperty("account",binding.usernameRegister.text.toString())
            registerJson.addProperty("nickname","")
            registerJson.addProperty("password",binding.passwordRegister.text.toString())
            registerJson.addProperty("confirmpassword",binding.passwordConfirm.text.toString())
            registerJson.addProperty("mobile","")
            registerJson.addProperty("email",binding.usernameRegister.text.toString())
            registerJson.addProperty("returnurl","")
            apiClient.Register(registerJson)

        }

     binding.forgetPassword.setOnClickListener {
         findNavController().navigate(R.id.action_windowFragment_to_passwordConfirmFragment)
     }


    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent?) {


        if(event!!.GetLoginData().code==0)
        {
            if(pref.getString("Account", "")=="")
            {
                pref.edit().putString("Account",binding.usernameLogin.text.toString())
                pref.edit().putString("Password",binding.passwordLogin.text.toString())
            }
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        else
        {
           when(event!!.GetLoginData().code)
           {
               1001->{
                   Toast.makeText(context,"帳號或密碼錯誤",Toast.LENGTH_LONG).show()
               }
           }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRegisterEvent(event: RegisterEvent?) {


        Log.d("code","${event!!.GetRegisterData().code}")

        if (event!!.GetRegisterData().code==0)
        {
            Toast.makeText(context,"註冊成功",Toast.LENGTH_LONG).show()

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        else
        {
            when(event!!.GetRegisterData().code)
            {
                1004,1001->{
                    Toast.makeText(context,"此帳號已有人使用",Toast.LENGTH_LONG).show()
                }
            }

        }
    }


}