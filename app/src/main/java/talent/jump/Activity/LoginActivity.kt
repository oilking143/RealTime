package talent.jump.Activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import talent.jump.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getpermission()
    }

    fun getpermission()
    {
        if ((ContextCompat.checkSelfPermission(this@LoginActivity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ||(ContextCompat.checkSelfPermission(this@LoginActivity,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
        {
            Log.e("--------->", "沒有許可權")
            //申請授權
            ActivityCompat.requestPermissions(this@LoginActivity,PERMISSIONS, 1)
        }
        else
        {
            Log.e("--------->", "已經被授權")
        }
    }

}