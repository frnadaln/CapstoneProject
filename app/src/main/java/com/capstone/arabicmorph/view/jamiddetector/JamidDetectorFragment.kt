package com.capstone.arabicmorph.view.jamiddetector

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.arabicmorph.R
import com.google.android.material.progressindicator.LinearProgressIndicator

class JamidDetectorFragment : Fragment() {

    private lateinit var layoutInitial: RelativeLayout
    private lateinit var layoutResult: RelativeLayout
    private lateinit var searchBar: EditText
    private lateinit var searchButton: FrameLayout
    private lateinit var resultWord: TextView
    private lateinit var resultDescription: TextView
    private lateinit var errorMessage: TextView
    private lateinit var xpCounter: TextView
    private lateinit var backgroundImage: ImageView
    private lateinit var overlayImage: ImageView
    private lateinit var progressIndicator: LinearProgressIndicator

    private var currentXP = 0

    private val viewModel: JamidDetectorViewModel by viewModels {
        JamidDetectorViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jamid_detector, container, false)

        layoutInitial = view.findViewById(R.id.layout_initial)
        layoutResult = view.findViewById(R.id.layout_result)
        searchBar = view.findViewById(R.id.enter_word)
        searchButton = view.findViewById(R.id.search_button_background)
        resultWord = view.findViewById(R.id.result_word)
        resultDescription = view.findViewById(R.id.result_description)
        errorMessage = view.findViewById(R.id.error_message)
        xpCounter = view.findViewById(R.id.xp_counter)
        backgroundImage = view.findViewById(R.id.background_image)
        overlayImage = view.findViewById(R.id.overlay_image)
        progressIndicator = view.findViewById(R.id.progressIndicator)

        updateXPCounter()

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

        startImageAnimation()

        observeViewModel()

        return view
    }

    private fun processSearchInput() {
        val inputWord = searchBar.text.toString().trim()

        if (inputWord.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.enter_word_to_search), Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)
        viewModel.predictJamid(inputWord)
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) { result ->
            showLoading(false)
            result.fold(
                onSuccess = { displayResult(it.text, it.prediction) },
                onFailure = { displayError(it.message ?: getString(R.string.word_not_found_please_try_again)) }
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun displayResult(word: String, description: String) {
        layoutInitial.visibility = View.GONE
        layoutResult.visibility = View.VISIBLE

        resultWord.text = word
        resultDescription.text = description
        errorMessage.visibility = View.GONE
    }

    private fun displayError(message: String) {
        layoutInitial.visibility = View.GONE
        layoutResult.visibility = View.VISIBLE

        errorMessage.visibility = View.VISIBLE
        errorMessage.text = message
        resultWord.text = ""
        resultDescription.text = ""
    }

    private fun incrementXP() {
        currentXP += 10
        updateXPCounter()
    }

    private fun updateXPCounter() {
        xpCounter.text = getString(R.string.xp_counter_text, currentXP)
    }

    private fun startImageAnimation() {
        val backgroundAnim = ObjectAnimator.ofFloat(backgroundImage, "translationX", -30f, 30f)
        backgroundAnim.duration = 6000
        backgroundAnim.repeatCount = ObjectAnimator.INFINITE
        backgroundAnim.repeatMode = ObjectAnimator.REVERSE

        val overlayAnim = ObjectAnimator.ofFloat(overlayImage, "translationX", -30f, 30f)
        overlayAnim.duration = 6000
        overlayAnim.repeatCount = ObjectAnimator.INFINITE
        overlayAnim.repeatMode = ObjectAnimator.REVERSE

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(backgroundAnim, overlayAnim)
        animatorSet.start()
    }
}
