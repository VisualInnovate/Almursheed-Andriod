package com.visualinnovate.almursheed.auth.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.viewmodel.AuthViewModel
import com.visualinnovate.almursheed.common.*
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.databinding.FragmentLoginBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    private var loginAs = "" // 1-> driver 2- guide 3- tourist
    private lateinit var email: String
    private lateinit var password: String
    private var countryId: Int? = null
    private var cityId: Int? = null

    private var rememberMeType: Boolean = false

    private val vm: AuthViewModel by viewModels()

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnRegisterCallBackListener: () -> Unit = {
        // navigate to register
        findNavController().customNavigate(R.id.registerTypeFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun initView() {
        // binding.spinnerLoginAs.spinnerText.setText(getString(R.string.select_your_option))
        // binding.spinnerChooseDestination.spinnerText.text = getString(R.string.select_your_destination)
        // binding.spinnerChooseCity.spinnerText.text = getString(R.string.select_your_city)
        initSpinner()
        initChooseDestinationSpinner()
        initCitySpinner()
    }

    private fun initToolbar() {
        binding.appBarLogin.setTitleString(getString(R.string.login))
        binding.appBarLogin.setTitleCenter(true)
        binding.appBarLogin.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
        binding.appBarLogin.showButtonOneWithoutImage(
            getString(R.string.register),
            btnRegisterCallBackListener
        )
    }

    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.options,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        binding.spinnerLoginAs.spinner.adapter = arrayAdapter

        binding.spinnerLoginAs.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    loginAs = parent?.getItemAtPosition(position).toString()
                    Log.d("onItemSelected", "options $loginAs")
                    // controlView()
                    loginAs = when (loginAs) {
                        "Driver" -> {
                            "1"
                        }
                        "Guide" -> {
                            "2"
                        }
                        else -> {
                            showExtraViewToTourist()
                            "3"
                        }
                    }
                    Log.d("onItemSelected", "options $loginAs")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }

        /* binding.spinnerLoginAs.spinner.setOnItemClickListener { parent, _, position, _ -> // parent, view, position, long
            loginAs = parent?.getItemAtPosition(position).toString()
            Log.d("onItemSelected", "options $loginAs")
            // controlView()
            loginAs = when (loginAs) {
                "Driver" -> {
                    "1"
                }
                "Guide" -> {
                    "2"
                }
                else -> {
                    "3"
                }
            }
            Log.d("onItemSelected", "options $loginAs")
        }*/
    }

    private fun showExtraViewToTourist() {
        Log.d("controlViews", "options $loginAs")
        if (loginAs == "Tourist" || loginAs == "3") {
            binding.txtChooseDestination.visible()
            binding.spinnerChooseDestination.root.visible()
            binding.txtChooseCity.visible()
            binding.spinnerChooseCity.root.visible()
        } else {
            binding.txtChooseDestination.gone()
            binding.spinnerChooseDestination.root.gone()
            binding.txtChooseCity.gone()
            binding.spinnerChooseCity.root.gone()
        }
    }

    private fun initChooseDestinationSpinner() {
        val countryList = Utils.countries.keys.toList().sorted()

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                countryList
            )

        binding.spinnerChooseDestination.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerChooseDestination.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Retrieve the selected country name
                    val selectedCountryName = countryList[position]
                    // Retrieve the corresponding country ID from the map
                    countryId = Utils.countries[selectedCountryName]!!.toInt()
                    Log.d(
                        "spinnerChooseDestination",
                        "selectedCountryName $selectedCountryName countryId $countryId"
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
    }

    private fun initCitySpinner() {
        val cityList = Utils.cities.keys.toList().sorted()

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                cityList
            )

        binding.spinnerChooseCity.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerChooseCity.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Retrieve the selected country name
                    val selectedCityName = cityList[position]
                    Log.d("spinnerChooseCity", "selectedCityName $selectedCityName")

                    // Retrieve the corresponding country ID from the map
                    cityId = Utils.cities[selectedCityName]!!
                    val cityId22 = Utils.cities[selectedCityName]!! //
                    Log.d("spinnerChooseCity", "cityId $cityId")
                    Log.d("spinnerChooseCity", "cityId22 $cityId22")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
    }

    private fun setBtnListener() {
        binding.btnRememberMe.setOnClickListener {
            if (!rememberMeType) {
                rememberMeType = true
                binding.imgRememberMe.setImageResource(R.drawable.ic_remember_me_selected)
            } else {
                rememberMeType = false
                binding.imgRememberMe.setImageResource(R.drawable.ic_remember_me_unselected)
            }
        }
        binding.txtForgetPassword.setOnClickListener {
            findNavController().customNavigate(R.id.forgetPasswordFragment)
        }

        binding.btnLogin.setOnClickListener {
            if (validate()) {
                // call api login
                SharedPreference.saveCityIfTourist(cityId)
                vm.login(email, password, loginAs.toInt())
            }
        }
    }

    private fun subscribeData() {
        vm.loginLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // navigate to home
                    if (SharedPreference.getCityIfTourist() == null) {
                        SharedPreference.saveCityIfTourist(it.data?.user!!.stateId)
                    }

                    SharedPreference.saveUserToken(it.data!!.token!!)
                    Log.d("Success", "${SharedPreference.getCityIfTourist()}")
                    requireActivity().startHomeActivity()
                    // save user
                }
                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("ResponseHandler.Error", it.message)
                }
                is ResponseHandler.Loading -> {
                    // show a progress bar
                }
                else -> {
                    toast("Else")
                }
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        email = binding.edtEmailAddress.value
        password = binding.edtPassword.value

        if (email.isEmptySting()) {
            binding.edtEmailAddress.error = getString(R.string.required)
            isValid = false
        } else if (!validEmail(email)) {
            binding.edtEmailAddress.error = getString(R.string.please_enter_a_valid_email)
            isValid = true
        } else {
            binding.edtEmailAddress.error = null
        }
        if (password.isEmpty()) {
            binding.edtPassword.error = getString(R.string.required)
            isValid = false
        } else if (password.length < 8) {
            binding.edtPassword.error = "password should be more than 8 characters"
            isValid = false
        } else {
            binding.edtPassword.error = null
        }
        if (loginAs.isEmptySting()) {
            toast("Please choose login as")
            isValid = false
        }
        return isValid
    }
}
