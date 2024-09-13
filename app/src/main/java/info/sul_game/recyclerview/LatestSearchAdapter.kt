package info.sul_game.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import info.sul_game.databinding.ItemRecentSearchRecyclerviewBinding

class LatestSearchAdapter(private var item : List<String>) : RecyclerView.Adapter<LatestSearchAdapter.RecentSearchHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchHolder {
        val binding = ItemRecentSearchRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentSearchHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.count()
    }

    override fun onBindViewHolder(
        holder: RecentSearchHolder,
        position: Int
    ) {
        holder.textView.text = item[position]
    }

    inner class RecentSearchHolder(binding: ItemRecentSearchRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val textView = binding.tvRecentSearchItem
    }
}