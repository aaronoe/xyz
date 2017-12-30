package de.aaronoe.xyz.model


data class MessagingToken(val token : String,
                          val timestamp: Long) {

    constructor() : this(
            token = "",
            timestamp = 0L
    )

    constructor(token: String) : this(
            token = token,
            timestamp = System.currentTimeMillis()
    )

}