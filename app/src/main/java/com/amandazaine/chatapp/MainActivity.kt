package com.amandazaine.chatapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amandazaine.chatapp.adapter.ViewPagerAdapter
import com.amandazaine.chatapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initToolbar()
        initTabNavigation()
    }

    private fun initTabNavigation() {
        val tabLayout = binding.tabLayout
        tabLayout.isTabIndicatorFullWidth = false

        val viewPager = binding.viewPager

        val tabTitles = listOf("Chats", "Contacts")

        viewPager.adapter = ViewPagerAdapter(tabTitles, supportFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun initToolbar() {
        val toolbar = binding.includeMainToolbar.materialToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "ChatApp"
        }

        addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(
                    menu: Menu,
                    menuInflater: MenuInflater
                ) {
                    menuInflater.inflate(R.menu.main_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.menu_opt_profile -> {
                            startActivity(Intent(applicationContext, ProfileActivity::class.java))
                            return true
                        }
                        R.id.menu_opt_logout -> {
                            AlertDialog
                                .Builder(this@MainActivity)
                                .setTitle("Log Out")
                                .setMessage("Are you sure you want to log out?")
                                .setPositiveButton("Yes") { _, _ ->
                                    firebaseAuth.signOut()
                                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                                }
                                .setNegativeButton("No", null)
                                .create()
                                .show()
                            return true
                        }
                        else -> return false
                    }
                }
            }
        )

    }
}