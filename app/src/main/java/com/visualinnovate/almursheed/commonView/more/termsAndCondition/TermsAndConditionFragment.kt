package com.visualinnovate.almursheed.commonView.more.termsAndCondition

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.FragmentTermsAndConditionBinding

class TermsAndConditionFragment : Fragment() {

    private var _binding: FragmentTermsAndConditionBinding? = null
    private val binding get() = _binding!!

    // private val vm: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTermsAndConditionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hideBottomNav()

        val docxUrl =
            "https://dev.back.mursheed.com/media/89/%D9%85%D9%84%D8%A7%D8%AD%D8%B8%D8%A7%D8%AA-(2)-(1).docx"

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)

        val googleDocsViewerUrl = "https://docs.google.com/gview?embedded=true&url="
        val finalUrl = googleDocsViewerUrl + docxUrl

        binding.webView.loadUrl(finalUrl)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}