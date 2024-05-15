package com.example.metrov6

import android.Manifest
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle
import com.github.ybq.android.spinkit.style.DoubleBounce
import mumayank.com.airlocationlibrary.AirLocation


class load_activity : AppCompatActivity() {

    private lateinit var progressbar: ProgressBar
    private lateinit var airLocation: AirLocation
    private lateinit var locationManager: LocationManager
    private lateinit var textView3: View

    var backPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        textView3 = findViewById(R.id.textView3)
        progressbar = findViewById(R.id.spin_kit)

        val spinKitDrawable = Circle()

//        val doubleBounce: Sprite = DoubleBounce()
        progressbar.indeterminateDrawable = Circle()
        spinKitDrawable.color = resources.getColor(R.color.black)
//        progressbar.indeterminateDrawable = spinKitDrawable

//        val progressBar = findViewById<View>(R.id.spin_kit) as ProgressBar
//        val cubeGrid = CubeGrid()
//        progressbar.indeterminateDrawable = cubeGrid
//        cubeGrid.color = resources.getColor(R.color.black)



        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    startFetchLocation()
                } else {
                    handleLocationPermissionDenied()
                }
            }

        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun handleLocationPermissionDenied() {
        progressbar = findViewById(R.id.spin_kit) as ProgressBar
        progressbar.visibility = View.GONE
        textView3.visibility= View.GONE
        Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun startFetchLocation() {
        airLocation = AirLocation(this, object : AirLocation.Callback {
            override fun onSuccess(locations: ArrayList<Location>) {

                if (!backPressed) {


                    val lat = locations[0].latitude
                    val long = locations[0].longitude
                    val coder = Geocoder(this@load_activity)
                    val add = coder.getFromLocation(lat, long, 1)

                    val address = add?.get(0)?.getAddressLine(0)

                    Handler().postDelayed({
                        progressbar = findViewById(R.id.spin_kit) as ProgressBar
                        progressbar.visibility = View.GONE
                        textView3.visibility = View.GONE


                        if (!address.isNullOrEmpty()) {
                            val intent = Intent(this@load_activity, MainActivity2::class.java)
                            intent.putExtra("address", address)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            finish()
                        }
                    }, 5000)
                }
            }

            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
                progressbar.visibility = View.GONE
                textView3.visibility= View.GONE
                Toast.makeText(this@load_activity, "Failed to fetch location", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@load_activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }, true, 0)
        airLocation.start()
    }




    override fun onResume() {
        super.onResume()

        checkLocationSettings()
    }

    private fun checkLocationSettings() {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled) {
            val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        backPressed = true
        val intent = Intent(this@load_activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK )
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        startActivity(intent)
        finish()
        super.onBackPressed()
    }


}