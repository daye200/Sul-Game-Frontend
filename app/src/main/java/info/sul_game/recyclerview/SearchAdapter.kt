package info.sul_game.recyclerview

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private var item : List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var filteredList : List<String> = item

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            // 입력 받은 문자열에 대한 처리
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                TODO("Not yet implemented")
            }

            // 처리에 대한 결과
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<String>
            }

        }
    }
}