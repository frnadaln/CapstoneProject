package com.capstone.arabicmorph.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class JamidResponse(
	val prediction: String,
	val text: String
) : Parcelable
