package com.visualinnovate.almursheed.commonView.price.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.CityItem
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.commonView.price.adapters.MyPricesAdapter
import com.visualinnovate.almursheed.commonView.price.viewModels.PriceViewModel
import com.visualinnovate.almursheed.databinding.FragmentPriceBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PriceFragment : BaseFragment() {
    private var _binding: FragmentPriceBinding? = null
    private val binding get() = _binding!!
    private val vm: PriceViewModel by viewModels()

    private lateinit var myPricesAdapter: MyPricesAdapter

    private var citiesList = ArrayList<ChooserItemModel>()
    private var cityName: String? = null
    private var cityId: String? = null

    private lateinit var price: String

    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_priceFragment)
        initToolbar()
        initViews()
        setBtnListener()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.my_prices))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun initViews() {
        binding.city.text = cityName ?: getString(R.string.choose_city)
        // initCitiesSpinner()
        val country = SharedPreference.getCountryId()
        citiesList = setupCitiesList(Utils.filteredCities)
        initMyPricesRecyclerView()
    }

    private fun setupCitiesList(cities: ArrayList<CityItem>): ArrayList<ChooserItemModel> {
        Utils.filterCitiesByCountryId()
        val chooserItemList = ArrayList<ChooserItemModel>()
        cities.forEach {
            val item = ChooserItemModel(
                name = it.state,
                id = it.stateId,
                isSelected = vm.cityName == it.state
            )
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun showCityChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.cityy), citiesList, { data, _ -> // position
                cityId = data.id
                cityName = data.name
                binding.city.text = cityName
            })
        showBottomSheet(chooseTextBottomSheet!!, "CityBottomSheet")
    }

    private fun initMyPricesRecyclerView() {
        myPricesAdapter = MyPricesAdapter()
        binding.pricesRV.apply {
            itemAnimator = DefaultItemAnimator()
            myPricesAdapter.setHasStableIds(true)
            adapter = myPricesAdapter
        }
    }

    private fun setBtnListener() {
        binding.city.onDebouncedListener {
            showCityChooser()
        }

        binding.btnUpdate.onDebouncedListener {
            if (validate()) {
                vm.addNewPrice(cityId!!, price)
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        price = binding.price.text.toString().trim()

        if (price.isNotEmpty()) {
            binding.price.error = null
        } else {
            binding.price.error = getString(R.string.required)
            isValid = false
        }
        if (cityId == null) {
            binding.city.error = getString(R.string.required)
            isValid = false
        } else {
            binding.city.error = null
        }

        /*for ((index, value) in myPricesAdapter.getPricesList().withIndex()) {
            if (value?.cityId.toString() == cityId) {
                toast("you are already selected this city")
                isValid = false
                break
            }
        }*/
        return isValid
    }

    private fun subscribeData() {
        vm.addNewPrice.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    vm.getAllPrices()
                }

                is ResponseHandler.Error -> {
                    toast(it.message)
                }

                is ResponseHandler.Loading -> {
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    hideMainLoading()
                }

                else -> {
                    toast("Something went wrong")
                }
            }
        }

        vm.myPrices.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    val data = it.data?.priceServices
                    myPricesAdapter.submitData(data)
                }

                is ResponseHandler.Error -> {
                    toast(it.message)
                }

                is ResponseHandler.Loading -> {
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    hideMainLoading()
                }

                else -> {
                    toast("Something went wrong")
                }
            }
        }
    }

    /*private fun initCitiesSpinner() {
        val cityList = allCitiesString

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                cityList,
            )

        binding.spinnerCities.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerCities.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    cityId = allCities[position].stateId
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }*/

    override fun onStart() {
        super.onStart()
        vm.getAllPrices()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
