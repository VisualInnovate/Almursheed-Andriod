package com.visualinnovate.almursheed.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.auth.model.CityItem
import com.visualinnovate.almursheed.auth.model.CountryItem
import com.visualinnovate.almursheed.auth.model.LanguageItem
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel

object Utils {

    var selectedCountryId: String = ""
    var selectedNationalName: String = ""
    val allCountries = ArrayList<CountryItem>()
    val allLanguages = ArrayList<LanguageItem>()
    val allCities = ArrayList<CityItem>()
    val allNationalities = ArrayList<String>()

    fun getStateName(stateId: Int): String {
        var stateName = ""
        allCities.forEach { it ->
            if (it.stateId == stateId.toString()) {
                stateName = it.state
            }
        }
        return stateName
    }

    fun getCountryName(stateId: Int): String {
        var stateName = ""
        allCities.forEach { it ->
            if (it.stateId == stateId.toString()) {
                stateName = it.state
            }
        }
        return stateName
    }

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

    val citiesModel = HashMap<CityItem, String>()
    val carYears = HashMap<String, String>()
    val carBrand = HashMap<String, String>()
    val carType = HashMap<String, String>()
    val carModel = HashMap<String, String>()
    val languages = HashMap<String, Int>()

    fun loadImage(context: Context, urlToImage: Int, imgView: ImageView) {
        Glide.with(context)
            .load(urlToImage)
            .into(imgView)
    }

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
}
