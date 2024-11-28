package com.capstone.arabicmorph.view.jamiddetector

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.capstone.arabicmorph.R

class JamidDetectorFragment : Fragment() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var layoutInitial: RelativeLayout
    private lateinit var layoutResult: RelativeLayout
    private lateinit var searchBar: EditText
    private lateinit var searchButton: FrameLayout
    private lateinit var resultWord: TextView
    private lateinit var resultDescription: TextView
    private lateinit var errorMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jamid_detector, container, false)

        drawerLayout = activity?.findViewById(R.id.drawer_layout) ?: throw IllegalStateException("DrawerLayout not found")
        layoutInitial = view.findViewById(R.id.layout_initial)
        layoutResult = view.findViewById(R.id.layout_result)
        searchBar = view.findViewById(R.id.enter_word)
        searchButton = view.findViewById(R.id.search_button_background)
        resultWord = view.findViewById(R.id.result_word)
        resultDescription = view.findViewById(R.id.result_description)
        errorMessage = view.findViewById(R.id.error_message)

        searchButton.setOnClickListener {
            processSearchInput()
        }

        searchBar.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                processSearchInput()
                true
            } else {
                false
            }
        }

        return view
    }

    private fun processSearchInput() {
        val inputWord = searchBar.text.toString().trim()

        if (inputWord.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.enter_word_to_search), Toast.LENGTH_SHORT).show()
            return
        }

        if (inputWord.lowercase() == "jamid") {
            displayResult(inputWord, getString(R.string.example_result_description))
        } else {
            displayError()
        }
    }

    private fun displayResult(word: String, description: String) {
        layoutInitial.visibility = View.GONE
        layoutResult.visibility = View.VISIBLE

        resultWord.text = word
        resultDescription.text = description
        errorMessage.visibility = View.GONE
    }

    private fun displayError() {
        layoutInitial.visibility = View.GONE
        layoutResult.visibility = View.VISIBLE

        errorMessage.visibility = View.VISIBLE
        resultWord.text = ""
        resultDescription.text = ""
    }
}
