package com.isidroid.squareoffsdk.network.response

import com.google.gson.annotations.SerializedName

// request
data class RegisterRequest(@SerializedName("registration") val registration: RegistrationRequest)

data class RegistrationRequest(@SerializedName("name") val name: String,
                               @SerializedName("email") val email: String,
                               @SerializedName("password") val password: String,
                               @SerializedName("password_confirmation") val pwdConfirmation: String,
                               @SerializedName("terms_and_conditions") val termsConditions: String,
                               @SerializedName("avatar") val avatar: String? = null,
                               @SerializedName("username") val username: String? = null,
                               @SerializedName("about_me") val about: String? = null,
                               @SerializedName("side_a_color") val aColor: String? = null,
                               @SerializedName("side_b_color") val bColor: String? = null,
                               @SerializedName("twitter_handle") val twitter: String? = null,
                               @SerializedName("font_family") val font: String? = null)

// response
data class RegisterResponse(val i: String)