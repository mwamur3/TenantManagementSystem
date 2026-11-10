
package com.example.tenantmanagmentsystem.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tenantmanagmentsystem.R
import com.example.tenantmanagmentsystem.databinding.ActivityMainBinding
import com.example.tenantmanagmentsystem.ui.auth.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TENANT_LIFECYCLE", "MainActivity: onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboardFragment,
                R.id.addTenantFragment,
                R.id.viewTenantsFragment
            ),
            binding.drawerLayout
        )

        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        )

        val headerView = binding.navigationView.getHeaderView(0)
        val emailTextView =
            headerView.findViewById<TextView>(R.id.headerEmail)

        emailTextView.text =
            intent.getStringExtra("EMAIL") ?: "Tenant Administrator"

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.drawerLogout -> {
                    binding.drawerLayout.closeDrawers()
                    logout()
                    true
                }

                R.id.dashboardFragment,
                R.id.addTenantFragment,
                R.id.viewTenantsFragment -> {

                    val destinationId = item.itemId

                    if (navController.currentDestination?.id != destinationId) {
                        navController.navigate(destinationId)
                    }

                    binding.drawerLayout.closeDrawers()
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, 1, Menu.NONE, "View Tenants")
        menu.add(Menu.NONE, 2, Menu.NONE, "Logout")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            1 -> {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

                val navController = navHostFragment.navController

                if (navController.currentDestination?.id != R.id.viewTenantsFragment) {
                    navController.navigate(R.id.viewTenantsFragment)
                }

                true
            }

            2 -> {
                logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        return navHostFragment.navController.navigateUp(
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onDestroy")
    }
}
