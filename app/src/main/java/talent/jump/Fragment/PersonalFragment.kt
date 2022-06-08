package talent.jump.Fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import java.io.ByteArrayOutputStream
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Events.AuthRegisterEvent
import talent.jump.Events.CreateStreamEvent
import talent.jump.Events.GetStreamEvent
import talent.jump.Events.GetUserEvent
import talent.jump.R
import talent.jump.data.AuthRegistResponse
import talent.jump.databinding.PersonalFragmentBinding
import talent.jump.model.ApiTokenClient


class PersonalFragment: BaseFragment(){
    private var _binding: PersonalFragmentBinding? = null
    private val binding get() = _binding!!
    private var apiclient=ApiTokenClient()
    private var requestCode=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PersonalFragmentBinding.inflate(inflater, container, false)
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
        binding.becomAdmin.setOnClickListener{

            apiclient.StreamAuth()

        }

        binding.photoView.setOnClickListener {


            val dialog = context?.let { it1 -> BottomSheetDialog(it1) }

            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog_content, null)


            val btnPhoto = view.findViewById<LinearLayout>(R.id.photo)
            val btnGallery = view.findViewById<LinearLayout>(R.id.gallery)


            btnPhoto.setOnClickListener {
                requestCode=1
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    resultLauncher.launch(takePictureIntent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
                dialog!!.dismiss()
            }

            btnGallery.setOnClickListener {
                requestCode=2
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                try {
                    resultLauncher.launch(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
                dialog!!.dismiss()
            }


            dialog!!.setContentView(view)
            dialog!!.show()

        }

        binding.editBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_personalEditFragment)

        }

        binding.startLive.setOnClickListener {
            /**
             *
             *     "display_name": "Oiltest",
            "description": "油王直播",
            "type_id": 1,
            "private": true,
            "pass_code": "abc123",
            "status": 1,
            "living_at": 0
             */

            val createJson=JsonObject()
            createJson.addProperty("display_name","${binding.userName.text}")
            createJson.addProperty("description","${binding.description.text}")
            createJson.addProperty("type_id",1)
            createJson.addProperty("private",true)
            createJson.addProperty("pass_code","abc123")
            createJson.addProperty("status",1)
            createJson.addProperty("living_at",0)
            apiclient.createStreams(createJson)

        }



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun CreateStreamEvent(event: CreateStreamEvent?) {

        if(event!!.GetStreamResponse().status)
        {
            val action =  MainFragmentDirections.actionMainFragmentToLiveRoomFragment(
                event!!.GetStreamResponse().data.push
            )
            findNavController().navigate(action)
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
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

            if(data.stream_auth.status!=0)
            {
                binding.postAction.visibility=View.VISIBLE
                binding.startLive.visibility=View.VISIBLE
                binding.becomAdmin.visibility=View.GONE
                binding.nextTime.visibility=View.VISIBLE
            }
            else
            {
                binding.postAction.visibility=View.GONE
                binding.startLive.visibility=View.GONE
                binding.becomAdmin.visibility=View.VISIBLE
                binding.nextTime.visibility=View.GONE
            }

        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStreamAuthEvent(event: AuthRegisterEvent?) {


        if(event!!.GetAuthRegist().status)
        {
            Toast.makeText(context,resources.getString(R.string.auth_success),Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val options: RequestOptions = RequestOptions()
                .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                .placeholder(R.drawable.forest)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.NORMAL)
            if(requestCode==1)
            {
                context?.let {
                    Glide.with(it)
                        .load(data!!.extras!!.get("data"))
                        .apply(options)
                        .into(binding.photoView)
                }


                val imageBitmap = data!!.extras!!.get("data") as Bitmap
                val photoJson=JsonObject()
                photoJson.addProperty("data",encodeImage(imageBitmap))

                apiclient.upLoadPhoto(photoJson)
            }
            else
            {
                context?.let {
                    Glide.with(it)
                        .load(data!!.data)
                        .apply(options)
                        .into(binding.photoView)
                }
            }

            }

        }


    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    }


