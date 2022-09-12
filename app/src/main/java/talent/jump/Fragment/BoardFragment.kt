package talent.jump.Fragment

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Activity.PostActivity
import talent.jump.Events.CreatePostEvent
import talent.jump.Events.GetFollowerEvent
import talent.jump.Events.GetMeEvent
import talent.jump.Events.GetPostListEvent
import talent.jump.R
import talent.jump.data.FansListData
import talent.jump.data.ListPostData
import talent.jump.data.UserData
import talent.jump.databinding.BoardFragmentBinding
import talent.jump.model.ApiTokenClient


class BoardFragment: BaseFragment() {
    private var _binding: BoardFragmentBinding? = null
    private val binding get() = _binding!!
    private var apiclinet=ApiTokenClient()
    private var followCounts=0
    private lateinit var selfData:UserData
    private var followList= mutableListOf<ListPostData>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BoardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()

        apiclinet.getMe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetUserEvent(event: GetMeEvent?) {
        selfData=event!!.GetUserResponse().data
        apiclinet.getFollows("1","20")
        binding.postBtn.setOnClickListener {

            if(selfData.type==3)
            {
                val intent = Intent(activity, PostActivity::class.java)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(requireContext(),"您必須是直播主方能貼文",Toast.LENGTH_LONG).show()
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnFollowList(event: GetFollowerEvent) {

       var dataAry=event!!.GetFollowList().data
       var idAry= mutableListOf<String>()
       followCounts=dataAry.size
        for (i in dataAry.indices)
        {
            idAry.add(dataAry[i].id)
        }

        idAry.add(selfData.id)

        for(i in idAry.indices)
        {
            apiclinet.getPostList(idAry[i],"","")

        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnListPost(event: GetPostListEvent?) {

        binding.progressBar.visibility=View.GONE
        if(event!!.GetPostList().status)
        {
            val data= event.GetPostList().data
            if(data.isNotEmpty())
            {
                for(i in data.indices)
                {
                    followList.add(data[i])
                }

            }
        }

            val linearLayoutManager= LinearLayoutManager(context)
            binding.recyclerWall.layoutManager = linearLayoutManager
            binding.recyclerWall.adapter=WallBoardAdapter(followList)
            binding.loadingProgress.visibility=View.GONE

        followList= mutableListOf()


    }

    inner class WallBoardAdapter(var data: List<ListPostData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_board_wall, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var holder=holder as ViewHolder

            val imgList:ArrayList<String> = ArrayList()

            for(i in data[position].medias.indices)
            {
                imgList.add(data[position].medias[i])
            }

            holder.memo.text=data[position].content

            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            holder.cyclerPager.layoutManager = linearLayoutManager
           val adapter= context?.let { BannerAdapter(imgList, it) }
            val inDecorator=CirclePagerIndicatorDecoration()
            holder.cyclerPager.addItemDecoration(inDecorator)
            holder.cyclerPager.adapter=adapter

            holder.icon_like.setOnClickListener {

                holder.icon_like.background= ResourcesCompat.getDrawable(resources, R.drawable.icon_heart_board, null)
            }

            holder.icon_message.setOnClickListener {

                val commentId=data[position].id

                val action = MainFragmentDirections.actionMainFragmentToCommentListFragment(commentId)
                findNavController().navigate(action)
            }

            holder.icon_share.setOnClickListener {

                val dialog = context?.let { it1 -> BottomSheetDialog(it1) }

                val view = layoutInflater.inflate(R.layout.bottom_share_dialog, null)


                dialog!!.setContentView(view)
                dialog!!.show()
            }



        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val cyclerPager=itemView.findViewById<RecyclerView>(R.id.banner_recycler)
            val icon_like=itemView.findViewById<ImageView>(R.id.icon_like)
            val icon_message=itemView.findViewById<ImageView>(R.id.icon_message)
            val icon_share=itemView.findViewById<ImageView>(R.id.icon_share)
            val memo=itemView.findViewById<TextView>(R.id.memo)

        }

    }


    private inner class BannerAdapter(var imgList:ArrayList<String>,context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        val mContext=context
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return PagerViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_board_page, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var holder=holder as PagerViewHolder

         if(imgList[position].contains(".mp4"))
         {
            Log.d("testName",imgList[position])
             holder.videoview.visibility=View.VISIBLE
             holder.videoview.setVideoURI(Uri.parse(imgList[position]))
             holder.videoview.start()
         }
            else
         {
             holder.videoview.visibility=View.GONE
             mContext.let {
                 val options = RequestOptions()
                     .skipMemoryCache(true)
                 Glide.with(it)
                     .load(imgList[position])
                     .apply(options)
                     .into(holder.ivPager)
             }


         }

        }

        override fun getItemCount(): Int {
          return imgList.size
        }

        private inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var ivPager: ImageView = itemView.findViewById(R.id.ivPager)
            var videoview: VideoView = itemView.findViewById(R.id.video_view)

        }

    }


    class CirclePagerIndicatorDecoration : RecyclerView.ItemDecoration() {

        private val colorActive = Color.parseColor("#615A58")
        private val colorInactive = Color.parseColor("#BBAFAC")

        /**
         * Height of the space the indicator takes up at the bottom of the view.
         */
        private val mIndicatorHeight = (DP * 32).toInt()

        /**
         * Indicator stroke width.
         */
        private val mIndicatorStrokeWidth = DP * 8

        /**
         * Indicator width.
         */
        private val mIndicatorItemLength = DP * 2
        /**
         * Padding between indicators.
         */
        private val mIndicatorItemPadding = DP * 12

        /**
         * Some more natural animation interpolation
         */
        private val mInterpolator = AccelerateDecelerateInterpolator()

        private val mPaint = Paint()

        init {
            mPaint.strokeCap = Paint.Cap.ROUND
            mPaint.strokeWidth = mIndicatorStrokeWidth
            mPaint.style = Paint.Style.STROKE
            mPaint.isAntiAlias = true
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
            val itemCount = parent.adapter!!.itemCount

            // center horizontally, calculate width and subtract half from center
            val totalLength = mIndicatorItemLength * itemCount
            val paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding
            val indicatorTotalWidth = totalLength + paddingBetweenItems
            val indicatorStartX = (parent.width - indicatorTotalWidth) / 2f

            // center vertically in the allotted space
            val indicatorPosY = parent.height - mIndicatorHeight / 2f

            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)


            // find active page (which should be highlighted)
            val layoutManager = parent.layoutManager as LinearLayoutManager?
            val activePosition = layoutManager!!.findFirstVisibleItemPosition()
            if (activePosition == RecyclerView.NO_POSITION) {
                return
            }

            // find offset of active page (if the user is scrolling)
            val activeChild = layoutManager.findViewByPosition(activePosition)
            val left = activeChild!!.left
            val width = activeChild.width

            // on swipe the active item will be positioned from [-width, 0]
            // interpolate offset for smooth animation
            val progress = mInterpolator.getInterpolation(left * -1 / width.toFloat())

            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
        }

        private fun drawInactiveIndicators(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, itemCount: Int) {
            mPaint.color = colorInactive

            // width of item indicator including padding
            val itemWidth = mIndicatorItemLength + mIndicatorItemPadding

            var start = indicatorStartX
            for (i in 0 until itemCount) {
                // draw the line for every item
                c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint)
                start += itemWidth
            }
        }

        private fun drawHighlights(
            c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
            highlightPosition: Int, progress: Float, itemCount: Int
        ) {
            mPaint.color = colorActive

            // width of item indicator including padding
            val itemWidth = mIndicatorItemLength + mIndicatorItemPadding

            if (progress == 0f) {
                // no swipe, draw a normal indicator
                val highlightStart = indicatorStartX + itemWidth * highlightPosition

                c.drawLine(
                    highlightStart, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint
                )

            } else {
                var highlightStart = indicatorStartX + itemWidth * highlightPosition
                // calculate partial highlight
                val partialLength = mIndicatorItemLength * progress

                // draw the cut off highlight

                c.drawLine(
                    highlightStart + partialLength, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint
                )

                // draw the highlight overlapping to the next item as well
                if (highlightPosition < itemCount - 1) {
                    highlightStart += itemWidth

                    c.drawLine(
                        highlightStart, indicatorPosY,
                        highlightStart + partialLength, indicatorPosY, mPaint
                    )
                }
            }
        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = mIndicatorHeight
        }

        companion object {
            private val DP = Resources.getSystem().displayMetrics.density
        }
    }

}