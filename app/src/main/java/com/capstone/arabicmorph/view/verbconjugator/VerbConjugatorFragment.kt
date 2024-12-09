package com.capstone.arabicmorph.view.verbconjugator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.arabicmorph.R
import com.capstone.arabicmorph.ui.adapters.ConjugationAdapter

class VerbConjugatorFragment : Fragment() {

    private lateinit var conjugatorViewModel: ConjugatorViewModel
    private lateinit var conjugationAdapter: ConjugationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_verb_conjugator, container, false)

        val repository = ConjugatorRepository()
        val viewModelFactory = ConjugatorViewModelFactory(repository)
        conjugatorViewModel = ViewModelProvider(this, viewModelFactory)[ConjugatorViewModel::class.java]

        conjugationAdapter = ConjugationAdapter(emptyList())
        root.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.results_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = conjugationAdapter
        }

        conjugatorViewModel.conjugatorResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                val conjugationList = mutableListOf<List<String>>()

                conjugationList.add(it.result["1"]?.toString()?.split(",")?.toList() ?: emptyList())
                conjugationList.add(it.result["2"]?.toString()?.split(",")?.toList() ?: emptyList())
                conjugationList.add(it.result["3"]?.toString()?.split(",")?.toList() ?: emptyList())

                conjugationAdapter.updateList(conjugationList.flatten().take(13))
                root.findViewById<View>(R.id.result_layout).visibility = View.VISIBLE
            } ?: run {
                Toast.makeText(context, "Word not found", Toast.LENGTH_SHORT).show()
            }
        }

        root.findViewById<View>(R.id.search_icon).setOnClickListener {
            val searchText = root.findViewById<android.widget.EditText>(R.id.enter_word).text.toString().trim()
            if (searchText.isNotEmpty()) {
                conjugatorViewModel.getConjugationResults(searchText)
            } else {
                Toast.makeText(context, getString(R.string.enter_word_to_search), Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }
}