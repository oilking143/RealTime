package talent.jump.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import talent.jump.databinding.FragmrntPasswordConfirmBinding

class PasswordConfirmFragment:Fragment() {
    private var _binding: FragmrntPasswordConfirmBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmrntPasswordConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backIcon.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}