package info.sul_game.activity

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import info.sul_game.R
import info.sul_game.utils.view.DateDialog
import info.sul_game.utils.view.ModalBottomSheetDialog
import info.sul_game.utils.view.WarningDialog
import info.sul_game.api.MemberApi
import info.sul_game.databinding.ActivitySignUpBinding
import info.sul_game.utils.TokenUtil
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var previousName: String = ""
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var universityNames: List<String>
    private lateinit var checkBoxList: List<CheckBox>
    private var isAllCheckBoxChecked = false
    private var isPermissionState = false
    private lateinit var memberApi: MemberApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUniversityNames()

        binding.btnCloseSignup.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.etNameSignup.addTextChangedListener {
            val maxLength = "/15"
            binding.tvCountLengthSignup.text =
                binding.etNameSignup.text.length.toString() + maxLength

            if(binding.etNameSignup.text.toString() == previousName && previousName != ""){
                binding.tvHint1Signup.visibility = View.INVISIBLE
                binding.tvHint2Signup.visibility = View.VISIBLE
                binding.tvHint3Signup.visibility = View.INVISIBLE
            } else{
                binding.tvHint1Signup.visibility = View.VISIBLE
                binding.tvHint2Signup.visibility = View.INVISIBLE
                binding.tvHint3Signup.visibility = View.INVISIBLE
            }
            buttonClickable()
        }

        binding.btnCheckNameSignup.setOnClickListener {
            // 사용 가능 닉네임인지 확인
            if (isAvailableName(binding.etNameSignup.text.toString())) {
                binding.tvHint1Signup.visibility = View.INVISIBLE
                binding.tvHint2Signup.visibility = View.VISIBLE
                binding.tvHint3Signup.visibility = View.INVISIBLE
                previousName = binding.etNameSignup.text.toString()
                binding.btnCheckNameSignup.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.main_color))
            } else{
                binding.tvHint1Signup.visibility = View.INVISIBLE
                binding.tvHint2Signup.visibility = View.INVISIBLE
                binding.tvHint3Signup.visibility = View.VISIBLE
            }
            buttonClickable()
        }

        binding.etDateSignup.isFocusable = false
        binding.etDateSignup.isFocusableInTouchMode = false
        binding.etDateSignup.isClickable = true

        binding.etDateSignup.setOnClickListener{
            val dlg = DateDialog(this)
            dlg.listener = object : DateDialog.DateDialogListener {
                override fun onDateSelected(year: Int, month: Int, day: Int) {
                    binding.etDateSignup.setText("$year / $month / $day")
                }
            }
            dlg.show()
        }

        binding.tvUniversitySignup.setOnClickListener{
            val modal = ModalBottomSheetDialog()
            modal.setStyle(DialogFragment.STYLE_NORMAL, R.style.TransParentBottomSheetDialogTheme)
            modal.onUniversitySelected = { universityName ->
                binding.tvUniversitySignup.setText(universityName) // 선택된 대학명을 텍스트뷰에 설정
            }
            modal.show(supportFragmentManager, ModalBottomSheetDialog.TAG)
        }

//        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, universityNames)
//        binding.tvUniversitySignup.setAdapter(adapter)

        binding.tvUniversitySignup.addTextChangedListener {
            buttonClickable()
        }

        checkBoxList = listOf(
            binding.cbPermission2Signup,
            binding.cbPermission3Signup,
            binding.cbPermission4Signup,
            binding.cbPermission5Signup,
            binding.cbPermission6Signup,
            binding.cbPermission7Signup,
        )

        // '모두 동의합니다' 체크박스 클릭 이벤트
        binding.cbPermission1Signup.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isAllCheckBoxChecked = true
                isPermissionState = true
                buttonClickable()
                checkBoxList.forEach { checkBox ->
                    checkBox.isChecked = true
                }
            } else {
                if (isAllCheckBoxChecked) {
                    isAllCheckBoxChecked = false
                    isPermissionState = false
                    buttonClickable()
                    checkBoxList.forEach { checkBox ->
                        checkBox.isChecked = false
                    }
                }
            }
        }

        // 개별 체크박스 클릭 이벤트
        checkBoxList.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                if (!checkBox.isChecked) {
                    isAllCheckBoxChecked = false
                    binding.cbPermission1Signup.isChecked = false
                    isPermissionState = false
                    buttonClickable()
                } else if (checkBoxList.all { it.isChecked }) {
                    isAllCheckBoxChecked = true
                    binding.cbPermission1Signup.isChecked = true
                    isPermissionState = true
                    buttonClickable()
                }
            }
        }

        binding.btnNextSignup.setOnClickListener{
            WarningDialog(this).show()
        }
    }

    /**
     * TODO : 서버랑 합체해야됨
     * 사용 가능 닉네임인지 확인
     */
    private fun isAvailableName(name: String): Boolean {
//        val request = NicknameRequest(nickname = name)
//        memberApi.checkNickName(TokenUtil().getRefreshToken(this@SignUpActivity).toString(), request).enqueue(object :
//            Callback<Void> {
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                if (response.isSuccessful) {
//                    Log.d("API_CALL", "닉네임 업데이트 성공")
//                } else {
//                    Log.e("API_CALL", "닉네임 업데이트 실패: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                Log.e("API_CALL", "API 호출 실패: ${t.message}")
//            }
//        })
        return true
    }

    private fun isAvailableBirth(birth: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy / MM / dd", Locale.US)
            val birthDate = sdf.parse(birth) ?: return false
            val today = Calendar.getInstance()
            val birthCalendar = Calendar.getInstance().apply { time = birthDate }
            var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

            if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            age >= 14
        } catch (e: Exception) {
            false
        }
    }

    private fun showUnderageDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("만 14세 이상만 가입 가능합니다.")
            .setCancelable(false)
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()

        val drawable = GradientDrawable()
        drawable.cornerRadius = 40f
        drawable.setColor(ContextCompat.getColor(this, android.R.color.white))

        alert.window?.setBackgroundDrawable(drawable)

        alert.show()

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.main_color))
    }

    private fun loadUniversityNames() {
        val inputStream = resources.openRawResource(R.raw.university)
        val jsonFile = inputStream.bufferedReader().use { it.readText() }

        val jsonObject = JSONObject(jsonFile)
        val recordsArray: JSONArray = jsonObject.getJSONArray("records")

        universityNames = mutableListOf<String>().apply {
            for (i in 0 until recordsArray.length()) {
                val record: JSONObject = recordsArray.getJSONObject(i)
                add(record.getString("학교명"))
            }
        }
    }

    /**
     * Button Clickable 설정
     * 입력한 닉네임이 제대로 쓰여졌으면 클릭 허용
     */
    private fun buttonClickable() {
        val dateText = binding.etDateSignup.text.toString()
        val isNameAvailable = binding.tvHint2Signup.visibility == View.VISIBLE
        val isBirthValid = isAvailableBirth(dateText)
        val isUniversityValid = binding.tvUniversitySignup.text.toString() in universityNames

        if (isNameAvailable && isBirthValid && isUniversityValid && isPermissionState) {
            binding.btnNextSignup.isEnabled = true
            binding.btnNextSignup.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.main_color)
        } else {
            if (!isBirthValid && dateText.isNotEmpty()) {
                showUnderageDialog()
            }
            binding.btnNextSignup.isEnabled = false
            binding.btnNextSignup.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.light_gray)
        }
    }
}