package com.visualinnovate.almursheed.home.view.guide

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.Constant
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentAllGuidesBinding
import com.visualinnovate.almursheed.home.adapter.AllGuideAdapter
import com.visualinnovate.almursheed.home.model.GuideModel

class AllGuidesFragment : Fragment() {

    private var _binding: FragmentAllGuidesBinding? = null
    private val binding get() = _binding!!

    private lateinit var allGuideAdapter: AllGuideAdapter

    private val btnGuideClickCallBack: (guide: GuideModel) -> Unit =
        { guide ->
            val bundle = Bundle()
            bundle.putParcelable(Constant.GUIDE, guide)
            findNavController().customNavigate(R.id.guideDetailsFragment, false, bundle)
        }

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnSortCallBackFunc: () -> Unit = {
        toast("Clicked btnSortCallBackFunc")
    }

    private val btnFilterCallBackFunc: () -> Unit = {
        toast("Clicked btnFilterCallBackFunc")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllGuidesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
    }

    private fun initToolbar() {
        binding.appBarSeeAllGuides.setTitleString(getString(R.string.guides))
        binding.appBarSeeAllGuides.setTitleCenter(true)
        binding.appBarSeeAllGuides.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
        binding.appBarSeeAllGuides.showButtonSortAndFilter(
            getString(R.string.sort),
            getString(R.string.filter),
            R.drawable.ic_sort,
            R.drawable.ic_filter,
            btnSortCallBackFunc,
            btnFilterCallBackFunc
        )
    }

    private fun initViews() {
        initSeeAllDriverRecycler()
    }

    private fun initSeeAllDriverRecycler() {
        binding.rvSeeAllGuides.apply {
            allGuideAdapter = AllGuideAdapter(btnGuideClickCallBack)
            allGuideAdapter.submitData(getAllGuideList())
            adapter = allGuideAdapter
        }
    }

    private fun getAllGuideList(): ArrayList<GuideModel> {
        val guideList = ArrayList<GuideModel>()

        guideList.add(
            GuideModel(
                0,
                R.drawable.img_driver,
                2.2,
                "Ahmed Mohamed",
                120.0,
                "October",
                false,
                "English, Arabic, French"
            )
        )
        guideList.add(
            GuideModel(
                1,
                R.drawable.img_driver,
                1.1,
                "Mohamed Mohamed",
                120.0,
                "Giza",
                true,
                "English"
            )
        )
        guideList.add(
            GuideModel(
                2,
                R.drawable.img_driver,
                4.5,
                "Ahmed Ahmed",
                120.0,
                "Cairo",
                false,
                "Arabic, French"
            )
        )
        guideList.add(
            GuideModel(
                3,
                R.drawable.img_driver,
                3.1,
                "Ahmed Mohamed",
                120.0,
                "Giza",
                true,
                "English, Arabic"
            )
        )
        return guideList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
