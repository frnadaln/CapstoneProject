package com.capstone.arabicmorph

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VerbConjugatorFragment : Fragment() {

    private lateinit var initialLayout: RelativeLayout
    private lateinit var resultLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConjugatorResultAdapter
    private lateinit var floatingButton: ImageView
    private val data = mutableListOf<Array<String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_verb_conjugator, container, false)

        initialLayout = view.findViewById(R.id.layout_initial)
        resultLayout = view.findViewById(R.id.result_layout)
        recyclerView = view.findViewById(R.id.recycler_view_results)
        floatingButton = view.findViewById(R.id.floating_button)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ConjugatorResultAdapter(data)
        recyclerView.adapter = adapter

        val searchBar: EditText = view.findViewById(R.id.enter_word)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    performSearch(query)
                } else {
                    showInitialLayout()
                }
            }
        })

        floatingButton.setOnClickListener {
            Toast.makeText(context, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun performSearch(query: String) {
        data.clear()

        if (query == "أكل") {
            data.add(arrayOf("أَكَلْتَ", "تَأْكُلُ", "كُلْ"))
            data.add(arrayOf("أَكَلْتِ", "تَأْكُلِينَ", "كُلِي"))
            data.add(arrayOf("أَكَلْ", "تَأْكُلَانِ", "كُلَا"))
            data.add(arrayOf("أَكَلْنَا", "نَأْكُلُ", "لْنَا"))
            data.add(arrayOf("أَكَلْتُم", "تَأْكُلُونَ", "كُلُوا"))
            data.add(arrayOf("أَكَلْتُنَّ", "تَأْكُلْنَ", "كُلْنَ"))
            data.add(arrayOf("أَكَلُوا", "يَأْكُلُونَ", "لُوا"))
            data.add(arrayOf("أَكَلْنَ", "يَأْكُلْنَ", "كُنَ"))
            data.add(arrayOf("...", "...", "..."))
            data.add(arrayOf("...", "...", "..."))
            data.add(arrayOf("...", "...", "..."))
        }

        adapter.notifyDataSetChanged()
        showResultsLayout()
    }

    private fun showInitialLayout() {
        initialLayout.visibility = View.VISIBLE
        resultLayout.visibility = View.GONE
    }

    private fun showResultsLayout() {
        initialLayout.visibility = View.GONE
        resultLayout.visibility = View.VISIBLE
    }
}
