package com.visualinnovate.almursheed.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.startHomeActivity
import com.visualinnovate.almursheed.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay

class SplashFragment : BaseFragment() {

    private lateinit var binding: FragmentSplashBinding

    // Replace with your audio file path
    private val audioFilePath = "path/to/your/audio/file.mp3"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // load icon next of get started
        loadGifImage(requireContext(), R.drawable.animation_logo_splash, binding.imgLogoSplash)
        lifecycleScope.launchWhenResumed {
            delay(1000)
            if (SharedPreference.getUserLoggedIn()) {
                requireActivity().startHomeActivity()
            } else {
                findNavController().customNavigate(R.id.onBoardingFragment)
            }
        }
        //  binding.waveformView.setAudioData(loadAudioDataFromRaw(R.raw.bell))
    }

    fun loadAudioDataFromRaw(resourceId: Int): ShortArray {
        val inputStream = resources.openRawResource(resourceId)
        val fileLength = inputStream.available()
        val audioData = ByteArray(fileLength)
        inputStream.read(audioData)
        inputStream.close()

        val shortAudioData = ShortArray(fileLength / 2)
        for (i in 0 until fileLength / 2) {
            val sampleValue = (audioData[2 * i].toInt() shl 8) or audioData[2 * i + 1].toInt()
            shortAudioData[i] = sampleValue.toShort()
        }

        return shortAudioData
    }
}
