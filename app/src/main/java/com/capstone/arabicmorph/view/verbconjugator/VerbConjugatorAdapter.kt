package com.capstone.arabicmorph.view.verbconjugator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.arabicmorph.data.JsonMember3
import com.capstone.arabicmorph.data.JsonMember9
import com.capstone.arabicmorph.data.SuggestItem
import com.capstone.arabicmorph.databinding.ConjugatorResultItemBinding

class ConjugationAdapter : RecyclerView.Adapter<ConjugationAdapter.ConjugationViewHolder>() {

    private var suggestItems: List<SuggestItem> = emptyList()
    private var jsonMember9List: List<JsonMember9> = emptyList()
    private var jsonMember3List: List<JsonMember3> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConjugationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ConjugatorResultItemBinding.inflate(inflater, parent, false)
        return ConjugationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConjugationViewHolder, position: Int) {
        val suggestItem = suggestItems[position]
        val jsonMember9 = jsonMember9List.getOrNull(position)
        val jsonMember3 = jsonMember3List.getOrNull(position)

        holder.bind(suggestItem, jsonMember9, jsonMember3)
    }

    override fun getItemCount(): Int {
        return suggestItems.size
    }

    fun setData(
        suggestItems: List<SuggestItem>,
        jsonMember9List: List<JsonMember9>,
        jsonMember3List: List<JsonMember3>
    ) {
        this.suggestItems = suggestItems
        this.jsonMember9List = jsonMember9List
        this.jsonMember3List = jsonMember3List
    }

    inner class ConjugationViewHolder(private val binding: ConjugatorResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestItem: SuggestItem, jsonMember9: JsonMember9?, jsonMember3: JsonMember3?) {
            binding.verbText.text = suggestItem.verb
            binding.harakaText.text = suggestItem.haraka
            binding.transitiveText.text = suggestItem.transitive.toString()

            jsonMember9?.let {
                binding.madhiText.text = it.jsonMember1
                binding.mudhoriText.text = it.jsonMember2
            }

            jsonMember3?.let {
                binding.amarText.text = it.jsonMember6
            }
        }
    }
}
