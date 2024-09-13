package info.sul_game.recyclerview

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import info.sul_game.R
import info.sul_game.databinding.ItemCreateRecyclerviewBinding
import info.sul_game.fragment.CreateCreationGameFragment

class CreateFileAdapter(private val items: MutableList<MediaItem>, private val fragment: CreateCreationGameFragment): RecyclerView.Adapter<CreateFileAdapter.CreateCreateAdapterViewHoler>() {
    data class MediaItem(val uri: Uri, val isVideo: Boolean,val isAudio: Boolean)
    private var videoItemCnt = 0
    private var audioItemCnt =0
    private var totalItemCnt = 0
    private var videoExists = false
    private var audioExists = false
    private var totalExists = false
    fun isVideoExist(): Boolean{
        if(videoItemCnt<=0){
            videoExists=false
        }else{videoExists=true}
        return videoExists
    }
    fun isAudioExist():Boolean{
        if(audioItemCnt<=0){
            audioExists=false
        }else{audioExists=true}
        return audioExists

    }
    fun isTotalExists():Boolean{
        if(totalItemCnt>0){
            totalExists = true
        }else{totalExists = false}
        return totalExists
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreateCreateAdapterViewHoler {
        val binding = ItemCreateRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    return CreateCreateAdapterViewHoler(binding)
    }

    override fun onBindViewHolder(
        holder: CreateCreateAdapterViewHoler,
        position: Int
    ) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun addMedia(uri:Uri,isVideo: Boolean,isAudio:Boolean):Boolean{


        items.add(MediaItem(uri,isVideo,isAudio))
        notifyItemInserted(items.size -1)
        if(isVideo==true){

            videoItemCnt++
        }
        if(isAudio==true){
            audioItemCnt++
        }
        totalItemCnt++
        return true
    }

    fun removeMedia(position: Int){
        if(items[position].isVideo){
            videoItemCnt=0
        }
        if(items[position].isAudio){
            audioItemCnt=0
        }
        totalItemCnt--
        items.removeAt(position)
        notifyItemRemoved(position)
        fragment.checkRecyclerViewVisibility()
    }
    inner class CreateCreateAdapterViewHoler(private val binding: ItemCreateRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: MediaItem){
            if (item.isVideo) {
                // 비디오일 때
                binding.ivVideoIconItemCreateRv.visibility = View.VISIBLE
                binding.ivAudioIconItemCreateRv.visibility = View.GONE // 오디오 아이콘 숨김
                Glide.with(binding.ivThumnailItemCreateRv.context)
                    .asBitmap()
                    .load(item.uri)
                    .into(binding.ivThumnailItemCreateRv)
            } else if (item.isAudio) {
                // 오디오일 때
                binding.ivVideoIconItemCreateRv.visibility = View.GONE // 비디오 아이콘 숨김
                binding.ivAudioIconItemCreateRv.visibility = View.VISIBLE // 오디오 아이콘 보임
                binding.ivThumnailItemCreateRv.setImageResource(R.drawable.audio_background) // 오디오 아이콘으로 이미지 설정
            } else {
                // 그 외 (이미지)
                binding.ivVideoIconItemCreateRv.visibility = View.GONE // 비디오 아이콘 숨김
                binding.ivAudioIconItemCreateRv.visibility = View.GONE // 오디오 아이콘 숨김
                Glide.with(binding.ivThumnailItemCreateRv.context)
                    .load(item.uri)
                    .into(binding.ivThumnailItemCreateRv)
            }
            binding.btnDeleteItemCreateRv.setOnClickListener {
                removeMedia(adapterPosition)

            }
            binding.btnDeleteItemCreateRv.visibility = View.VISIBLE
        }
    }
}