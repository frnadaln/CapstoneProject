package com.capstone.arabicmorph.data

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ConjugatorResponse(

	@field:SerializedName("verb_info")
	val verbInfo: String,

	@field:SerializedName("suggest")
	val suggest: List<SuggestItem>,

	@field:SerializedName("json_member_9_list")
	val jsonMember9List: List<JsonMember9>,

	@field:SerializedName("json_member_3_list")
	val jsonMember3List: List<JsonMember3>
) : Parcelable

@Parcelize
data class SuggestItem(

	@field:SerializedName("future")
	val future: String,

	@field:SerializedName("verb")
	val verb: String,

	@field:SerializedName("haraka")
	val haraka: String,

	@field:SerializedName("transitive")
	val transitive: Boolean
) : Parcelable

@Parcelize
data class JsonMember3(

	@field:SerializedName("6")
	val jsonMember6: String
) : Parcelable

@Parcelize
data class JsonMember9(

	@field:SerializedName("1")
	val jsonMember1: String,

	@field:SerializedName("2")
	val jsonMember2: String
) : Parcelable