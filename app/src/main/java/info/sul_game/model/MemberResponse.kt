package info.sul_game.model

import java.util.Date

data class MemberResponse (
    val likedOfficialGames : List<BasePost>,
    val likedCreationGames : List<BasePost>,
    val likedIntros : List<BasePost>,

    // BasePost
    val createdDate: Date,
    val updatedDate: Date,
    val isDeleted : Boolean,
    val isUpdated : Boolean,
    val basePostId : Int,
    val title : String,
    val introduction : String,
    val description : String,
    val likes : Int,
    val likedMemberIds : List<Int>,
    val views : Int,
    val reportedCount : Int,
    val member: Member,
    val dailyScore : Int,
    val weeklyScore : Int,
    val sourceType : String,
    val thumbnailIcon : String,
    val creatorInfoIsPrivate : Boolean,

    val Member : Member,

    //Bookmark
    val bookmarkedOfficialGameIds : List<BasePost>,
    val bookmarkedCreationGameIds : List<BasePost>,
    val bookmarkedIntroIds : List<BasePost>,

    //LikedPost
    val likedOfficalGames : List<BasePost>,
    val likedCreationGames : List<BasePost>,
    val likedIntros : List<BasePost>,

    val member: Member,
    val memberInteraction: MemberInteraction
)