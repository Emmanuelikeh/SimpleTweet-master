package com.codepath.apps.restclienttemplate

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
class User(var name: String = "", var screenName: String = "", var publicImageUrl: String = ""):
Parcelable {

    companion object {
        fun fromJson(jsonObject: JSONObject): User {
            val user = User()
            user.name = jsonObject.getString("name")
            user.screenName = jsonObject.getString("screen_name")
            user.publicImageUrl = jsonObject.getString("profile_image_url_https")

            return user
        }
    }
}