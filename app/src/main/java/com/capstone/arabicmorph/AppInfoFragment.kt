package com.capstone.arabicmorph

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment

class AppInfoFragment : Fragment(R.layout.fragment_app_info) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.btn_back_app_info)
        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_content, VerbConjugatorFragment())
                .commit()
        }
    }
}
