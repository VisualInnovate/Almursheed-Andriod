package com.visualinnovate.almursheed.auth

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.Car
import com.visualinnovate.almursheed.auth.model.City
import com.visualinnovate.almursheed.auth.model.Country
import com.visualinnovate.almursheed.auth.model.Language
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.ActivityAuthBinding
import com.visualinnovate.almursheed.utils.Utils.carBrand
import com.visualinnovate.almursheed.utils.Utils.carType
import com.visualinnovate.almursheed.utils.Utils.carYears
import com.visualinnovate.almursheed.utils.Utils.cities
import com.visualinnovate.almursheed.utils.Utils.countries
import com.visualinnovate.almursheed.utils.Utils.languages
import com.visualinnovate.almursheed.utils.Utils.nationalities
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(), AuthViewsManager {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val jsonObject = JSONObject(jsonFile)
            val country: Country = Gson().fromJson(jsonObject.toString(), Country::class.java)
            country.countryList?.filter {
                it.lang == SharedPreference.getLanguage()
            }.also {
                it?.forEach { item ->
                    val countryName = item.country
                    val countryId = item.country_id
                    val nationalName = item.nationality
                    countries[countryName] = countryId
                    nationalities[nationalName] = countryId
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
                carYears[year] = carId
                carBrand[makeAndModel] = carId
                carType[makeType] = carId
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
                languages[langName] = langId
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
                cities[stateName] = stateId
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

    override fun showLoading() {
        binding.AuthProgressLoading.visible()
    }

    override fun hideLoading() {
        binding.AuthProgressLoading.gone()
    }
}
