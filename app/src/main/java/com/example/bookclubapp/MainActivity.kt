package com.example.bookclubapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.bookclubapp.R
import com.example.bookclubapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var firebaseAuth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        setBottomNavigation()
        setNavigationDrawer()




    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }





    private fun setBottomNavigation() {
        bottomNavigationView = binding.bottomNavigationView
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.visibility = if (destination.id == R.id.signUpFragment ||
                destination.id == R.id.loginFragment
            ) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }


    }


    private fun setNavigationDrawer() {
        drawerLayout = binding.drawerLayout
        val navigationView = binding.navigationView

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homePageFragment ||
                destination.id == R.id.searchFragment ||
                destination.id == R.id.favouritesFragment ||
                destination.id == R.id.profileFragment ||
                destination.id == R.id.readFragment ||
                destination.id == R.id.readingListFragment ||
                destination.id == R.id.bookDetailFragment
            ) {

                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                actionBarDrawerToggle.isDrawerIndicatorEnabled = true
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                actionBarDrawerToggle.syncState()
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                actionBarDrawerToggle.isDrawerIndicatorEnabled = false
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }


        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.baseline_menu_24)


        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.readFragment -> navController.navigate(R.id.readFragment)
                R.id.readingListFragment -> navController.navigate(R.id.readingListFragment)
                R.id.logout_item -> showLogoutConfirmationDialog()
            }
            true
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }


    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ -> logout() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()

        navController.navigate(R.id.loginFragment)
    }

    override fun onStart() {
        super.onStart()
        actionBarDrawerToggle.syncState()
    }
}