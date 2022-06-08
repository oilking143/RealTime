package talent.jump.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.socket.client.Socket
import talent.jump.R
import talent.jump.databinding.MainFragmentBinding


class MainFragment: BaseFragment() ,RadioGroup.OnCheckedChangeListener{
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mSocket:Socket

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.livePageSlider.isUserInputEnabled = false
        binding.livePageSlider.adapter=LivePagerSlideAdapter(this)
        binding.bottomRadio.setOnCheckedChangeListener(this)

    }


    inner class LivePagerSlideAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int =3

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return LiveFragment()
                1 -> return BoardFragment()
                2 -> return PersonalFragment()

            }
            return  LiveFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId)
        {
            R.id.home_radio->binding.livePageSlider.currentItem = 0
            R.id.wall_radio->binding.livePageSlider.currentItem=1
            R.id.mine_radio->binding.livePageSlider.currentItem=2
        }
    }
}