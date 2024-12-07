package com.sugaryzer.sugaryzer.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.SectionsPagerAdapter
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.di.Injection
import com.sugaryzer.sugaryzer.ui.profile.ProfileViewModel
import com.sugaryzer.sugaryzer.ui.signin.SignInActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adapter: SectionsPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyTheme()
        setContentView(R.layout.activity_main)

        viewModel.getSession().observe(this) { token ->
            if (token.isNullOrEmpty()){
                val signInActivity = Intent(this, SignInActivity::class.java)
                startActivity(signInActivity)
                finish()
            }
        }

        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        adapter = SectionsPagerAdapter(this)
        viewPager.adapter = adapter

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPager.setCurrentItem(0, true)
                R.id.navigation_history -> viewPager.setCurrentItem(1, true)
                R.id.navigation_news -> viewPager.setCurrentItem(2, true)
                R.id.navigation_profile -> viewPager.setCurrentItem(3, true)
            }
            true
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNavigationView.selectedItemId = R.id.navigation_home
                    1 -> bottomNavigationView.selectedItemId = R.id.navigation_history
                    2 -> bottomNavigationView.selectedItemId = R.id.navigation_news
                    3 -> bottomNavigationView.selectedItemId = R.id.navigation_profile
                }
            }
        })
    }

    private fun applyTheme() {
        val repository = Injection.provideRepository(this)
        val mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository)
        ).get(ProfileViewModel::class.java)

        mainViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}