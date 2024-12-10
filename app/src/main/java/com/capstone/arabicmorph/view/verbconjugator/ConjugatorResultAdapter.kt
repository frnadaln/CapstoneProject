package com.capstone.arabicmorph.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.arabicmorph.R

class ConjugationAdapter(private var data: List<String>) :
    RecyclerView.Adapter<ConjugationAdapter.ConjugationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConjugationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conjugation, parent, false)
        return ConjugationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConjugationViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateList(newData: List<String>) {
        data = newData
        notifyDataSetChanged()
    }

    class ConjugationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.conjugation_text)

        fun bind(item: String) {
            textView.text = item
        }
    }
}
