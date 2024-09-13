package info.sul_game.recyclerview

import androidx.recyclerview.widget.RecyclerView
import info.sul_game.databinding.ItemDrinkingGameRecyclerviewBinding

class DrinkingGameViewHolder(binding: ItemDrinkingGameRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val image = binding.ivImageChartItem
    var title = binding.tvTitleChartItem
    var contents = binding.tvContentsChartItem
    var cntHeart = binding.tvHeartChartItem
    }