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
                                               val followerCount: Int,
                                               val followingCount: Int) {

    constructor() : this(
            userName = "",
            email = "",
            pictureUrl = "",
            userId = "",
            followingCount = 0,
            followerCount = 0
    )

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