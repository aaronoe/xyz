package de.aaronoe.xyz.model

import de.aaronoe.xyz.model.enums.PostType

/**
 * Possible Collections in Firestore:
 * - Likes
 * - Comments
 */
data class Post(val id : String,
                val author: User,
                val type: PostType,
                val mediaUrl: String,
                val numberOfLikes: Int,
                val numberOfComments: String,
                val timestamp: Long,
                val caption: String)
