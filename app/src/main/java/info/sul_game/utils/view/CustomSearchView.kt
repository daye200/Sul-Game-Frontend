
package info.sul_game.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import info.sul_game.R
import info.sul_game.databinding.CustomSearchviewBinding

class CustomSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: CustomSearchviewBinding = CustomSearchviewBinding.inflate(
        LayoutInflater.from(context), this, true)

    private val searchEditText: EditText = binding.etSearchSearchview
    private val searchButton: ImageButton = binding.btnSearchSearchview
    private val previousButton: ImageButton = binding.btnPreviousSearchview
    private var queryTextListener: OnQueryTextListener? = null
    private var focusChangeListener: OnFocusChangeListener? = null

    private var showPreviousButton : Boolean = true
    private var useEditText : Boolean = true
    private lateinit var hint : String

    init {
        initView()
        initAttrs(attrs)
    }

    private fun initView(){
//        inflate(context, R.layout.custom_searchview, this)

        searchButton.setOnClickListener {
            queryTextListener?.onQueryTextSubmit(searchEditText.text.toString())
        }

        searchEditText.addTextChangedListener { text ->
            queryTextListener?.onQueryTextChange(text.toString())
        }

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            focusChangeListener?.onFocusChange(hasFocus)
        }
    }

    private fun initAttrs(attrs: AttributeSet?){
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomSearchView,
            0, 0
        ).apply {
            try {
                showPreviousButton = getBoolean(R.styleable.CustomSearchView_showPreviousButton, true)
                hint = getString(R.styleable.CustomSearchView_hint) ?: ""
                useEditText = getBoolean(R.styleable.CustomSearchView_useEditText, true)
            } finally {
                recycle()
            }
        }

        searchEditText.hint = hint
        previousButton.visibility = if(showPreviousButton) View.VISIBLE else View.GONE

        if (!useEditText){
            searchEditText.isClickable = false
            searchEditText.isFocusable = false
            searchEditText.visibility = View.GONE
        }
    }

    fun setOnQueryTextListener(listener: OnQueryTextListener) {
        queryTextListener = listener
    }

    fun setOnFocusChangeListener(listener: OnFocusChangeListener) {
        focusChangeListener = listener
    }

    /**
     * View.setOnClickListener 에서 제공하는 것과 비슷한 역할
     */
    fun setOnBackListener(listener: OnPreviousListener) {
        previousButton.setOnClickListener {
            listener.onClick(it)
        }
    }

    /**
     * Kotlin 에서 작성한 코드는 SAM 을 람다로 받을 수 없기 때문에
     * onClick 메서드를 람다로 받을 수 있도록 오버로딩 했다.
     */
    fun setOnBackListener(action: (view: View) -> Unit) {
        previousButton.setOnClickListener {
            action(it)
        }
    }

    var query: String
        get() = searchEditText.text.toString()
        set(value) {
            searchEditText.setText(value)
        }

    fun setQuery(query: String, submit: Boolean) {
        searchEditText.setText(query)
        if (submit) {
            queryTextListener?.onQueryTextSubmit(query)
        }
    }

    interface OnQueryTextListener {
        fun onQueryTextChange(newText: String): Boolean
        fun onQueryTextSubmit(query: String): Boolean
    }

    interface OnFocusChangeListener {
        fun onFocusChange(hasFocus: Boolean)
    }

    interface OnPreviousListener {
        fun onClick(view: View)
    }
}