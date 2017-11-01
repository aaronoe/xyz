package de.aaronoe.xyz.model

data class Comment(val author: User,
                   val commentText: String,
                   val id: String,
                   val timestamp: Long)