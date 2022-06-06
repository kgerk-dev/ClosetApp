package com.example.allin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Defines a Navigation Host, and creates a Bottom Nav bar.
        val host : NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_frag_host) as NavHostFragment? ?: return

        navController = host.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bot_nav_view)
        bottomNavigationView.setupWithNavController(navController)

        //appBarConfiguration = AppBarConfiguration(navController.graph)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.clothingListFragment, R.id.outfitListFragment, R.id.calendarFragment, R.id.packingListFragment)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.addClothingFragment || nd.id == R.id.updateClothingFragment || nd.id == R.id.addOutfitFragment
                || nd.id == R.id.addClothingToOutfits || nd.id == R.id.clothingTopsList || nd.id == R.id.clothingBotList
                || nd.id == R.id.clothingShoesList || nd.id == R.id.clothingOuterWearList || nd.id == R.id.favoriteListFragment ||
                nd.id == R.id.outfitDetailsFragment || nd.id == R.id.addPackingListItemFragment || nd.id == R.id.packingListChooseOutfits)
            {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
    /*- FUNCTIONS REQUIRED TO INFLATE THE BOTTOM NAV MENU AND NAVIGATE WITH THE NAV GRAPH --------*/

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}