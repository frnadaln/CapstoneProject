package com.capstone.arabicmorph

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment

class SettingFragment : Fragment(R.layout.fragment_setting) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.btn_back_setting)
        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_content, VerbConjugatorFragment())
                .commit()
        }
    }
}
