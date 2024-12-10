package com.capstone.arabicmorph.view.jamiddetector

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import androidx.activity.OnBackPressedCallback
import com.capstone.arabicmorph.gamification.DailyChallengeScheduler
import com.capstone.arabicmorph.gamification.util.NotificationUtils
import com.capstone.arabicmorph.view.history.historydatabase.HistoryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    private lateinit var saveButton: Button

    private var currentXP = 0
    private var lastSearchedWord: String = ""

    private lateinit var sharedPreferences: SharedPreferences
    private var wordsSearchedToday = 0

    private val viewModel: JamidDetectorViewModel by viewModels {
        JamidDetectorViewModelFactory(requireActivity().application)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jamid_detector, container, false)

        sharedPreferences = requireContext().getSharedPreferences("UserPreferences", android.content.Context.MODE_PRIVATE)
        wordsSearchedToday = sharedPreferences.getInt("wordsSearchedToday", 0)

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
        saveButton = view.findViewById(R.id.save_button)

        loadCurrentXP()
        updateXPCounter()
        scheduleDailyChallenge()

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

        saveButton.setOnClickListener {
            val inputTextValue = searchBar.text.toString().trim()
            val predictionValue = resultDescription.text.toString().trim()

            if (inputTextValue.isNotEmpty() && predictionValue.isNotEmpty()) {
                savePredictionToHistory(inputTextValue, predictionValue)
            } else {
                Toast.makeText(requireContext(), "No prediction available to save", Toast.LENGTH_SHORT).show()
            }
        }


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPressed()
                }
            }
        )

        return view
    }

    private fun saveCurrentXP() {
        val editor = sharedPreferences.edit()
        editor.putInt("CURRENT_XP", currentXP)
        editor.apply()
    }

    private fun loadCurrentXP() {
        currentXP = sharedPreferences.getInt("CURRENT_XP", 0)
    }

    private fun saveWordsSearchedCount() {
        val editor = sharedPreferences.edit()
        editor.putInt("wordsSearchedToday", wordsSearchedToday)
        editor.apply()
    }

    private fun incrementXPIfEligible() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastXPDate = sharedPreferences.getString("lastXPDate", "")

        if (wordsSearchedToday % 5 == 0 && lastXPDate != today) {
            currentXP += 10
            saveCurrentXP()
            saveLastXPDate(today)
            updateXPCounter()

            NotificationUtils().sendChallengeCompletedNotification(requireContext())
        }
    }

    private fun saveLastXPDate(date: String) {
        val editor = sharedPreferences.edit()
        editor.putString("lastXPDate", date)
        editor.apply()
    }

    private fun updateXPCounter() {
        xpCounter.text = getString(R.string.xp_counter_text, currentXP)
    }

    private fun scheduleDailyChallenge() {
        val scheduler = DailyChallengeScheduler()
        scheduler.scheduleDailyChallenge(requireContext())
    }

    private fun handleBackPressed() {
        if (layoutResult.visibility == View.VISIBLE) {
            layoutResult.visibility = View.GONE
            layoutInitial.visibility = View.VISIBLE
            searchBar.setText(lastSearchedWord)
            showLoading(false)
        } else {
            requireActivity().finish()
        }
    }

    private fun processSearchInput() {
        val inputWord = searchBar.text.toString().trim()

        if (inputWord.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.enter_word_to_search), Toast.LENGTH_SHORT).show()
            return
        }

        lastSearchedWord = inputWord
        showLoading(true)
        viewModel.predictJamid(inputWord)
    }

    private fun savePredictionToHistory(inputText: String, prediction: String) {
        val historyEntity = HistoryEntity(
            inputText = inputText,
            prediction = prediction,
            isSuccess = true
        )
        viewModel.savePrediction(historyEntity)
        Toast.makeText(requireContext(), "Prediction saved to history", Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        viewModel.result.observe(viewLifecycleOwner) { result ->
            Log.d("JamidDetectorFragment", "Result received: $result")

            if (result == null) return@observe

            result.fold(
                onSuccess = {
                    showLoading(false)
                    displayResult(it.text, it.prediction)

                    wordsSearchedToday++
                    saveWordsSearchedCount()
                    incrementXPIfEligible()
                },
                onFailure = {
                    showLoading(false)
                    displayError(it.message ?: getString(R.string.word_not_found_please_try_again))
                }
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressIndicator.visibility = View.VISIBLE
            progressIndicator.isIndeterminate = true
        } else {
            progressIndicator.visibility = View.GONE
        }
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
