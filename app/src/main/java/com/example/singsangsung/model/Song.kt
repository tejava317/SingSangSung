package com.example.singsangsung.model

data class Song(
    val id: Int,
    val title: String ="",
    val artist: String="",
    val duration: String="",
    val imageUrl: String="",
    var isChecked: Boolean=false
)