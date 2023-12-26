package com.visualinnovate.almursheed.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import com.visualinnovate.almursheed.auth.model.CarItem
import com.visualinnovate.almursheed.auth.model.CityItem
import com.visualinnovate.almursheed.auth.model.CountryItem
import com.visualinnovate.almursheed.auth.model.LanguageItem
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import java.lang.Math.ceil
import java.util.Locale

object Utils {

    var selectedCountryId: String = ""
    var selectedNationalName: String = ""

    val allNationalities = ArrayList<String>()
    val allCountries = ArrayList<CountryItem>()
    val allCities = ArrayList<CityItem>()

    val allLanguages = ArrayList<LanguageItem>()

    val allCitiesString: ArrayList<String>
        get() {
            allCities.toMutableList().map { it.state }.also {
                return it as ArrayList<String>
            }
        }

    val filteredNationalitiesString = ArrayList<String>()
    val filteredNationalities = ArrayList<CountryItem>()

    val filteredCitiesString = ArrayList<String>()
    val filteredCities = ArrayList<CityItem>()

    val filteredCountriesString = ArrayList<String>()
    val filteredCountries = ArrayList<CountryItem>()

    val allCarModels = ArrayList<ChooserItemModel>()
    val allCarBrand = ArrayList<CarItem>()

    val citiesModel = HashMap<CityItem, String>()
    val carYears = HashMap<String, String>()
    val carBrand = HashMap<String, String>()
    val carType = HashMap<String, String>()
    val carModel = HashMap<String, String>()
    val languages = HashMap<String, Int>()

    var conversationId: Int? = null

    fun filterCitiesByCountryId() {
        filteredCities.clear()
        filteredCitiesString.clear()
        allCities.toMutableList().filter {
            it.countryId == selectedCountryId
        }.also {
            filteredCities.addAll(it)
            it.forEach { cityItem ->
                filteredCitiesString.add(cityItem.state)
            }
        }
    }

    fun filterCountriesByNationality() {
        filteredCountries.clear()
        filteredCountriesString.clear()
        allCountries.toMutableList().filter {
            it.nationality == selectedNationalName
        }.also {
            filteredCountries.addAll(it)
            it.forEach { item ->
                filteredCountriesString.add(item.country)
            }
        }
    }

    @JvmStatic
    fun appLanguageAndScreenZoom(context: Context, language: String) {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val snap = 20
        val exactDpi: Float = (displayMetrics.xdpi + displayMetrics.ydpi) / 2
        val dpi: Int = displayMetrics.densityDpi
        val targetDpi = (ceil((exactDpi / snap).toDouble()) * snap).toInt()
        val resources: Resources = context.resources
        val config: Configuration = resources.configuration

        if (dpi - exactDpi > snap) {
            displayMetrics.densityDpi = targetDpi
            config.densityDpi = targetDpi + 15
            displayMetrics.setTo(displayMetrics)
        }
        val locale = Locale("en")
        Locale.setDefault(locale)
        config.setLocale(locale)
        config.fontScale = 0.91f
        config.setLocale(locale)
        resources.updateConfiguration(
            config,
            displayMetrics,
        )
    }
}
