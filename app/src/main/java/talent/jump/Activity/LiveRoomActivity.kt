package talent.jump.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import talent.jump.Fragment.LiveRoomFragment
import talent.jump.R
import talent.jump.databinding.ActivityLiveroomBinding


class LiveRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveroomBinding
    var fragmentLiveRoom=LiveRoomFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment()
    }

    fun initFragment()
    {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, fragmentLiveRoom)
        transaction.commit()
    }




}