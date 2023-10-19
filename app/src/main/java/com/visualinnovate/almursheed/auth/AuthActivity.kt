package com.visualinnovate.almursheed.auth

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
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.Car
import com.visualinnovate.almursheed.auth.model.City
import com.visualinnovate.almursheed.auth.model.CityItem
import com.visualinnovate.almursheed.auth.model.Country
import com.visualinnovate.almursheed.auth.model.Language
import com.visualinnovate.almursheed.auth.model.LanguageItem
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.ActivityAuthBinding
import com.visualinnovate.almursheed.utils.Utils.allCarModels
import com.visualinnovate.almursheed.utils.Utils.allCities
import com.visualinnovate.almursheed.utils.Utils.allCountries
import com.visualinnovate.almursheed.utils.Utils.allLanguages
import com.visualinnovate.almursheed.utils.Utils.allNationalities
import com.visualinnovate.almursheed.utils.Utils.carBrand
import com.visualinnovate.almursheed.utils.Utils.carType
import com.visualinnovate.almursheed.utils.Utils.carYears
import com.visualinnovate.almursheed.utils.Utils.citiesModel
import com.visualinnovate.almursheed.utils.Utils.filterCitiesByCountryId
import com.visualinnovate.almursheed.utils.Utils.languages
import com.visualinnovate.almursheed.utils.Utils.selectedCountryId
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(), AuthViewsManager {

    private lateinit var binding: ActivityAuthBinding

    var snackbar: Snackbar? = null
    private lateinit var networkConnectionManager: ConnectivityManager
    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initConnectivityManager()
        setupDataForCountryAndNationality()
        setupDataForCarModelAndYear()
        setupDataForLanguage()
        setupDataForCity()
    }

    // init (readJsonFile)
    private fun setupDataForCountryAndNationality() {
        // Read and parse the JSON data from the res/raw directory
        val jsonFile = readJSONFromRawResource(resources, R.raw.countries)

        try {
            allCountries.clear()
            val jsonObject = JSONObject(jsonFile)
            val country: Country = Gson().fromJson(jsonObject.toString(), Country::class.java)
            country.countryList?.filter {
                it.lang == SharedPreference.getLanguage()
            }.also {
                it?.forEach { item ->
                    allCountries.add(item)
                    allNationalities.add(item.nationality)
                }
                selectedCountryId = allCountries[0].country_id
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
                val model = item.model
                val makeType = "${item.make} - ${item.model}"
                carYears[year] = carId
                carBrand[makeAndModel] = carId
                carType[makeType] = carId

                val car = ChooserItemModel(carId, year)
                allCarModels.add(car)
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
            allLanguages.clear()
            language.languageList?.map {
                val langId = it.id
                val langName = it.lang
                languages[langName] = langId
            }

            language.languageList!!.forEach {
                val lang = LanguageItem(id = it.id, lang = it.lang)
                allLanguages.add(lang)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun setupDataForCity() {
        // Read and parse the JSON data from the res/raw directory
        val jsonFile = readJSONFromRawResource(resources, R.raw.cities)

        try {
            allCities.clear()
            val jsonObject = JSONObject(jsonFile)
            val city: City = Gson().fromJson(jsonObject.toString(), City::class.java)
            city.states?.map {
                val stateId = it.stateId
                val stateName = it.state
                val countryId = it.countryId
                val cityItem = CityItem(countryId, stateId, stateName)
                allCities.add(it)
                citiesModel[cityItem] = stateId
                //  cities[stateName] = stateId
            }
            filterCitiesByCountryId()
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

    override fun showLoading() {
        binding.AuthProgressLoading.visible()
    }

    override fun hideLoading() {
        binding.AuthProgressLoading.gone()
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
    }
}
