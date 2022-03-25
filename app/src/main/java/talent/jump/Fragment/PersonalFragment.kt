package talent.jump.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import talent.jump.R
import talent.jump.databinding.PersonalFragmentBinding


class PersonalFragment: androidx.fragment.app.Fragment() {
    private var _binding: PersonalFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PersonalFragmentBinding.inflate(inflater, container, false)
        return binding.root
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
                .load(R.drawable.forest)
                .apply(options)
                .into(binding.photoView)
        };

        val gridLayoutManager= GridLayoutManager(context,3)
        binding.photorecycler.layoutManager = gridLayoutManager
        binding.photorecycler.adapter=PhotoBoardAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    inner class PhotoBoardAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_personal_photo, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var holder=holder as ViewHolder



        }

        override fun getItemCount(): Int {
            return 5
        }

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

        }

    }

}