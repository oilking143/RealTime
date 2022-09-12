package talent.jump.Fragment

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Events.UriEvent
import talent.jump.R
import talent.jump.data.PhotoListData
import talent.jump.databinding.FragmentPostBinding
import talent.jump.model.ApiTokenClient


class PostFragment:BaseFragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private var apiclient= ApiTokenClient()
    var post= mutableListOf<Uri>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener {
            requireActivity().finish()

        }

        binding.progressBar.visibility=View.GONE
        val photoList=this.context?.let { getExternalImageContent(it) }

        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.forest)
            .priority(Priority.NORMAL)
        this.context?.let { Glide.with(it).load(photoList?.get(0)).apply(options).into(binding.previewImg) }

        val gridLayoutManager=GridLayoutManager(context,3)
        binding.galleryRecycler.layoutManager = gridLayoutManager
        binding.galleryRecycler.adapter= photoList?.let { GalleryAdapter(it) }

        binding.nextStepBtn.setOnClickListener {

            if(post.size<1)
            {
                Toast.makeText(this.context,"照片不得為空",Toast.LENGTH_LONG).show()
            }
            else
            {

                val photo_list= PhotoListData(post)

                val action = PostFragmentDirections.actionPostFragmentToSharePostFragment(photo_list)
                findNavController().navigate(action)
            }


        }

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetmsgEvent(event: UriEvent?) {

        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.forest)
            .priority(Priority.NORMAL)

        context?.let {
            Glide.with(it)
                .load(event!!.getUri())
                .apply(options)
                .into(binding.previewImg)
        }

    }

    private fun getExternalImageContent(c: Context):List<Uri> {
        val cursor = c.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        var pathList= mutableListOf<Uri>()
        cursor!!.moveToLast()
        cursor.apply {
            while (moveToPrevious()) {
                val id = getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                pathList.add(uri)

            }
            cursor.close()
        }

        return pathList
    }


    inner class GalleryAdapter(var photoList: List<Uri> ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var holder=holder as ViewHolder

            val options: RequestOptions = RequestOptions()
                .placeholder(R.drawable.forest)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.NORMAL)

            context?.let {
                Glide.with(it)
                    .load(photoList[position])
                    .apply(options)
                    .into(holder.imageView)
            }

            holder.imageView.setOnClickListener {

                holder.checkbox.isChecked = !holder.checkbox.isChecked
                EventBus.getDefault().post(UriEvent(photoList[position]))
                post.add(photoList[position])
            }


        }

        override fun getItemCount(): Int {
             return photoList.size
        }


        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val imageView=itemView.findViewById<ImageView>(R.id.gallery_img)
            val checkbox=itemView.findViewById<CheckBox>(R.id.img_checkbox)

        }

    }

}