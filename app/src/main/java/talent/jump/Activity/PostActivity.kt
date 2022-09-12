package talent.jump.Activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import talent.jump.databinding.ActivityMainBinding
import io.socket.client.IO
import io.socket.client.Socket
import talent.jump.databinding.ActivityPostBinding
import java.net.URISyntaxException


class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }





}