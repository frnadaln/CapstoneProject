package com.capstone.arabicmorph.view.verbconjugator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.arabicmorph.data.SuggestItem
import com.capstone.arabicmorph.databinding.ConjugatorResultItemBinding

class ConjugationAdapter : RecyclerView.Adapter<ConjugationAdapter.ConjugationViewHolder>() {

    private var data: List<SuggestItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConjugationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ConjugatorResultItemBinding.inflate(inflater, parent, false)
        return ConjugationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConjugationViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size

    fun submitList(newData: List<SuggestItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = data.size
            override fun getNewListSize(): Int = newData.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition].verb == newData[newItemPosition].verb
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == newData[newItemPosition]
            }
        })
        diffResult.dispatchUpdatesTo(this)
        data = newData
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

            binding.madhiText.text = "Madhi"
            binding.mudhoriText.text = "Mudhori"
            binding.amarText.text = "Amar"

        }
    }
}