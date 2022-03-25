package talent.jump.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import talent.jump.R
import talent.jump.databinding.BoardFragmentBinding


class BoardFragment: BaseFragment() {
    private var _binding: BoardFragmentBinding? = null
    private val binding get() = _binding!!

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

        val linearLayoutManager= LinearLayoutManager(context)
        binding.recyclerWall.layoutManager = linearLayoutManager
        binding.recyclerWall.adapter=WallBoardAdapter()

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    inner class WallBoardAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_board_wall, parent, false)
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