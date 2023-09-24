package com.visualinnovate.almursheed.home

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
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
    private lateinit var navGraph : NavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRole = SharedPreference.getUserRole()!!
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_home)
         navGraph =  navController.navInflater.inflate(R.navigation.nav_home)
        if (userRole == ROLE_GUIDE || userRole == ROLE_DRIVER) {
            setupDriverOrGuideViews()
        } else {
            setupTouristViews()
        }
        binding.bottomNavBar.setOnItemSelectedListener(object :
            ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                when (id) {
                    // tourist
                    R.id.action_home_tourist -> navController.customNavigate(R.id.homeFragment)
                    R.id.action_hireFragment -> navController.customNavigate(R.id.hireFragment)
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
