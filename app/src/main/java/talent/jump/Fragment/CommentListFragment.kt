package talent.jump.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import talent.jump.Events.CreateCommentEvent
import talent.jump.Events.DeletePostEvent
import talent.jump.Events.GetCommentListEvent
import talent.jump.Events.errorEvent
import talent.jump.R
import talent.jump.data.ListPostCommentData
import talent.jump.databinding.FragmentCommentBinding
import talent.jump.model.ApiTokenClient
import java.text.SimpleDateFormat
import java.util.*

class CommentListFragment:BaseFragment() {
    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!
    private var commentId=""
    private var apiClient=ApiTokenClient()
    val args by navArgs<CommentListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
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
        Log.d("commentId",commentId)


        apiClient.getListPostComments(commentId,"","","")

        binding.msgSendBtn.setOnClickListener {

            var postJson= JsonObject()
            postJson.addProperty("post_id",commentId)
            postJson.addProperty("content","${binding.pushMsg.text}")
            apiClient.CreateComment(postJson)
            binding.progressBar.visibility=View.VISIBLE
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        commentId = args.commentId

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnCommentList(event: GetCommentListEvent?) {
        Log.d("commentResult",event!!.GetCommentList().data[0].content)
        binding.progressBar.visibility=View.GONE
        val adapter=CommentAdapter(event!!.GetCommentList().data)
        val linearLayoutManager= LinearLayoutManager(context)
        binding.commentRecycler.layoutManager = linearLayoutManager
        binding.commentRecycler.adapter=adapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnCreateComment(event: CreateCommentEvent?) {

        binding.pushMsg.setText("")
        apiClient.getListPostComments(commentId,"","","")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnDeleteComment(event: DeletePostEvent?) {

        if (event!!.GetDelete().status)
        {
            apiClient.getListPostComments(commentId,"","","")

        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun errorEvent(event: errorEvent?) {

        Log.d("ErrorMsg",event!!.getMsg())
    }


    inner class CommentAdapter(var data:List<ListPostCommentData>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_comment_layout, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var holder=holder as ViewHolder
            holder.description.text=data[position].content
            holder.name.text=data[position].user.username
            holder.deletebtn.setOnClickListener {
              apiClient.DeleteComment(data[position].id)
            }

            var timepass=System.currentTimeMillis()-data[position].create_at
            val second=timepass/1000
            val minute=second/60
            val hour=minute/60
            val day=hour/24
            val date = Date(data[position].create_at)
            val f1 = SimpleDateFormat("yyyy-MM-dd ")
            val s2 = f1.format(date) // 2020-06-30 11:00:26.401
            val longtime=s2

            if(day>30)
            {
                holder.timeCount.text="$longtime"
            }
            if(day>0)
            {
                holder.timeCount.text="${day}"
                holder.day.text="天前"
            }
            else if(hour>0)
            {
                holder.timeCount.text="$hour"
                holder.day.text="小時前"
            }
            else if(minute>0)
            {
                holder.timeCount.text="$minute"
                holder.day.text="分鐘前"
            }
            else if(minute<0)
            {
                holder.timeCount.text=""
                holder.day.text="剛剛"
            }



        }

        override fun getItemCount(): Int {
          return data.size
        }

        inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var name: TextView = itemView.findViewById(R.id.name)
            var description: TextView = itemView.findViewById(R.id.description)
            var deletebtn:LinearLayout=itemView.findViewById(R.id.delete_msg)
            var timeCount:TextView=itemView.findViewById(R.id.time_count)
            var day:TextView=itemView.findViewById(R.id.day)

        }

    }

}