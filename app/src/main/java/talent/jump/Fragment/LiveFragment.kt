package talent.jump.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wangpeiyuan.cycleviewpager2.CycleViewPager2Helper
import com.wangpeiyuan.cycleviewpager2.adapter.CyclePagerAdapter
import com.wangpeiyuan.cycleviewpager2.indicator.DotsIndicator
import talent.jump.Activity.LoginActivity
import talent.jump.R
import talent.jump.databinding.LiveFragmentBinding


class LiveFragment: BaseFragment() {
    private var _binding: LiveFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LiveFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val gridLayoutManager= GridLayoutManager(context,2)
        binding.liveRoomRecycler.layoutManager = gridLayoutManager
        binding.liveRoomRecycler.adapter=LiveRoomAdapter()
        binding.liveTitleBtn.setOnClickListener {


        }
        binding.startPlayBtn.setOnClickListener {

            findNavController().navigate(R.id.action_mainFragment_to_liveRoomFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
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

    inner class LiveRoomAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.itme_live_room_view, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var holder=holder as ViewHolder

        holder.LiveRoomFrame.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_watchRoomFragment)
        }


        }

        override fun getItemCount(): Int {
            return 10
        }

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

                val LiveRoomFrame=itemView.findViewById<ConstraintLayout>(R.id.liveroom_frame)


        }

    }

}