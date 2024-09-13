package info.sul_game.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import info.sul_game.databinding.ActivityLoginBinding
import info.sul_game.utils.TokenUtil
import org.json.JSONObject

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebView()

        binding.btnCloseLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnGoogleLogin.setOnClickListener {
            binding.clContainer1Login.visibility = View.INVISIBLE
            binding.wvSocialLoginLogin.visibility = View.VISIBLE
            binding.wvSocialLoginLogin.loadUrl("https://api.sul-game.info/oauth2/authorization/google")
            Log.d("술겜위키", "Google 로그인 시도")
        }

        binding.btnKakaoLogin.setOnClickListener {
            binding.clContainer1Login.visibility = View.INVISIBLE
            binding.wvSocialLoginLogin.visibility = View.VISIBLE
            binding.wvSocialLoginLogin.loadUrl("https://api.sul-game.info/oauth2/authorization/kakao")
            Log.d("술겜위키", "Kakao 로그인 시도")
        }
    }

    @SuppressLint("JavascriptInterface")
    private fun setupWebView() {
        binding.wvSocialLoginLogin.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        // JavaScript Interface 추가
        binding.wvSocialLoginLogin.addJavascriptInterface(this, "Android")

        binding.wvSocialLoginLogin.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("술겜위키", "페이지 로딩 완료: $url")

                if (url?.contains("/login/oauth2/code/") == true) {
                    view?.evaluateJavascript(
                        """
                        (function() {
                            var req = new XMLHttpRequest();
                            req.open('GET', window.location.href, false);
                            req.send(null);
                            var headers = req.getAllResponseHeaders();
                            Android.processHeaders(headers);
                        })();
                        """.trimIndent(), null
                    )
                }

                view?.evaluateJavascript(
                    "(function() { return document.querySelector('pre').textContent; })();",
                    { result ->
                        Log.d("술겜위키", "응답 내용: $result")
                        if (result != "null" && result.isNotEmpty()) {
                            try {
                                // JSON 문자열 파싱 시 불필요한 이스케이프 문자 제거
                                val cleanedResult = result.trim('"').replace("\\\"", "\"")
                                val jsonObject = JSONObject(cleanedResult)
                                processLoginResponse(jsonObject)
                            } catch (e: Exception) {
                                Log.e("술겜위키", "JSON 파싱 오류: ${e.message}\n원본 데이터: $result", e)
                            }
                        }
                    }
                )
            }

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                val url = request?.url?.toString()
                Log.d("술겜위키", "Intercepted URL: $url")

                request?.requestHeaders?.forEach { (key, value) ->
                    Log.d("술겜위키", "요청 Header: $key = $value")
//                    setAccessToken(value)
                }

                return super.shouldInterceptRequest(view, request)
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
                Log.e("술겜위키", "HTTP 에러: ${errorResponse?.statusCode} - ${errorResponse?.reasonPhrase}")
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)

                view?.evaluateJavascript(
                    "(function() { return document.body.innerText; })();",
                    { result ->
                        Log.d("술겜위키", "Raw JavaScript result: $result")
                        if (result.startsWith("\"") && result.endsWith("\"")) {
                            val jsonString = result.substring(1, result.length - 1).replace("\\\"", "\"")
                            Log.d("술겜위키", "Parsed JSON string: $jsonString")
                            try {
                                if(jsonString.isNotEmpty()){
                                    val jsonObject = JSONObject(jsonString)
                                    processLoginResponse(jsonObject)
                                }
                            } catch (e: Exception) {
                                Log.e("술겜위키", "JSON 파싱 오류: ${e.message}\n원본 데이터: $jsonString", e)
                            }
                        }
                    }
                )
            }

        }
    }

    private fun processLoginResponse(jsonObject: JSONObject) {
        Log.d("술겜위키", "Received JSON: $jsonObject")
//        accessToken()
        val refreshToken = jsonObject.optString("refreshToken")
        val loginAccountStatus = jsonObject.optString("loginAccountStatus")
        val nickname = jsonObject.optString("nickname")
        val email = jsonObject.optString("email")

        Log.d("술겜위키", "Parsed data: refreshToken=$refreshToken, loginAccountStatus=$loginAccountStatus, nickname=$nickname, email=$email")

        if (refreshToken.isNotEmpty()) {
            saveRefreshToken(refreshToken)
            // TODO : 나중에 네트워크에서 accesstoken 가져오기 해봐야됨!!!!!!
            saveAccessToken(refreshToken)
            saveUserInfo(loginAccountStatus, nickname, email)

            runOnUiThread {
                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                if(loginAccountStatus == "PENDING"){
                    startActivity(Intent(this, SignUpActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        } else {
            Log.e("술겜위키", "RefreshToken이 비어있습니다.")
        }
    }

    private fun saveAccessToken(accessToken: String) {
        val sharedPreferences = TokenUtil().getEncryptedSharedPreferences(this)
        with(sharedPreferences.edit()) {
            putString("accessToken", accessToken)
            apply()
        }
        Log.d("술겜위키", "AccessToken 저장됨: $accessToken")
    }

    private fun saveRefreshToken(refreshToken: String) {
        val sharedPreferences = TokenUtil().getEncryptedSharedPreferences(this)
        with(sharedPreferences.edit()) {
            putString("refreshToken", refreshToken)
            apply()
        }
        Log.d("술겜위키", "RefreshToken 저장됨: $refreshToken")
    }

    private fun saveUserInfo(loginAccountStatus: String, nickname: String, email: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("loginAccountStatus", loginAccountStatus)
            putString("nickname", nickname)
            putString("email", email)
            apply()
        }
    }
}