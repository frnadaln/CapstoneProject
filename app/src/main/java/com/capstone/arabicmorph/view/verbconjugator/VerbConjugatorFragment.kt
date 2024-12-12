package com.capstone.arabicmorph.view.verbconjugator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.arabicmorph.R
import com.capstone.arabicmorph.databinding.FragmentVerbConjugatorBinding
import com.capstone.arabicmorph.gamification.DailyChallengeScheduler
import com.capstone.arabicmorph.gamification.util.NotificationUtils
import com.capstone.arabicmorph.view.appinfo.AppInfoFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VerbConjugatorFragment : Fragment() {

    private lateinit var binding: FragmentVerbConjugatorBinding
    private lateinit var conjugatorViewModel: ConjugatorViewModel
    private lateinit var conjugationAdapter: ConjugationAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutInitial: RelativeLayout
    private lateinit var layoutResult: RelativeLayout
    private lateinit var backgroundImage: ImageView
    private lateinit var overlayImage: ImageView
    private lateinit var recyclerView: RecyclerView

    private var currentXP = 0
    private var lastSearchedWord: String = ""

    private lateinit var sharedPreferences: SharedPreferences
    private var wordsSearchedToday = 0

    private val uniqueWordsToday: MutableSet<String> = mutableSetOf()

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerbConjugatorBinding.inflate(inflater, container, false)
        val root = binding.root

        val repository = ConjugatorRepository()
        val viewModelFactory = ConjugatorViewModelFactory(repository)

        recyclerView = binding.resultsRecyclerView
        progressBar = binding.progressIndicator

        conjugationAdapter = ConjugationAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = conjugationAdapter

        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        conjugatorViewModel = ViewModelProvider(this, viewModelFactory)[ConjugatorViewModel::class.java]

        conjugationAdapter = ConjugationAdapter()
        binding.resultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = conjugationAdapter
        }

        progressBar = binding.progressIndicator
        layoutInitial = binding.layoutInitial
        layoutResult = binding.layoutResult
        backgroundImage = binding.backgroundImage
        overlayImage = binding.overlayImage

        sharedPreferences =
            requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        wordsSearchedToday = sharedPreferences.getInt("wordsSearchedToday", 0)

        loadUniqueWordsToday()
        loadCurrentXP()
        updateXPCounter()
        scheduleDailyChallenge()

        startImageAnimation()

        showInitialLayout()

        layoutResult.visibility = View.GONE
        progressBar.visibility = View.GONE

        binding.searchButtonBackground.setOnClickListener {
            val searchText = binding.enterWord.text.toString().trim()

            if (!isInternetAvailable(requireContext())) {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val arabicRegex = Regex("^[\\u0600-\\u06FF\\u0750-\\u077F]+$")
            if (!arabicRegex.matches(searchText)) {
                Toast.makeText(requireContext(), "Please enter a Fusha Arabic word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (searchText.isNotEmpty()) {
                showLoading()
                conjugatorViewModel.getConjugationResults(searchText)
            } else {
                Toast.makeText(context, "Enter the word you want to search for", Toast.LENGTH_SHORT).show()
            }
            lastSearchedWord = searchText
        }

        conjugatorViewModel.conjugationResult.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                Log.d("VerbConjugator", "API response: $response")
                conjugationAdapter.setData(
                    response.verbInfo ?: "",
                    response.suggest ?: emptyList(),
                    response.result ?: emptyMap()
                )
                conjugationAdapter.notifyDataSetChanged()
                layoutInitial.visibility = View.GONE
                layoutResult.visibility = View.VISIBLE
                hideLoading()
                displayResult()

                if (uniqueWordsToday.add(lastSearchedWord)) {
                    saveUniqueWordsToday()
                    incrementXPIfEligible()
                }
            } else {
                Log.e("VerbConjugator", "No data received or error")
                layoutInitial.visibility = View.GONE
                layoutResult.visibility = View.VISIBLE
                displayError()
                hideLoading()
            }
        })

        binding.helpSection.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, AppInfoFragment())
                .addToBackStack(null)
                .commit()
        }

        return root
    }

    private fun displayResult() {
        binding.apply {
            layoutInitial.visibility = View.GONE
            layoutResult.visibility = View.VISIBLE

            resultsRecyclerView.visibility = View.VISIBLE
            resultErrorText.visibility = View.GONE
        }
    }

    private fun displayError() {
        binding.apply {
            layoutInitial.visibility = View.GONE
            layoutResult.visibility = View.VISIBLE

            resultsRecyclerView.visibility = View.GONE
            resultErrorText.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        binding.progressIndicator.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressIndicator.visibility = View.GONE
    }

    private fun showInitialLayout() {
        layoutInitial.visibility = View.VISIBLE
        layoutResult.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
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

