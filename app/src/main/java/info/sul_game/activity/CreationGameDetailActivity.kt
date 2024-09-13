package info.sul_game.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import info.sul_game.R
import info.sul_game.databinding.ActivityCreateDetailBinding


class CreationGameDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCreateDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val createDetailList = ArrayList<CreationGameDetailComment>()
        createDetailList.add(
            CreationGameDetailComment("다예"
                ,"아아아아 힘들어"
                , R.drawable.btn_star_on
            )
        )
        createDetailList.add(

            CreationGameDetailComment("뽀로로"
                ,"노는게 너무 좋아"
                ,R.drawable.btn_star_on

            )
        )
    }
}