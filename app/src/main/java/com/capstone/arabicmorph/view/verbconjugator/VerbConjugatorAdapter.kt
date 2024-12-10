package com.capstone.arabicmorph.view.verbconjugator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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
        val item = suggestItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = suggestItems.size

    fun submitList(
        newSuggestItems: List<SuggestItem>,
        newJsonMember9List: List<JsonMember9>,
        newJsonMember3List: List<JsonMember3>
    ) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = suggestItems.size
            override fun getNewListSize(): Int = newSuggestItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return suggestItems[oldItemPosition].verb == newSuggestItems[newItemPosition].verb
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return suggestItems[oldItemPosition] == newSuggestItems[newItemPosition]
            }
        })
        diffResult.dispatchUpdatesTo(this)

        // Menyimpan data yang diterima
        suggestItems = newSuggestItems
        jsonMember9List = newJsonMember9List
        jsonMember3List = newJsonMember3List
    }

    class ConjugationViewHolder(private val binding: ConjugatorResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: SuggestItem) {
            binding.verbText.text = item.verb
            binding.harakaText.text = item.haraka
            binding.transitiveText.text = item.transitive.toString()

            binding.madhiText.text = "-"
            binding.mudhoriText.text = "-"
            binding.amarText.text = "-"
        }
    }
}
