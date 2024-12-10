package com.capstone.arabicmorph.view.history

import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.arabicmorph.adapter.HistoryAdapter
import com.capstone.arabicmorph.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels { HistoryViewModelFactory(requireActivity().application) }
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        startImageAnimation()

        binding.historyProgressBar.visibility = View.VISIBLE

        viewModel.predictHistory.observe(viewLifecycleOwner) { historyList ->
            binding.historyProgressBar.visibility = View.GONE

            if (historyList != null && historyList.isNotEmpty()) {
                historyAdapter.submitList(historyList)
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.GONE
                showEmptyState()
            }
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter { history ->
            viewModel.deletePrediction(history)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun showEmptyState() {
        binding.imageStack.visibility = View.VISIBLE
        binding.emptyStateTextView.visibility = View.VISIBLE
        binding.revealText.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun startImageAnimation() {
        val backgroundAnim = ObjectAnimator.ofFloat(binding.backgroundImage, "translationX", -30f, 30f)
        backgroundAnim.duration = 6000
        backgroundAnim.repeatCount = ObjectAnimator.INFINITE
        backgroundAnim.repeatMode = ObjectAnimator.REVERSE

        val animatorSet = AnimatorSet().apply {
            playTogether(backgroundAnim)
        }
        animatorSet.start()

        val title = ObjectAnimator.ofFloat(binding.emptyStateTextView, View.ALPHA, 1f).setDuration(100)
        val revealText = ObjectAnimator.ofFloat(binding.revealText, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                revealText
            )
            startDelay = 100
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
