package info.sul_game.model

import java.util.Date

data class Member(
    val createdDate: Date,
    val updatedDate: Date,
    val isDeleted: Boolean,
    val memberId: Long,
    val email: String,
    val nickname: String,
    val birthDate: String,
    val profileUrl: String,
    val role: String,
    val college: String,
    val exp: Int,
    val expLevel: String,
    val isUniversityPublic: Boolean,
    val accountStatus: String,
    val isNotificationEnabled: Boolean,
    val infoPopupVisible: Boolean,
    val lastLoginTime: Date
)