package talent.jump.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Events.GetMeEvent
import talent.jump.R
import talent.jump.databinding.NotStartPersonalFragmentBinding
import talent.jump.model.ApiTokenClient
import talent.jump.utility.PlayerViewModel


class NotStartPersonalFragment: androidx.fragment.app.Fragment() {
    private var _binding: NotStartPersonalFragmentBinding? = null
    private val binding get() = _binding!!
    private var apiclient=ApiTokenClient()
    val args by navArgs<NotStartPersonalFragmentArgs>()
    private var userId=""
    private var profilePhoto=""
    private var nickName=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotStartPersonalFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        userId = args.userId
        profilePhoto=args.profilePhoto
        nickName=args.nickname

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val options: RequestOptions = RequestOptions()
            .transform(MultiTransformation(CenterCrop(), CircleCrop()))
            .placeholder(R.drawable.forest)
            .error(R.mipmap.ic_launcher)
            .priority(Priority.NORMAL)

        context?.let {
            Glide.with(it)
                .load(profilePhoto)
                .apply(options)
                .into(binding.photoView)
        };

        binding.userName.text = nickName

//        binding.userName.text=data.nickname

    }


    override fun onDestroyView() {
        super.onDestroyView()
    }


}