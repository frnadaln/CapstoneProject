package com.capstone.arabicmorph.view.verbconjugator

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.arabicmorph.data.JsonMember3
import com.capstone.arabicmorph.data.JsonMember9
import com.capstone.arabicmorph.data.SuggestItem
import com.capstone.arabicmorph.databinding.ConjugatorResultItemBinding

class ConjugationAdapter : RecyclerView.Adapter<ConjugationAdapter.ConjugationViewHolder>() {

    private var verbInfo: String = ""
    private var suggestItems: List<SuggestItem> = emptyList()
    private var jsonMember9List: List<JsonMember9> = emptyList()
    private var jsonMember3List: List<JsonMember3> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConjugationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ConjugatorResultItemBinding.inflate(inflater, parent, false)
        return ConjugationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConjugationViewHolder, position: Int) {
        val verbInfo = String()
        val suggestItem = suggestItems[position]
        val jsonMember9 = jsonMember9List.getOrNull(position)
        val jsonMember3 = jsonMember3List.getOrNull(position)

        Log.d("ConjugationAdapter", "Binding data for verb: ${suggestItem.verb}")
        holder.bind(suggestItem, jsonMember9, jsonMember3, verbInfo)
    }

    override fun getItemCount(): Int {
        return suggestItems.size
    }

    fun setData(
        verbInfo: String,
        suggestItems: List<SuggestItem>?,
        jsonMember9List: List<JsonMember9>?,
        jsonMember3List: List<JsonMember3>?

    ) {
        if (suggestItems != null) {
            Log.d("ConjugationAdapter", "Setting data: suggestItems size = ${suggestItems.size}")
        }
        this.verbInfo = verbInfo
        this.suggestItems = suggestItems ?: emptyList()
        this.jsonMember9List = jsonMember9List ?: emptyList()
        this.jsonMember3List = jsonMember3List ?: emptyList()

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataJson(jsonMember9List: List<JsonMember9>?) {
        this.jsonMember9List = jsonMember9List ?: emptyList()
        notifyDataSetChanged()
    }

    inner class ConjugationViewHolder(private val binding: ConjugatorResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestItem: SuggestItem, jsonMember9: JsonMember9?, jsonMember3: JsonMember3?, verbInfo: String) {
            binding.verbInfo.text = verbInfo.ifEmpty { "-" }
            binding.verbText.text = suggestItem.verb
            binding.futureText.text = suggestItem.future

            binding.madhiText.text = jsonMember9?.json1 ?: "-"
            binding.mudhoriText.text = jsonMember9?.json2 ?: "-"
            binding.amarText.text = jsonMember3?.json6 ?: "-"
            }
        }
    }
