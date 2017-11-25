package de.aaronoe.xyz.model

import com.google.firebase.auth.FirebaseUser
import org.parceler.Parcel
import org.parceler.ParcelConstructor

/**
 * Outgoing Collections possibly include:
 * - Posts
 * - Liked Posts
 * - Mentioned Posts?
 * - Messaging Tokens
 * - Followers
 * - Following
 **/

@Parcel(Parcel.Serialization.BEAN)
data class User @ParcelConstructor constructor(val userName : String,
                                               val email: String,
                                               val pictureUrl: String,
                                               val userId: String,
                                               val postCount: Int,
                                               val followerCount: Int,
                                               val followingCount: Int,
                                               val location: String,
                                               val bio: String) {

    constructor() : this(
            userName = "",
            email = "",
            pictureUrl = "",
            userId = "",
            postCount = 0,
            followingCount = 0,
            followerCount = 0,
            location = "",
            bio = ""
    )

    /**
     * Used when the user first signs in
     */
    constructor(firebaseUser: FirebaseUser) : this(
            userName = firebaseUser.displayName.toString(),
            email = firebaseUser.email.toString(),
            pictureUrl = firebaseUser.photoUrl.toString(),
            userId = firebaseUser.uid,
            postCount = 0,
            followerCount =  0,
            followingCount = 0,
            location = "",
            bio = ""
    )
}