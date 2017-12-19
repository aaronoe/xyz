package de.aaronoe.xyz.model

data class Comment(val author: User,
                   val commentText: String,
                   val id: String,
                   val timestamp: Long) {

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