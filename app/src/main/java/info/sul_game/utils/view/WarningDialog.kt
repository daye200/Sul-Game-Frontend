package info.sul_game.utils.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import info.sul_game.activity.SignUpConfirmationActivity
import info.sul_game.databinding.DialogWarningBinding

class WarningDialog (private val context : AppCompatActivity) {

    private lateinit var binding : DialogWarningBinding
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    fun show() {
        binding = DialogWarningBinding.inflate(LayoutInflater.from(context))

        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        // 확인 버튼 동작
        binding.btnSubmitWarningDialog.setOnClickListener {
            dlg.dismiss()
            context.startActivity(Intent(context, SignUpConfirmationActivity::class.java))
            context.finish()
        }

        // 취소 버튼 동작
        binding.btnCancelWarningDialog.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }
}