package info.sul_game.utils.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import info.sul_game.databinding.DialogDateBinding
import java.util.Calendar

class DateDialog (private val context : AppCompatActivity) {

    private lateinit var binding : DialogDateBinding
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    // 콜백 인터페이스 정의
    interface DateDialogListener {
        fun onDateSelected(year: Int, month: Int, day: Int)
    }

    var listener: DateDialogListener? = null

    fun show() {
        binding = DialogDateBinding.inflate(LayoutInflater.from(context))

        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        binding.npYearDateDialog.minValue = 1900
        binding.npYearDateDialog.maxValue = Calendar.getInstance().get(Calendar.YEAR)
        binding.npYearDateDialog.value = 2024

        binding.npMonthDateDialog.minValue = 1
        binding.npMonthDateDialog.maxValue = 12

        binding.npMonthDateDialog.setOnValueChangedListener { _, _, newVal ->
            updateDayPickerMaxValue(newVal, binding.npYearDateDialog.value)
        }

        binding.npYearDateDialog.setOnValueChangedListener { _, _, newYear ->
            updateDayPickerMaxValue(binding.npMonthDateDialog.value, newYear)
        }

        // 초기 DayPicker 설정
        updateDayPickerMaxValue(binding.npMonthDateDialog.value, binding.npYearDateDialog.value)

        // 확인 버튼 동작
        binding.btnSubmitDateDialog.setOnClickListener {
            listener?.onDateSelected(
                binding.npYearDateDialog.value,
                binding.npMonthDateDialog.value,
                binding.npDayDateDialog.value
            )
            dlg.dismiss()
        }

        // 취소 버튼 동작
        binding.btnCancelDateDialog.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }

    // 윤년 확인 함수
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    // DayPicker의 maxValue를 설정하는 함수
    private fun updateDayPickerMaxValue(month: Int, year: Int) {
        binding.npDayDateDialog.maxValue = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 30 // 기본값 (오류 방지를 위해 추가)
        }
        binding.npDayDateDialog.minValue = 1
    }
}