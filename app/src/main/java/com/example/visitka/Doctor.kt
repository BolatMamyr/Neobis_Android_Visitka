package com.example.visitka

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doctor(
    val name: String,
    val lastName: String,
    val email: String,
    val website: String = "https://neobis.club",
    val phoneNumber: Long = 77471111111,
    val telegramId: String = "bolatmamyr"
) : Parcelable