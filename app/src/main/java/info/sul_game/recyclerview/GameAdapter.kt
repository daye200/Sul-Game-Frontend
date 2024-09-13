package info.sul_game.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import info.sul_game.databinding.ItemGameRecyclerviewBinding

class GameAdapter (val gameItemList: ArrayList<GameItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemGameRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return GameViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 3
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is GameViewHolder) {
            holder.title.text = gameItemList[position].title
            holder.contents.text = gameItemList[position].contents
            holder.userName.text = gameItemList[position].userName
            holder.cntHeart.text = gameItemList[position].cntHeart.toString()
        }
    }
}