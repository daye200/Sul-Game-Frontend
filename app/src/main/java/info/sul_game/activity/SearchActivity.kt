package info.sul_game.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import info.sul_game.databinding.ActivitySearchBinding
import info.sul_game.recyclerview.LatestSearchAdapter
import info.sul_game.utils.view.CustomSearchView

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSearchView() // searchview 초기화
        initRecyclerView() // recyclerview 초기화
    }
    
    private fun initRecyclerView() {
        var recentSearchList = mutableListOf("바니바니", "고래 게임")

        binding.recyclerviewRecentSearch.adapter = LatestSearchAdapter(recentSearchList)
        binding.recyclerviewRecentSearch.layoutManager = LinearLayoutManager(this)
    }

    private fun initSearchView(){
        // searchView의 뒤로가기 버튼 기능 구현
        binding.searchviewSearch.setOnBackListener {
            finish()
        }

        // searchView의 setOnQueryTextListener 설정
        binding.searchviewSearch.setOnQueryTextListener(object : CustomSearchView.OnQueryTextListener{
            // 검색 버튼 입력 시 호출
            override fun onQueryTextChange(newText: String): Boolean {
                TODO("Not yet implemented")
            }

            // 텍스트 입력 및 수정 시 호출
            override fun onQueryTextSubmit(query: String): Boolean {
                TODO("Not yet implemented")
            }

        } )
    }
}