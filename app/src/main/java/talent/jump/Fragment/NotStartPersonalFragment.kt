package talent.jump.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Events.GetUserEvent
import talent.jump.R
import talent.jump.databinding.NotStartPersonalFragmentBinding
import talent.jump.model.ApiTokenClient


class NotStartPersonalFragment: androidx.fragment.app.Fragment() {
    private var _binding: NotStartPersonalFragmentBinding? = null
    private val binding get() = _binding!!
    private var apiclient=ApiTokenClient()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotStartPersonalFragmentBinding.inflate(inflater, container, false)
        return binding.root
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



        apiclient.getMe()


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetUserEvent(event: GetUserEvent?) {

        if(event!!.GetUserResponse().status)
        {
           val data= event!!.GetUserResponse().data
            val options: RequestOptions = RequestOptions()
                .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                .placeholder(R.drawable.forest)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.NORMAL)

            context?.let {
                Glide.with(it)
                    .load(data.profile_photo)
                    .apply(options)
                    .into(binding.photoView)
            };

            binding.userName.text=data.nickname

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}