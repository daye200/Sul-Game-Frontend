package info.sul_game.recyclerview

import androidx.recyclerview.widget.RecyclerView
import info.sul_game.databinding.ItemGameRecyclerviewBinding

class GameViewHolder (binding: ItemGameRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
    val title = binding.tvTitleGameItem
    val contents = binding.tvContentsGameItem
    val userName = binding.tvUsernameGameItem
    val cntHeart = binding.tvHeartGameItem
}