package info.sul_game.recyclerview

import androidx.recyclerview.widget.RecyclerView
import info.sul_game.databinding.ItemChartRecyclerviewBinding

class LiveChartViewHolder(binding: ItemChartRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val image = binding.ivImageChartItem
    val rank = binding.tvRankChartItem
    val title = binding.tvTitleChartItem
    val contents = binding.tvContentsChartItem
    val cntHeart = binding.tvHeartChartItem
}