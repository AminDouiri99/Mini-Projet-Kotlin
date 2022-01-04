package com.example.talentium.Model

data class ProfilePost(
    val _id :String,
    val user:String,
    val image: Int,
    val description: String,
    val source: String,
    val display: Boolean,
    val likes :Array<String>
)
