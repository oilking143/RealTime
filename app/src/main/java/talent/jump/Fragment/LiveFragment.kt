package talent.jump.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.wangpeiyuan.cycleviewpager2.CycleViewPager2Helper
import com.wangpeiyuan.cycleviewpager2.adapter.CyclePagerAdapter
import com.wangpeiyuan.cycleviewpager2.indicator.DotsIndicator
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Activity.LiveRoomActivity
import talent.jump.Activity.MainActivity
import talent.jump.Activity.PostActivity
import talent.jump.Activity.WatchRoomActivity
import talent.jump.Events.GetSearchEvent
import talent.jump.Events.GetStreamEvent
import talent.jump.Events.errorEvent
import talent.jump.GlobalData
import talent.jump.R
import talent.jump.data.DataLiveStreamList
import talent.jump.data.LivadataLists
import talent.jump.data.LiveData
import talent.jump.data.SearchStream
import talent.jump.databinding.LiveFragmentBinding
import talent.jump.model.ApiTokenClient


class LiveFragment: BaseFragment() {
    private var _binding: LiveFragmentBinding? = null
    private val binding get() = _binding!!
    private var apiclient= ApiTokenClient()
    private lateinit var data: DataLiveStreamList
    private var mClient= OkHttpClient()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LiveFragmentBinding.inflate(inflater, container, false)
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


        apiclient.getListStream("1","10","","")

        val imgList:ArrayList<Int> = ArrayList()
        imgList.add(R.drawable.forest)
        imgList.add(R.drawable.forest)
        imgList.add(R.drawable.forest)
        val nextItemVisiblePx = requireContext().resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            requireContext().resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val dotsRadius = requireContext().resources.getDimension(R.dimen.dots_radius)
        val dotsPadding = requireContext().resources.getDimension(R.dimen.dots_padding)
        val dotsBottomMargin = requireContext().resources.getDimension(R.dimen.dots_bottom_margin)



        CycleViewPager2Helper(binding.banner)
            .setAdapter(MyCyclePagerAdapter(imgList, requireContext()))
            .setMultiplePagerScaleInTransformer(
                nextItemVisiblePx.toInt(),
                currentItemHorizontalMarginPx.toInt(),
                0.1f
            )
            .setDotsIndicator(
                dotsRadius,
                Color.WHITE,
                Color.LTGRAY,
                dotsPadding,
                0,
                dotsBottomMargin.toInt(),
                0,
                DotsIndicator.Direction.CENTER
            )
            .setAutoTurning(3000L)
            .build()


        binding.liveTitleBtn.setOnClickListener {
            backnormal()
            binding.liveTitleBtn.textSize=resources.getDimension(R.dimen.select_radio)
            binding.liveTitleBtn.setTypeface(null, Typeface.BOLD)


            val gridLayoutManager= GridLayoutManager(context,2)
            binding.liveRoomRecycler.layoutManager = gridLayoutManager
            binding.liveRoomRecycler.adapter=LiveRoomAdapter(data.lists)
        }

        binding.musicTitleBtn.setOnClickListener {
            backnormal()
            binding.musicTitleBtn.textSize=resources.getDimension(R.dimen.select_radio)
            binding.musicTitleBtn.setTypeface(null, Typeface.BOLD)

            val localdata= mutableListOf<LivadataLists>()
            for(i in 0 until data.lists.size)
            {
                if(data.lists[i].type==1)
                {
                    localdata.add(data.lists[i])
                }
            }

            val gridLayoutManager= GridLayoutManager(context,2)
            binding.liveRoomRecycler.layoutManager = gridLayoutManager
            binding.liveRoomRecycler.adapter=LiveRoomAdapter(localdata)
        }

        binding.gameTitleBtn.setOnClickListener {
            backnormal()
            binding.gameTitleBtn.textSize=resources.getDimension(R.dimen.select_radio)
            binding.gameTitleBtn.setTypeface(null, Typeface.BOLD)

            val localdata= mutableListOf<LivadataLists>()
            for(i in 0 until data.lists.size)
            {
                if(data.lists[i].type==2)
                {
                    localdata.add(data.lists[i])
                }
            }

            val gridLayoutManager= GridLayoutManager(context,2)
            binding.liveRoomRecycler.layoutManager = gridLayoutManager
            binding.liveRoomRecycler.adapter=LiveRoomAdapter(localdata)
        }

