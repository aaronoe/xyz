package de.aaronoe.xyz.model

import com.google.firebase.auth.FirebaseUser

/**
 * Outgoing Collections possibly include:
 * - Posts
 * - Liked Posts
 * - Mentioned Posts?
 * - Messaging Tokens
 * - Followers
 * - Following
 **/

data class User(val userName : String,
                val email: String,
                val pictureUrl: String,
                val userId: String,
                val followerCount: Int,
                val followingCount: Int) {

    /**
     * Used when the user first signs in
     */
    constructor(firebaseUser: FirebaseUser) : this(
            userName = firebaseUser.displayName.toString(),
            email = firebaseUser.email.toString(),
            pictureUrl = firebaseUser.photoUrl.toString(),
            userId = firebaseUser.uid,
            followerCount =  0,
            followingCount = 0
    )
}