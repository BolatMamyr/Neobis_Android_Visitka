package com.example.visitka

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doctor(
    val name: String,
    val lastName: String
) : Parcelable