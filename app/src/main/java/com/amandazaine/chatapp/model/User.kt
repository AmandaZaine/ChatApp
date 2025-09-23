package com.amandazaine.chatapp.model

data class User(
    var id: String,
    var name: String,
    var email: String,
    var image: String? = ""
)
