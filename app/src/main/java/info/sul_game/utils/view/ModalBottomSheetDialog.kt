package info.sul_game.utils.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import info.sul_game.R
import info.sul_game.databinding.DialogUniversityBinding
import info.sul_game.recyclerview.UniversityItem
import info.sul_game.recyclerview.UniversityAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.text.Collator
import java.util.Locale

class ModalBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogUniversityBinding
    private lateinit var universityNames: List<String>
    private lateinit var sortedUniversityItemNames: ArrayList<UniversityItem>
    var onUniversitySelected: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DialogUniversityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUniversityNames()
        sortedUniversityItemNames = ArrayList()
        val groupedUniversities = sortAndGroupUniversities(universityNames)

        groupedUniversities.forEach { name ->
            if (name.length == 1 && name[0] in 'ㄱ'..'ㅎ') {
                // 초성인 경우
                sortedUniversityItemNames.add(
                    UniversityItem(
                        0,
                        name
                    )
                ) // "header" 타입을 추가하여 구분
            } else {
                // 대학 이름인 경우
                sortedUniversityItemNames.add(UniversityItem(1, name)) // "item" 타입으로 구분
            }
        }

        val adapter = UniversityAdapter(sortedUniversityItemNames)
        adapter.onItemClick = { selectedUniversity ->
            onUniversitySelected?.invoke(selectedUniversity)
            dismiss() // 다이얼로그 닫기
        }

        binding.rvUniversityUniversityDialog.adapter = adapter
        binding.rvUniversityUniversityDialog.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // 검색 버튼 클릭 이벤트 처리
//        searchButton.setOnClickListener {
//            val query = searchEditText.text.toString()
//            // 검색 로직 구현 (예: 검색 결과 필터링, 서버 요청 등)
//            // 예시로 검색어를 출력
//            if (query.isNotEmpty()) {
//                // 예시: 검색어를 사용한 검색 동작 수행
//                performSearch(query)
//            } else {
//                // 검색어가 비어 있을 때의 처리
//                showEmptySearchAlert()
//            }
//        }
//
//        // 취소 버튼 클릭 시 다이얼로그 닫기
//        val cancelButton: Button = view.findViewById(R.id.btn_cancel)
//        cancelButton.setOnClickListener {
//            dismiss() // 다이얼로그 닫기
//        }
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

    // 대학 이름들을 정렬하고 초성 변경 시 초성을 리스트에 추가하는 함수
    private fun sortAndGroupUniversities(universityNames: List<String>): List<String> {
        // 정렬을 위해 한국어 Collator 사용
        val collator = Collator.getInstance(Locale.KOREAN)

        // 대학 이름을 초성 기준으로 정렬
        val sortedNames = universityNames.sortedWith(collator)

        // 결과를 저장할 리스트
        val result = mutableListOf<String>()
        var previousInitial = ""

        // 초성을 추출하고 그룹화
        for (name in sortedNames) {
            val initial = getInitialSound(name)

            // 초성이 이전과 다르면 리스트에 추가
            if (initial != previousInitial) {
                result.add(initial)
                previousInitial = initial
            }

            // 대학 이름 추가
            result.add(name)
        }
        return result
    }

    // 문자열에서 첫 번째 글자의 초성을 추출하는 함수
    private fun getInitialSound(text: String): String {
        val initial = text.firstOrNull() ?: return ""

        return when (initial) {
            in '가'..'깋' -> "ㄱ"
            in '나'..'닣' -> "ㄴ"
            in '다'..'딯' -> "ㄷ"
            in '라'..'맇' -> "ㄹ"
            in '마'..'밓' -> "ㅁ"
            in '바'..'빟' -> "ㅂ"
            in '사'..'싷' -> "ㅅ"
            in '아'..'잏' -> "ㅇ"
            in '자'..'짛' -> "ㅈ"
            in '차'..'칳' -> "ㅊ"
            in '카'..'킿' -> "ㅋ"
            in '타'..'팋' -> "ㅌ"
            in '파'..'핗' -> "ㅍ"
            in '하'..'힣' -> "ㅎ"
            else -> initial.toString() // 한글이 아닐 경우 문자 그대로 반환
        }
    }


    private fun performSearch(query: String) {
        // 검색 기능을 수행하는 로직 작성
        // 예시: 입력된 검색어를 이용해 필터링하거나 서버에 요청 보내기
        println("Searching for: $query")
    }

    private fun showEmptySearchAlert() {
        // 빈 검색어에 대한 알림 또는 토스트 메시지 표시
        // 예: Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
        println("검색어를 입력해주세요.")
    }

    companion object {
        const val TAG = "BasicBottomModalSheet"
    }
}