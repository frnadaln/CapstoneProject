package com.capstone.arabicmorph

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConjugatorResultAdapter(private val data: List<Array<String>>) :
    RecyclerView.Adapter<ConjugatorResultAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val column1: TextView = view.findViewById(R.id.column1)
        val column2: TextView = view.findViewById(R.id.column2)
        val column3: TextView = view.findViewById(R.id.column3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.conjugator_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val row = data[position]
        holder.column1.text = row[0]
        holder.column2.text = row[1]
        holder.column3.text = row[2]
    }

    override fun getItemCount(): Int = data.size
}

