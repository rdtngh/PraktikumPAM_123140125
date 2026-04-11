package com.example.a6_123140125.data.model

import com.google.gson.annotations.SerializedName

data class NewsPost(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("imageUrl")
    val imageUrl: String? = null
) {
    fun getSafeImageUrl(): String {
        return if (imageUrl.isNullOrEmpty()) {
            "https://picsum.photos/seed/${id}/400/300"
        } else {
            imageUrl
        }
    }
}
