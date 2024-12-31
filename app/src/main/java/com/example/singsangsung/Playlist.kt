package com.example.singsangsung

data class Playlist (
    val id : Int,
    val name : String,
    val imageName : String = "",
    val checkedMusic : List<Int> = emptyList()
)