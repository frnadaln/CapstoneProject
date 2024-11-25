package com.capstone.arabicmorph

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VerbConjugatorFragment : Fragment() {

    private lateinit var initialLayout: RelativeLayout
    private lateinit var resultLayout: LinearLayout
    private lateinit var imageStack: FrameLayout
    private lateinit var helpSection: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConjugatorResultAdapter
    private lateinit var searchButton: View
    private lateinit var searchInput: EditText
    private val data = mutableListOf<Array<String>>()

    interface DrawerToggleListener {
        fun onMenuIconClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_verb_conjugator, container, false)

        initialLayout = view.findViewById(R.id.layout_initial)
        resultLayout = view.findViewById(R.id.result_layout)
        imageStack = view.findViewById(R.id.image_stack)
        helpSection = view.findViewById(R.id.help_section)
        recyclerView = view.findViewById(R.id.recycler_view_results)
        searchButton = view.findViewById(R.id.search_button_background)
        searchInput = view.findViewById(R.id.enter_word)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ConjugatorResultAdapter(data)
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                performSearch(query)
            } else {
                Toast.makeText(context, "Masukkan kata untuk mencari!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun performSearch(query: String) {
        data.clear()

        if (query == "أكل") {
            data.add(arrayOf("أَكَلْتَ", "تَأْكُلُ", "كُلْ"))
            data.add(arrayOf("أَكَلْتِ", "تَأْكُلِينَ", "كُلِي"))
        } else {
            data.add(arrayOf("Hasil tidak ditemukan", "-", "-"))
        }

        adapter.notifyDataSetChanged()
        showResultsLayout()
    }

    private fun showResultsLayout() {
        imageStack.visibility = View.GONE
        helpSection.visibility = View.GONE
        resultLayout.visibility = View.VISIBLE
    }
}
