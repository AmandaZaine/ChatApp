package com.amandazaine.chatapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amandazaine.chatapp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private var hasPermissionCamera = false
    private var hasPermissionGallery = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
        requestPermissions()
    }

    private fun initToolbar() {
        val toolbar = binding.includeToolbarProfile.materialToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Edit Profile"
            setDisplayHomeAsUpEnabled(true)
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermissions() {

        //Verify if the user has granted the permission
        val checkPermissionCamera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val checkPermissionGallery = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)

        hasPermissionCamera = checkPermissionCamera == PackageManager.PERMISSION_GRANTED
        hasPermissionGallery = checkPermissionGallery == PackageManager.PERMISSION_GRANTED

        //If the user has not granted the permission, we map the required permissions to a list
        var deniedPermissions = mutableListOf<String>()

        if (!hasPermissionCamera) deniedPermissions.add(android.Manifest.permission.CAMERA)
        if (!hasPermissionGallery) deniedPermissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        //If the list is not empty, we request the permissions
        if( deniedPermissions.isNotEmpty() ) {

            //We use the ActivityResultLauncher to handle the result of the permission request
            var permissionManager = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                hasPermissionCamera = it[android.Manifest.permission.CAMERA] ?: hasPermissionCamera
                hasPermissionGallery = it[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?: hasPermissionGallery
            }

            permissionManager.launch(deniedPermissions.toTypedArray() )
        }
    }
}