package com.visualinnovate.almursheed.commonView.more.aboutUs

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentAboutUsBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutUsFragment : BaseFragment() {

    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AboutUsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).hideBottomNav()

        // call api to get about us page
        viewModel.getAboutUsPage()

        initToolbar()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.about_us))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun subscribeData() {
        viewModel.getAboutUsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // set Data
                    binding.title.text = Html.fromHtml(
                        it.data?.pages?.get(0)?.title?.localized,
                        Html.FROM_HTML_MODE_LEGACY
                    )
                    binding.content.text = Html.fromHtml(
                        it.data?.pages?.get(0)?.description?.localized,
                        Html.FROM_HTML_MODE_LEGACY
                    )
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideMainLoading()
                }

                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}