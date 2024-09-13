package info.sul_game.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import info.sul_game.databinding.ItemDrinkingGameRecyclerviewBinding

class DrinkingGameAdapter(val drinkingGameItemList: ArrayList<DrinkingGameItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding =
            ItemDrinkingGameRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DrinkingGameViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return drinkingGameItemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is DrinkingGameViewHolder) {
            holder.image.setImageResource(drinkingGameItemList[position].image)
            holder.title.text = drinkingGameItemList[position].title
            holder.contents.text = drinkingGameItemList[position].contents
            holder.cntHeart.text = drinkingGameItemList[position].cntHeart.toString()
        }
    }
}