package talent.jump.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import talent.jump.Fragment.LiveRoomFragment
import talent.jump.R
import talent.jump.databinding.ActivityWatchroomBinding


class WatchRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWatchroomBinding
    var fragmentLiveRoom=LiveRoomFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment()
    }

    fun initFragment()
    {

        val transaction = supportFragmentManager.beginTransaction()


        val intent=getIntent()
        val bundle = Bundle()
        bundle.putString("pullstream",intent.getStringExtra("pullstream") )
        bundle.putString("stId",intent.getStringExtra("stId") )
        bundle.putString("roomname",intent.getStringExtra("roomname") )
        bundle.putString("username",intent.getStringExtra("username") )
        bundle.putBoolean("private",intent.getBooleanExtra("private",true) )
        fragmentLiveRoom.arguments=bundle
        transaction.add(R.id.fragment_container, fragmentLiveRoom)
        transaction.commit()
    }




}