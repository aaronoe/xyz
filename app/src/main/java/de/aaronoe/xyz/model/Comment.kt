package de.aaronoe.xyz.model

import java.util.*

data class Comment(val author: User,
                   val commentText: String,
                   val id: String,
                   val timestamp: Long) {

    constructor(author: User,
                commentText: String) : this(
            author = author,
            commentText = commentText,
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis()
    )

    /**
     * This empty constructor is required for Firestore's reflection initialization
     */
    constructor() : this(
            author = User(),
            commentText = "",
            id = "",
            timestamp = 0L
    )

}