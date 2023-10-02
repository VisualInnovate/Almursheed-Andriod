package com.visualinnovate.almursheed.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.auth.model.CityItem
import com.visualinnovate.almursheed.auth.model.CountryItem

object Utils {

    var selectedCountryId: String = ""
    var selectedNationalName: String = ""
    val allCountries = ArrayList<CountryItem>()
    val allCities = ArrayList<CityItem>()
    val allNationalities = ArrayList<String>()

    val filteredCitiesString = ArrayList<String>()
    val filteredCities = ArrayList<CityItem>()

    val filteredCountriesString = ArrayList<String>()
    val filteredCountries = ArrayList<CountryItem>()

    val citiesModel = HashMap<CityItem, String>()
    val carYears = HashMap<String, String>()
    val carBrand = HashMap<String, String>()
    val carType = HashMap<String, String>()
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
