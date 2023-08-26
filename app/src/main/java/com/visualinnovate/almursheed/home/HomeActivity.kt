package com.visualinnovate.almursheed.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.ViewsManager
import com.visualinnovate.almursheed.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), ViewsManager {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_home)
        binding.bottomNavBar.setItemSelected(R.id.action_home)
        binding.bottomNavBar.setOnItemSelectedListener(object :
                ChipNavigationBar.OnItemSelectedListener {
                override fun onItemSelected(id: Int) {
                    when (id) {
                        R.id.action_home -> navController.navigate(R.id.homeFragment)
                        R.id.action_hireFragment -> navController.navigate(R.id.hireFragment)
                        R.id.action_accommodationFragment -> navController.navigate(R.id.accommodationFragment)
                        R.id.action_flightReservation -> navController.navigate(R.id.flightReservationFragment)
                        R.id.action_more -> navController.navigate(R.id.moreFragment)
                    }
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun changeSelectedBottomNavListener(id: Int) { // resource
        if (this::binding.isInitialized) {
            binding.bottomNavBar.setItemSelected(id)
        }
    }
}
