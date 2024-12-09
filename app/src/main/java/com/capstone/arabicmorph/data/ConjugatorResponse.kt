package com.capstone.arabicmorph.data

data class ConjugatorResponse(
	val result: Map<String, Map<String, String>>,
	val suggest: List<Suggestion>,
	val verbInfo: VerbInfo
)

data class Suggestion(
	val future: String,
	val haraka: String,
	val transitive: Boolean,
	val verb: String
)

data class VerbInfo(
	val verb: String,
	val future: String,
	val haraka: String,
	val transitive: Boolean
)