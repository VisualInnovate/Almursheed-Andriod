package com.visualinnovate.almursheed.home

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.Car
import com.visualinnovate.almursheed.auth.model.City
import com.visualinnovate.almursheed.auth.model.Country
import com.visualinnovate.almursheed.auth.model.Language
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.ActivityMainBinding
import com.visualinnovate.almursheed.utils.Constant.ROLE_DRIVER
import com.visualinnovate.almursheed.utils.Constant.ROLE_GUIDE
import com.visualinnovate.almursheed.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainViewsManager {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var userRole: String
    private lateinit var navGraph: NavGraph

    var snackbar: Snackbar? = null
    private lateinit var networkConnectionManager: ConnectivityManager
    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRole = SharedPreference.getUserRole()!!
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_home)
        navGraph = navController.navInflater.inflate(R.navigation.nav_home)
        initConnectivityManager()
        /*if (userRole == ROLE_GUIDE || userRole == ROLE_DRIVER) {
            setupDriverOrGuideViews()
        } else {
            setupTouristViews()
        }*/
        setupTouristViews()

        if (userRole == ROLE_GUIDE || userRole == ROLE_DRIVER) {
            binding.bottomNavBar.removeViewAt(1)
        }

        binding.bottomNavBar.setOnItemSelectedListener(object :
            ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                when (id) {
                    // Tourist
                    R.id.action_home_tourist -> navController.customNavigate(R.id.homeFragment)
                    R.id.action_hireFragment -> {
                        if (userRole == ROLE_GUIDE || userRole == ROLE_DRIVER) {
                        } else {
                            navController.customNavigate(R.id.hireFragment)
                        }
                    }

                    R.id.action_accommodationFragment -> navController.customNavigate(R.id.accommodationFragment)
                    R.id.action_flightReservation -> navController.customNavigate(R.id.flightReservationFragment)

                    // Guide and Driver
                    R.id.action_home_driver_guide -> navController.customNavigate(R.id.homeDriveAndGuideFragment)
                    R.id.action_priceFragment -> navController.customNavigate(R.id.priceFragment)
                    R.id.action_earningFragment -> navController.customNavigate(R.id.editProfileFragment)

                    R.id.action_more -> navController.customNavigate(R.id.moreFragment)
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun showLoading() {
        binding.MainProgressLoading.visible()
    }

    override fun hideLoading() {
        binding.MainProgressLoading.gone()
    }

    override fun changeSelectedBottomNavListener(id: Int) { // resource
        if (this::binding.isInitialized) {
            binding.bottomNavBar.setItemSelected(id)
        }
    }

    private fun initConnectivityManager() {
        networkConnectionManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkConnectionCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (snackbar != null) {
                    if (snackbar!!.isShown) {
                        snackbar!!.dismiss()
                    }
                }
            }

            override fun onLost(network: Network) {
                showSnackBar()
            }
        }
        networkConnectionManager.registerDefaultNetworkCallback(networkConnectionCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            networkConnectionManager.unregisterNetworkCallback(networkConnectionCallback)
        } catch (ex: IllegalArgumentException) {
            // ignore this error
        }
    }

    fun showSnackBar() {
        snackbar = Snackbar.make(
            findViewById(R.id.mainActivity_root),
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE,
        ).setActionTextColor(ContextCompat.getColor(this, R.color.accent))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setAction(getString(R.string.reconnect)) {
                AlertDialog.Builder(this)
                    .setMessage(R.string.no_internet)
                    .setPositiveButton(R.string.action_settings) { paramDialogInterface, _ ->
                        startActivity(
                            Intent(
                                Settings.ACTION_WIRELESS_SETTINGS,
                            ),
                        )
                        paramDialogInterface.dismiss()
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->
                        snackbar?.show()
                    }.setCancelable(false)
                    .show()
            }
        snackbar?.show()
    }

    private fun setupDriverOrGuideViews() {
        navGraph.setStartDestination(R.id.homeDriveAndGuideFragment)
        binding.bottomNavBar.setMenuResource(R.menu.driver_guide_menu_bottom_nav)
        binding.bottomNavBar.setItemSelected(R.id.action_home_driver_guide)
        navController.graph = navGraph
    }

    private fun setupTouristViews() {
        navGraph.setStartDestination(R.id.homeFragment) // replace to homeTouristFragment
        binding.bottomNavBar.setMenuResource(R.menu.tourist_menu_bottom_nav)
        binding.bottomNavBar.setItemSelected(R.id.action_home_tourist)
        navController.graph = navGraph
    }

    // init (readJsonFile)
    private fun setupDataForCountryAndNationality() {
        // Read and parse the JSON data from the res/raw directory
        val jsonFile = readJSONFromRawResource(resources, R.raw.countries)

        try {
            val jsonObject = JSONObject(jsonFile)
            val country: Country = Gson().fromJson(jsonObject.toString(), Country::class.java)
            country.countryList?.filter {
                it.lang == SharedPreference.getLanguage()
            }.also {
                it?.forEach { item ->
                    val countryName = item.country
                    val countryId = item.country_id
                    val nationalName = item.nationality
                    Utils.countries[countryName] = countryId
                    Utils.nationalities[nationalName] = countryId
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun setupDataForCarModelAndYear() {
        // Read and parse the JSON data from the res/raw directory
        val jsonFile = readJSONFromRawResource(resources, R.raw.car_model_year)

        try {
            val jsonObject = JSONObject(jsonFile)
            val car: Car = Gson().fromJson(jsonObject.toString(), Car::class.java)
            car.carList?.map { item ->
                val carId = item.id
                val year = item.year
                val makeAndModel = item.make
                val makeType = "${item.make} - ${item.model}"
                Utils.carYears[year] = carId
                Utils.carBrand[makeAndModel] = carId
                Utils.carType[makeType] = carId
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun setupDataForLanguage() {
        // Read and parse the JSON data from the res/raw directory
        val jsonFile = readJSONFromRawResource(resources, R.raw.languages)

        try {
            val jsonObject = JSONObject(jsonFile)
            val language: Language = Gson().fromJson(jsonObject.toString(), Language::class.java)
            language.languageList?.map {
                val langId = it.id
                val langName = it.lang
                Utils.languages[langName] = langId
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun setupDataForCity() {
        // Read and parse the JSON data from the res/raw directory
        val jsonFile = readJSONFromRawResource(resources, R.raw.cities)

        try {
            val jsonObject = JSONObject(jsonFile)
            val city: City = Gson().fromJson(jsonObject.toString(), City::class.java)
            city.states?.map {
                val stateId = it.stateId
                val stateName = it.state
                Utils.cities[stateName] = stateId
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun readJSONFromRawResource(resources: Resources, resId: Int): String {
        val inputStream = resources.openRawResource(resId)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charsets.UTF_8)
    }
}
