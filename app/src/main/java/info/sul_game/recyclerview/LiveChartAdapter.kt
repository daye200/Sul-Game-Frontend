package info.sul_game.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import info.sul_game.databinding.ItemChartRecyclerviewBinding


class LiveChartAdapter(val liveCharList: ArrayList<LiveChartItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemChartRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return LiveChartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return liveCharList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is LiveChartViewHolder) {
            holder.image.setImageResource(liveCharList[position].image)
            holder.rank.text = (position + 1).toString()
            holder.title.text = liveCharList[position].title
            holder.contents.text = liveCharList[position].contents
            holder.cntHeart.text = liveCharList[position].cntHeart.toString()
        }
    }
}