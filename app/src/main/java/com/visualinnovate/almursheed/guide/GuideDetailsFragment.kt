package com.visualinnovate.almursheed.guide

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.price.adapters.MyPricesAdapter
import com.visualinnovate.almursheed.databinding.FragmentGuideDetailsBinding
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class GuideDetailsFragment : BaseFragment() {

    private var _binding: FragmentGuideDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private var guideId: Int? = null
    private var guide: DriverAndGuideItem? = null

    private lateinit var pricesAdapter: MyPricesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuideDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hideBottomNav()

        guideId = requireArguments().getInt(Constant.GUIDE_ID)

        // call api to get guide details by id
        vm.getGuideDetailsById(guideId)

        initToolbar()
        initPricesRecyclerView()
        subscribeData()
        setBtnListeners()
    }


    private fun setBtnListeners() {
        binding.btnHire.onDebouncedListener {
            guide?.let {
                val bundle = Bundle()
                bundle.putParcelable("selectedDriverOrGuide", guide)
                bundle.putString("type", Constant.ROLE_GUIDES)
                findNavController().customNavigate(R.id.hireFragment, data = bundle)
            }
        }
    }

    private fun initToolbar() {
        binding.appBarGuidesDetails.setTitleString(getString(R.string.guide_details))
        binding.appBarGuidesDetails.setTitleCenter(true)
        binding.appBarGuidesDetails.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back
        )
    }

    private fun subscribeData() {
        // observe in drivers list
        vm.guideDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    guide = it.data!!.guide
                    initViews(it.data.guide)
                    pricesAdapter.submitData(it.data.guide!!.priceServices)
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

    @SuppressLint("SetTextI18n")
    private fun initViews(guide: DriverAndGuideItem?) {
        Glide.with(requireContext())
            .load(guide?.personalPhoto)
            .error(R.drawable.ic_person)
            .into(binding.imgGuide)

        binding.guideName.text = guide?.name ?: ""
        binding.guideCity.text = guide?.country ?: ""
        binding.guideCountry.text = guide?.state ?: ""
        binding.guideDescription.text = guide?.bio ?: ""

        guide?.languages.let {
            if (it?.isNotEmpty() == true) {
                binding.languageEnglish.text = guide?.getLanguage()?.joinToString(" , ")
            } else {
                binding.languageEnglish.text = getString(R.string.no_languages)
            }
        }

        guide?.priceServices?.let {
            if (it.isNotEmpty()) {
                binding.guidePrice.text = "$ ${it[0]?.price.toString()}"
            } else {
                binding.guidePrice.text = "$ 0.0"
            }
        }

        // binding.guidePrice.text = "$ ${guideArgument.guidePrice}"

        val rateImage = when (guide?.totalRating?.toDouble()?.roundToInt() ?: 0) {
            1 -> R.drawable.ic_star_1
            2 -> R.drawable.ic_stars_2
            3 -> R.drawable.ic_stars_3
            4 -> R.drawable.ic_stars_4
            5 -> R.drawable.ic_stars_5
            0 -> R.drawable.ic_group_rate
            else -> R.drawable.ic_group_rate
        }

        binding.guideReview.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(requireContext(), rateImage), null, null, null
        )
        // binding.guideReview.text = "(${guide?.count_rate} ${getString(R.string.review)})"
    }

    private fun initPricesRecyclerView() {
        pricesAdapter = MyPricesAdapter()
        binding.pricesRV.apply {
            itemAnimator = DefaultItemAnimator()
            pricesAdapter.setHasStableIds(true)
            adapter = pricesAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
