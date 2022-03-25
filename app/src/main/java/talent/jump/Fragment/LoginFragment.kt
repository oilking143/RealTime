package talent.jump.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import talent.jump.R
import talent.jump.databinding.LoginFragmentBinding
import java.util.*
import java.util.concurrent.TimeUnit

class LoginFragment: BaseFragment() {
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                findNavController().navigate(R.id.action_loginFragment_to_windowFragment)
            }
        }, 3000)


    }

    override fun onDestroy() {
        super.onDestroy()
    }
}