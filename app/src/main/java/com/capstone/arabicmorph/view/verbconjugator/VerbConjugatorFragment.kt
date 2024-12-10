package com.capstone.arabicmorph.view.verbconjugator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.arabicmorph.R
import com.capstone.arabicmorph.data.ConjugatorRepository
import com.capstone.arabicmorph.data.SuggestItem
import com.capstone.arabicmorph.databinding.FragmentVerbConjugatorBinding
import com.capstone.arabicmorph.view.appinfo.AppInfoFragment

class VerbConjugatorFragment : Fragment() {

    private lateinit var binding: FragmentVerbConjugatorBinding
    private lateinit var conjugatorViewModel: ConjugatorViewModel
    private lateinit var conjugationAdapter: ConjugationAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutInitial: RelativeLayout
    private lateinit var layoutResult: RelativeLayout
    private lateinit var backgroundImage: ImageView
    private lateinit var overlayImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerbConjugatorBinding.inflate(inflater, container, false)
        val root = binding.root

        val repository = ConjugatorRepository()
        val viewModelFactory = ConjugatorViewModelFactory(repository)
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

        startImageAnimation()


        // Handle search button click
        binding.searchButtonBackground.setOnClickListener {
            val searchText = binding.enterWord.text.toString().trim()
            if (searchText.isNotEmpty()) {
                showLoading(true) // Show loading indicator
                conjugatorViewModel.getConjugationResults(searchText)
            } else {
                Toast.makeText(context, "Please enter a word to search", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle API response
        conjugatorViewModel.conjugatorResponse.observe(viewLifecycleOwner) { response ->
            showLoading(false) // Hide loading indicator after response

            response?.let {
                displayResult(it.suggest) // Display conjugation results
            } ?: run {
                displayError("No results found") // Display error message
            }
        }

        binding.helpSection.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, AppInfoFragment())
                .addToBackStack(null)
                .commit()
        }

        return root
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun displayResult(results: List<SuggestItem>) {
        layoutInitial.visibility = View.GONE
        layoutResult.visibility = View.VISIBLE

        if (results.isNotEmpty()) {
            conjugationAdapter.submitList(results)
        } else {
            Toast.makeText(context, "Tidak ada hasil ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayError(message: String) {
        layoutInitial.visibility = View.GONE
        layoutResult.visibility = View.VISIBLE

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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