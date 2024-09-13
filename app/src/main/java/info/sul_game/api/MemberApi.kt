package info.sul_game.api

import info.sul_game.model.Member
import info.sul_game.model.MemberRequest
import info.sul_game.model.MemberResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MemberApi {
    @POST("members/check-nickname")
    fun checkNickName(
        @Header("Authorization") token: String,
        @Body MemberRequest: MemberRequest
    ): Call<MemberResponse>

    // 마이페이지
    @GET("members/profile")
    fun getMemberProfile(
        @Header("Authorization") token: String,
        @Body MemberRequest: MemberRequest
    ): Call<MemberResponse>

    // 좋아요한 글
    @GET("members/liked-post")
    fun getLikedPosts(
        @Header("Authorization") token: String,
        @Body MemberRequest: MemberRequest
    ): Call<MemberResponse>

    // 즐겨찾기한 글
    @GET("members/bookmarked-post")
    fun getBookmarkedPosts(
        @Header("Authorization") token: String,
        @Body MemberRequest: MemberRequest
    ): Call<MemberResponse>

    // 회원 닉네임 업데이트
    @Multipart
    @POST("members/nickname")
    fun updateNickname(
        @Part("memberId") memberId: RequestBody,
        @Part("nickname") nickname: RequestBody
    ): Call<Member>

    // 회원 프로필 이미지 업데이트
    @Multipart
    @POST("members/profile-image")
    fun updateProfileImage(
        @Part("memberId") memberId: RequestBody,
        @Part("multipartFile") multipartFile: MultipartBody.Part
    ): Call<Member>
}