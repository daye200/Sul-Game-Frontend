package info.sul_game.recyclerview

import androidx.recyclerview.widget.RecyclerView
import info.sul_game.databinding.ItemUniversityNameRecyclerviewBinding

class UniversityNameViewHolder(binding: ItemUniversityNameRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var university_name = binding.tvNameUniversityNameItem
}