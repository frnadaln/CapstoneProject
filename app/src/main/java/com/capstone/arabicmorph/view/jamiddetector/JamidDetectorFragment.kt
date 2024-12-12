package com.capstone.arabicmorph.view.jamiddetector

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.arabicmorph.R
import com.capstone.arabicmorph.databinding.FragmentJamidDetectorBinding
import com.capstone.arabicmorph.gamification.DailyChallengeScheduler
import com.capstone.arabicmorph.gamification.util.NotificationUtils
import com.capstone.arabicmorph.view.history.historydatabase.HistoryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

class JamidDetectorFragment : Fragment() {

    private var _binding: FragmentJamidDetectorBinding? = null
    private val binding get() = _binding!!

    private var currentXP = 0
    private var lastSearchedWord: String = ""

    private lateinit var sharedPreferences: SharedPreferences
    private var wordsSearchedToday = 0

    private val uniqueWordsToday: MutableSet<String> = mutableSetOf()

    private val viewModel: JamidDetectorViewModel by viewModels {
        JamidDetectorViewModelFactory(requireActivity().application)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJamidDetectorBinding.inflate(inflater, container, false)

        sharedPreferences =
            requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        wordsSearchedToday = sharedPreferences.getInt("wordsSearchedToday", 0)

        loadUniqueWordsToday()
        loadCurrentXP()
        updateXPCounter()
        scheduleDailyChallenge()

        setupListeners()
        startImageAnimation()
        observeViewModel()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPressed()
                }
            }
        )

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupListeners() {
        binding.apply {
            searchButtonBackground.setOnClickListener {
                processSearchInput()
            }

            enterWord.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    processSearchInput()
                    true
                } else {
                    false
                }
            }

            saveButton.setOnClickListener {
                val inputTextValue = enterWord.text.toString().trim()
                val predictionValue = resultDescription.text.toString().trim()

                if (inputTextValue.isNotEmpty() && predictionValue.isNotEmpty()) {
                    savePredictionToHistory(inputTextValue, predictionValue)
                } else {
                    Toast.makeText(requireContext(), "No prediction available to save", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadUniqueWordsToday() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val savedDate = sharedPreferences.getString("uniqueWordsDate", "")
        if (savedDate != today) {
            uniqueWordsToday.clear()
        } else {
            sharedPreferences.getStringSet("uniqueWordsToday", emptySet())?.let {
                uniqueWordsToday.addAll(it)
            }
        }
    }

    private fun saveUniqueWordsToday() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        sharedPreferences.edit()
            .putStringSet("uniqueWordsToday", uniqueWordsToday)
            .putString("uniqueWordsDate", today)
            .apply()
    }

    private fun saveCurrentXP() {
        val editor = sharedPreferences.edit()
        editor.putInt("CURRENT_XP", currentXP)
        editor.apply()
    }

    private fun loadCurrentXP() {
        currentXP = sharedPreferences.getInt("CURRENT_XP", 0)
    }

    private fun incrementXPIfEligible() {
        if (uniqueWordsToday.size % 5 == 0) {
            currentXP += 10
            saveCurrentXP()
            updateXPCounter()
            NotificationUtils().sendChallengeCompletedNotification(requireContext())
        }
    }

    private fun updateXPCounter() {
        binding.xpCounter.text = getString(R.string.xp_counter_text, currentXP)
    }

    private fun scheduleDailyChallenge() {
        val scheduler = DailyChallengeScheduler()
        scheduler.scheduleDailyChallenge(requireContext())
    }

    private fun handleBackPressed() {
        binding.apply {
            if (layoutResult.visibility == View.VISIBLE) {
                layoutResult.visibility = View.GONE
                layoutInitial.visibility = View.VISIBLE
                enterWord.setText(lastSearchedWord)
                showLoading(false)
            } else {
                requireActivity().finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun processSearchInput() {
        if (!isInternetAvailable(requireContext())) {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
            return
        }

        val inputWord = binding.enterWord.text.toString().trim()

        if (inputWord.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.enter_word_to_search), Toast.LENGTH_SHORT).show()
            return
        }

        val arabicRegex = Regex("^[\\u0600-\\u06FF\\u0750-\\u077F]+$")
        if (!arabicRegex.matches(inputWord)) {
            Toast.makeText(requireContext(), "Please enter a Fusha Arabic word", Toast.LENGTH_SHORT).show()
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
            if (result == null) return@observe

            result.fold(
                onSuccess = {
                    showLoading(false)
                    displayResult(it.text, it.prediction)

                    if (uniqueWordsToday.add(it.text)) {
                        saveUniqueWordsToday()
                        incrementXPIfEligible()
                    }
                },
                onFailure = {
                    showLoading(false)
                    displayError(it.message ?: getString(R.string.word_not_found_please_try_again))
                }
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun displayResult(word: String, description: String) {
        binding.apply {
            layoutInitial.visibility = View.GONE
            layoutResult.visibility = View.VISIBLE

            resultWord.text = word
            resultDescription.text = description
            errorMessage.visibility = View.GONE
        }
    }

    private fun displayError(message: String) {
        binding.apply {
            layoutInitial.visibility = View.GONE
            layoutResult.visibility = View.VISIBLE

            errorMessage.visibility = View.VISIBLE
            errorMessage.text = message
            resultWord.text = ""
            resultDescription.text = ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun startImageAnimation() {
        binding.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
