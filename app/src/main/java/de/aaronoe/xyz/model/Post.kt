package de.aaronoe.xyz.model

import de.aaronoe.xyz.model.enums.PostType
import java.util.*

/**
 * Possible Collections in Firestore:
 * - Likes
 * - Comments
 */
data class Post(val id : String,
                val author: User,
                val type: PostType,
                var mediaUrl: String,
                val numberOfLikes: Int,
                val numberOfComments: Int,
                val timestamp: Long,
                val caption: String) {

    /**
     * This empty constructor is required for Firestore's reflection initialization
     */
    constructor() : this(
            id = "",
            author = User(),
            mediaUrl = "",
            type = PostType.PHOTO,
            numberOfLikes = 0,
            numberOfComments = 0,
            timestamp = 0L,
            caption = ""
    )

    constructor(author: User, mediaUrl: String, caption: String) : this(
            id = UUID.randomUUID().toString(),
            author = author,
            mediaUrl = mediaUrl,
            type = PostType.VIDEO,
            numberOfLikes = 0,
            numberOfComments = 0,
            timestamp = System.currentTimeMillis(),
            caption = caption
    )

}
