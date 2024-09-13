package info.sul_game.model

data class MemberInteraction(
    val createdDate: String,
    val updatedDate: String,
    val isDeleted: Boolean,
    val id: Long,
    val totalLikeCount: Int,
    val totalCommentCount: Int,
    val totalPostCount: Int,
    val totalCommentLikeCount: Int,
    val totalPostLikeCount: Int,
    val totalMediaCount: Int,
    val likedOfficialGameIds: List<Long>,
    val likedCreationGameIds: List<Long>,
    val likedIntroIds: List<Long>,
    val bookmarkedOfficialGameIds: List<Long>,
    val bookmarkedCreationGameIds: List<Long>,
    val bookmarkedIntroIds: List<Long>
)