        binding.talkTitleBtn.setOnClickListener {
            backnormal()
            binding.talkTitleBtn.textSize=resources.getDimension(R.dimen.select_radio)
            binding.talkTitleBtn.setTypeface(null, Typeface.BOLD)

            val localdata= mutableListOf<LivadataLists>()
            for(i in 0 until data.lists.size)
            {
                if(data.lists[i].type==3)
                {
                    localdata.add(data.lists[i])
                }
            }

            val gridLayoutManager= GridLayoutManager(context,2)
            binding.liveRoomRecycler.layoutManager = gridLayoutManager
            binding.liveRoomRecycler.adapter=LiveRoomAdapter(localdata)


        }


        binding.searchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
//                val localdata= mutableListOf<LivadataLists>()
//                for(i in 0 until data.lists.size)
//                {
//                    if(data.lists[i].info.display_name.contains(s.toString()))
//                    {
//                        localdata.add(data.lists[i])
//                    }
//                }
//
//                val gridLayoutManager= GridLayoutManager(context,2)
//                binding.liveRoomRecycler.layoutManager = gridLayoutManager
//                binding.liveRoomRecycler.adapter=LiveRoomAdapter(localdata)
//
//                if(s!!.isNotEmpty())
//                {
//                    binding.searchIcon.visibility=View.GONE
//                }
//                else
//                {
//                    binding.searchIcon.visibility=View.VISIBLE
//                }

                if(s!!.isEmpty())
                {
                    apiclient.getListStream("1","10","0","")

                }
                else
                {
                    apiclient.getSearch("$s")

                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.startPlayBtn.setOnClickListener {

            val intent = Intent(activity, LiveRoomActivity::class.java)
            startActivity(intent)
        }


    }



    fun backnormal()
    {
        binding.liveTitleBtn.textSize=resources.getDimension(R.dimen.normal_radio)
        binding.musicTitleBtn.textSize=resources.getDimension(R.dimen.normal_radio)
        binding.gameTitleBtn.textSize=resources.getDimension(R.dimen.normal_radio)
        binding.talkTitleBtn.textSize=resources.getDimension(R.dimen.normal_radio)
        binding.liveTitleBtn.setTypeface(null, Typeface.NORMAL)
        binding.musicTitleBtn.setTypeface(null, Typeface.NORMAL)
        binding.gameTitleBtn.setTypeface(null, Typeface.NORMAL)
        binding.talkTitleBtn.setTypeface(null, Typeface.NORMAL)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetStreamEvent(event: GetStreamEvent?) {
        if(event!!.GetLiveStreamList().status)
        {
             data= event.GetLiveStreamList().data
            val gridLayoutManager= GridLayoutManager(context,2)
            binding.liveRoomRecycler.layoutManager = gridLayoutManager
            var sort=data.lists.sortedByDescending { it.info.viewer_count}
            var clean= mutableListOf<LivadataLists>()
            for(i in sort.indices)
            {
                if(sort[i].type==1)
                {
                    clean.add(sort[i])
                }
            }
            binding.liveRoomRecycler.adapter=LiveRoomAdapter(clean)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetSearchEvent(event: GetSearchEvent?) {
        if(event!!.GetSearchList().status)
        {

            val searchdata=event!!.GetSearchList().data
            val gridLayoutManager= GridLayoutManager(context,2)
            binding.liveRoomRecycler.layoutManager = gridLayoutManager
            var sort=searchdata.streams.sortedByDescending { it.viewer_count}
            binding.liveRoomRecycler.adapter=SearchAdapter(sort)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorEvent(event: errorEvent?) {

        Log.d("errorMsg",event!!.getMsg())

    }



    private inner class MyCyclePagerAdapter(imageItems:ArrayList<Int>,context: Context) : CyclePagerAdapter<PagerViewHolder>() {
        val items = imageItems
        val mContext=context
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
            return PagerViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_page, parent, false)
            )
        }

        override fun getRealItemCount(): Int = items.size

        override fun onBindRealViewHolder(holder: PagerViewHolder, position: Int) {
            val options = RequestOptions()
                .skipMemoryCache(true)
            mContext?.let {
                Glide.with(it)
                    .load(items.get(position))
                    .apply(options)
                    .into(holder.ivPager)
            }
        }
    }
    private inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivPager: ImageView = itemView.findViewById(R.id.ivPager)
    }

    inner class LiveRoomAdapter(var list:List<LivadataLists>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.itme_live_room_view, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var holder=holder as ViewHolder



        if(list[position].info.status==1)
        {
            holder.livingMark.visibility=View.VISIBLE
            holder.LiveRoomFrame.setOnClickListener {
                val pullstream=list[position].info.pull
                val stId=list[position].info.id
                val roomname=list[position].info.display_name
                val username=list[position].info.owner.nickname
                val private=list[position].info.private


                val intent = Intent(activity, WatchRoomActivity::class.java)
                intent.putExtra("pullstream",list[position].info.pull)
                intent.putExtra("stId",list[position].info.id)
                intent.putExtra("roomname",list[position].info.display_name)
                intent.putExtra("username",list[position].info.owner.nickname)
                intent.putExtra("private",list[position].info.private)
                startActivity(intent)
            }
        }
         else
        {
            holder.livingMark.visibility=View.GONE
            holder.LiveRoomFrame.setOnClickListener {
                val userId=list[position].info.owner.id
                val profile_photo=list[position].info.owner.profile_photo
                val nickname=list[position].info.owner.nickname

                val action = MainFragmentDirections.actionMainFragmentToNotStartPersonalFragment(userId,profile_photo,nickname)
                    findNavController().navigate(action)

            }
        }

            val options: RequestOptions = RequestOptions()
                .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                .placeholder(R.drawable.forest)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.NORMAL)

            context?.let {
                Glide.with(it)
                    .load(list[position].info.owner.profile_photo)
                    .apply(options)
                    .into(holder.roomphoto)
            }


            holder.tuberName.text=list[position].info.display_name
            holder.watch.text="${list[position].info.viewer_count}"


        }


