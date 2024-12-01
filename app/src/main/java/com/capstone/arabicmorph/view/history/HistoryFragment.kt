package com.capstone.arabicmorph.view.history

import HistoryAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.arabicmorph.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyList: List<String> = emptyList()

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
        checkHistory()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = HistoryAdapter(historyList)
        }
    }

    private fun checkHistory() {
        if (historyList.isEmpty()) {
            binding.imageStack.visibility = View.VISIBLE
            binding.emptyStateTextView.visibility = View.VISIBLE
            binding.revealText.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.imageStack.visibility = View.GONE
            binding.emptyStateTextView.visibility = View.GONE
            binding.revealText.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
