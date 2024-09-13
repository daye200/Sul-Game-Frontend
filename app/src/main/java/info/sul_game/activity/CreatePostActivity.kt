package info.sul_game.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import info.sul_game.databinding.ActivityCreateBinding
import info.sul_game.fragment.CreateCreationGameFragment
import info.sul_game.fragment.IntroCreateFragment

class CreatePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = CreateFragmentPagerAdapter(this)
        binding.vpCreate.adapter = pagerAdapter
        binding.btnChangeCreate.setOnClickListener {
            val currentItem = binding.vpCreate.currentItem
            binding.vpCreate.currentItem = if (currentItem == 0) 1 else 0
        }


    }

    class CreateFragmentPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
        override fun getItemCount(): Int = 2 // 0번과 1번, 두 개의 프래그먼트

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CreateCreationGameFragment()
                1 -> IntroCreateFragment()
                else -> throw IllegalStateException("Unexpected position $position")
            }

        }


}}