        override fun getItemCount(): Int {
            return list.size
        }

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            val LiveRoomFrame=itemView.findViewById<ConstraintLayout>(R.id.liveroom_frame)
            val livingMark=itemView.findViewById<ImageView>(R.id.living_mark)
            val roomphoto=itemView.findViewById<ImageView>(R.id.room_photo)
            val tuberName=itemView.findViewById<TextView>(R.id.tubername)
            val watch=itemView.findViewById<TextView>(R.id.watch)

        }

    }


    inner class SearchAdapter(var list:List<SearchStream>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.itme_live_room_view, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var holder=holder as ViewHolder



            if(list[position].status==1)
            {
                holder.livingMark.visibility=View.VISIBLE
                holder.LiveRoomFrame.setOnClickListener {
                    val pullstream=list[position].pull
                    val stId=list[position].id
                    val username=list[position].owner.nickname
                    val roomname=list[position].display_name
                    val private=list[position].private
                    val intent = Intent(activity, WatchRoomActivity::class.java)
                    intent.putExtra("pullstream",list[position].pull)
                    intent.putExtra("stId",list[position].id)
                    intent.putExtra("roomname",list[position].display_name)
                    intent.putExtra("username",list[position].owner.nickname)
                    intent.putExtra("private",list[position].private)
                    startActivity(intent)
                }
            }
            else
            {
                holder.livingMark.visibility=View.GONE
                holder.LiveRoomFrame.setOnClickListener {
                    findNavController().navigate(R.id.action_mainFragment_to_notStartPersonalFragment)

                }
            }

            val options: RequestOptions = RequestOptions()
                .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                .placeholder(R.drawable.forest)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.NORMAL)

            context?.let {
                Glide.with(it)
                    .load(list[position].owner.profile_photo)
                    .apply(options)
                    .into(holder.roomphoto)
            }


            holder.tuberName.text=list[position].display_name
            holder.watch.text="${list[position].viewer_count}"


        }


        override fun getItemCount(): Int {
            return list.size
        }

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            val LiveRoomFrame=itemView.findViewById<ConstraintLayout>(R.id.liveroom_frame)
            val livingMark=itemView.findViewById<ImageView>(R.id.living_mark)
            val roomphoto=itemView.findViewById<ImageView>(R.id.room_photo)
            val tuberName=itemView.findViewById<TextView>(R.id.tubername)
            val watch=itemView.findViewById<TextView>(R.id.watch)

        }

    }


}