package com.example.majika

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.majika.databinding.ActivityMainBinding
import com.example.majika.utils.ManagePermission


//this is the entry point of the app.
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var managePermission: ManagePermission

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//       initialize view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applicationContext.deleteDatabase(R.string.database_name.toString())
        // Ask for permission
        managePermission = ManagePermission(this, PERMISSION_LIST, PERMISSION_REQUEST_CODE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            managePermission.checkPermissions()
        }

//       initialize bottom navigation view and bottom navigation controller, and merge them

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        this.supportActionBar?.setDisplayShowCustomEnabled(true)
        this.supportActionBar?.setCustomView(R.layout.action_bar)

    }

    fun setTitle(title: String){
        val view = this.supportActionBar?.customView
        val titleText: TextView = view!!.findViewById(R.id.action_bar_title)
        val tempText: TextView = view.findViewById(R.id.temperature_text)
        titleText.text = title
        tempText.text = ""
    }

    companion object {
        const val FRAGMENT = "fragment"
        const val PERMISSION_REQUEST_CODE = 123
        val PERMISSION_LIST = listOf<String>(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.POST_NOTIFICATIONS
        )
    }
}