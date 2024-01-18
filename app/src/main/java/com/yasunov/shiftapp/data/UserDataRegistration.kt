package com.yasunov.shiftapp.data

import java.util.Date


data class UserDataRegistration(
    val firstName: String = "",
    val secondName: String = "",
    val selectedDate: Date = Date(Date().time - 1000 * 60 * 60 * 24 * 30L),
    val firstPassword: String = "",
    val secondPassword: String = "",
)
