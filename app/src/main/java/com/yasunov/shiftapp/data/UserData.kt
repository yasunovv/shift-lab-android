package com.yasunov.shiftapp.data

import java.util.Date

data class UserData(
    val firstName: String,
    val secondName: String,
    val selectedDate: Date,
    val password: String
)