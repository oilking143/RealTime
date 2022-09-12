package talent.jump.Fragment

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Events.CreatePostEvent
import talent.jump.Events.PostMediaEvent
import talent.jump.Events.errorEvent
import talent.jump.R
import talent.jump.data.PhotoListData
import talent.jump.databinding.FragmentContentBinding
import talent.jump.model.ApiTokenClient
import the.example.app.FileUrlConverter
import java.io.File

class SharePostFragment:BaseFragment() {
    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding!!
    lateinit var photoList:PhotoListData
    private var apiclinet= ApiTokenClient()
    private var user_id=""
    private  var mediaJsonAry= JsonArray()
    private var uploadCount=0
    val args by navArgs<SharePostFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
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


    override fun onAttach(context: Context) {
        super.onAttach(context)

        photoList = args.photoList!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener {

            requireActivity().supportFragmentManager.popBackStack()
        }

        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.forest)
            .error(R.mipmap.ic_launcher)
            .priority(Priority.NORMAL)

        context?.let {
            Glide.with(it)
                .load(photoList.getList()[0])
                .apply(options)
                .into(binding.photoPreview)
        }

        binding.shareBtn.setOnClickListener {

            binding.progerssBar.visibility=View.VISIBLE
            uploadCount=photoList.getList().size
            for(i in 0 until photoList.getList().size)
            {
                val realPath= context?.let { FileUrlConverter.getFilePath(it,photoList.getList()[i]) }
                val file = realPath?.let { it1 -> File(it1) }
                if (file != null) {
                    if(file.exists() && file.length()/1024/1024<100) {
                        val requestFile = file.asRequestBody("image/jpeg".toMediaType())
                        val body: MultipartBody.Part =
                            MultipartBody.Part.createFormData("media", file.name, requestFile)
                        apiclinet.postMedia(body)
                    } else {
                        Toast.makeText(context,"檔案大小規格不符合後台規範", Toast.LENGTH_LONG).show()
                    }
                }
            }

            binding.shareBtn.setOnClickListener(null)

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnPostMedia(event: PostMediaEvent?) {

        user_id=event!!.GetMedia().data.user_id

        val postJson= JsonObject()
        postJson.addProperty("content",binding.contentTxt.text.toString())
        mediaJsonAry.add(event!!.GetMedia().data.id)
        postJson.add("media_ids",mediaJsonAry)
        if(mediaJsonAry.size()==uploadCount)
        {
            apiclinet.createPost(postJson)
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnCreatePost(event: CreatePostEvent?) {

        requireActivity().finish()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorEvent(event: errorEvent?) {
        Log.d("error",event!!.getMsg())
    }


}