package com.capstone.arabicmorph.view.verbconjugator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.arabicmorph.data.SuggestItem
import com.capstone.arabicmorph.databinding.ConjugatorResultItemBinding

class ConjugationAdapter : RecyclerView.Adapter<ConjugationAdapter.ConjugationViewHolder>() {

    private var suggestItems: List<SuggestItem> = emptyList()
    private var result: Map<String, Map<String, String>> = emptyMap()
    private var verbInfo: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConjugationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ConjugatorResultItemBinding.inflate(inflater, parent, false)
        return ConjugationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConjugationViewHolder, position: Int) {
        suggestItems[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int {
        return suggestItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(
        verbInfo: String,
        suggestItems: List<SuggestItem>?,
        result: Map<String, Map<String, String>>?
    ) {
        this.verbInfo = verbInfo
        this.suggestItems = suggestItems ?: emptyList()
        this.result = result ?: emptyMap()

        notifyDataSetChanged()
    }

    inner class ConjugationViewHolder(private val binding: ConjugatorResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Map<String, Map<String, String>>) {
            binding.verbInfo.text = verbInfo.ifEmpty { "-" }

            val jsonMember9 = result["9"]
            val jsonMember3 = result["3"]

            binding.madhiText.text = jsonMember9?.get("1") ?: "-"
            binding.mudhoriText.text = jsonMember9?.get("2") ?: "-"
            binding.amarText.text = jsonMember3?.get("6") ?: "-"

            binding.futureText1.text = suggestItems.getOrNull(0)?.future ?: "-"
            binding.harakaText1.text = suggestItems.getOrNull(0)?.haraka ?: "-"
            binding.transitiveText1.text = suggestItems.getOrNull(0)?.transitive.toString()
            binding.verbText1.text = suggestItems.getOrNull(0)?.verb ?: "-"

            binding.futureText2.text = suggestItems.getOrNull(1)?.future ?: "-"
            binding.harakaText2.text = suggestItems.getOrNull(1)?.haraka ?: "-"
            binding.transitiveText2.text = suggestItems.getOrNull(1)?.transitive.toString()
            binding.verbText2.text = suggestItems.getOrNull(1)?.verb ?: "-"

            binding.futureText3.text = suggestItems.getOrNull(2)?.future ?: "-"
            binding.harakaText3.text = suggestItems.getOrNull(2)?.haraka ?: "-"
            binding.transitiveText3.text = suggestItems.getOrNull(2)?.transitive.toString()
            binding.verbText3.text = suggestItems.getOrNull(2)?.verb ?: "-"

            binding.futureText4.text = suggestItems.getOrNull(3)?.future ?: "-"
            binding.harakaText4.text = suggestItems.getOrNull(3)?.haraka ?: "-"
            binding.transitiveText4.text = suggestItems.getOrNull(3)?.transitive.toString()
            binding.verbText4.text = suggestItems.getOrNull(3)?.verb ?: "-"
        }
    }
}