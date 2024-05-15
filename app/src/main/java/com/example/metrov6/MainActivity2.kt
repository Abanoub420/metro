package com.example.metrov6


import android.os.Bundle
import com.example.metrov6.R
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.preference.PreferenceManager
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.ShakeDetector

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import mumayank.com.airlocationlibrary.AirLocation
import java.util.Locale


class MainActivity2 : AppCompatActivity(), AirLocation.Callback, ShakeDetector.ShakeListener,
    TextToSpeech.OnInitListener {


    lateinit var airLocation: AirLocation
    lateinit var addView : TextView
    lateinit var btnLocal : Button
    lateinit var viewRouteBtn :Button

    lateinit var tts: TextToSpeech

    var  minDistanceIndex : Int = 0


    var  found = ""


    private var latitude1: Double = 0.0
    private var longitude1: Double = 0.0


    var addLOCALL: Location? = null


    lateinit var mapView: MapView

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var nestedScrollView: NestedScrollView




    class LocationData(val name: String, val latitude: Double, val longitude: Double)

    val locations1 = listOf(
        LocationData("ElMounib", 29.98110, 31.21232),
        LocationData("SakiatMekky", 29.99547, 31.20866),
        LocationData("OmmElMasryeen", 30.00565, 31.20811),
        LocationData("ElGiza", 30.01065, 31.20710),
        LocationData("Faisal", 30.01705, 31.20398),
        LocationData("CairoUniversity", 	30.02600, 31.20117),
        LocationData("ElBohoth", 30.03581, 31.20018),
        LocationData("Dokki", 30.03846, 31.21224),
        LocationData("Opera", 30.04193, 31.22497),
        LocationData("Sadat", 30.04414, 31.23443),
        LocationData("MohamedNaguib", 30.04532, 31.24416),
        LocationData("Attaba", 30.05234, 31.24681),
        LocationData("AlShohadaa", 30.06105, 31.24604),
        LocationData("massra", 30.07193, 31.24502),
        LocationData("roadelfarg", 30.08059, 31.24541),
        LocationData("StTeresa", 30.08795, 31.24549),
        LocationData("Khalafawy", 30.09788, 31.24540),
        LocationData("Mezallat", 30.10417, 31.24564),
        LocationData("KolleyyetElZeraa", 30.11369, 31.24867),
        LocationData("ShubraElKheima", 30.12176, 31.24463),

        )

    private lateinit var prefs: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        prefs = getSharedPreferences("saved", MODE_PRIVATE)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        addView = findViewById(R.id.addView)
        btnLocal = findViewById(R.id.btnLocal)
        viewRouteBtn = findViewById(R.id.viewRouteBtn)


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout2)
        nestedScrollView = findViewById(R.id.nestedScrollView2)



        tts= TextToSpeech(this,this)




        swipeRefreshLayout.setOnRefreshListener {
            // Perform your refresh operation here

            // Simulate a delay for demonstration purposes

           UpdatedAddress()

            val cardView = findViewById<CardView>(R.id.routeCard)
            cardView.visibility = View.GONE



            Handler().postDelayed({
                swipeRefreshLayout.isRefreshing = false
            }, 2000)
        }

        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY == 0) {

                swipeRefreshLayout.isEnabled = true
            } else {
                swipeRefreshLayout.isEnabled = false
            }
        }





        Sensey.getInstance().init(this)
        Sensey.getInstance().startShakeDetection(this)

        btnLocal.setOnClickListener{


            airLocation= AirLocation(this,this,true,0,"")
            airLocation.start()

            addView.setText("")



        }

        var address66 = intent.getStringExtra("address")

        addView.append(" \n $address66 \n")

         address66 = intent.getStringExtra("address")
        val coder = Geocoder(this)
        val addresses2 = address66?.let { coder.getFromLocationName(it, 1) }
        val startLocation = addresses2?.get(0)
        var startLatLng = LatLng(startLocation?.latitude ?: 0.0, startLocation?.longitude ?: 0.0)

        mapView.getMapAsync { googleMap ->
            googleMap.addMarker(
                MarkerOptions().position(startLatLng).title("YOUR LOCATION")
            )

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 18f))
        }







    }

    fun UpdatedAddress() {
        airLocation = AirLocation(this, object : AirLocation.Callback {
            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
                showToast(this@MainActivity2, "Check your internet connection")
                val intent = Intent(this@MainActivity2, MainActivity::class.java)
                startActivity(intent)

            }

            override fun onSuccess(locations: ArrayList<Location>) {

                addView.setText("PRESS NEAREST STATION")

                if (!checkConnection(this@MainActivity2)) {
                    showToast(this@MainActivity2, "Check your internet connection")
                    return
                }

                val currentLocation = locations.firstOrNull()
                if (currentLocation != null) {
                    val coder = Geocoder(this@MainActivity2)
                    val addresses = coder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
                    val startLocation = addresses?.firstOrNull()

                    if (startLocation != null) {
                        val startLatLng = LatLng(startLocation.latitude, startLocation.longitude)

                        mapView.getMapAsync { googleMap ->
                            googleMap.clear()
                            googleMap.addMarker(
                                MarkerOptions().position(startLatLng).title("YOUR LOCATION")
                            )

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 18f))
                        }
                    }
                }
            }


        }, true, 0, "")

        airLocation.start()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLocation.onActivityResult(requestCode, resultCode, data) // ADD THIS LINE INSIDE onActivityResult

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults) // ADD THIS LINE INSIDE onRequestPermissionResult
    }



    override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
        Toast.makeText(this, " XDD failed to get the location, maybe due to lack of the internet", Toast.LENGTH_SHORT).show()

    }

    override fun onSuccess(locations: ArrayList<Location>) {


        if (!checkConnection(this)) {
            showToast(this, "Check your internet connection")
            return
        }

        mapView.onResume()

        val savedLocationElMounib = locations1.find { it.name == "ElMounib" }
        val savedLocationSakiatMekky = locations1.find { it.name == "SakiatMekky" }
        val savedLocationOmmElMasryeen = locations1.find { it.name == "OmmElMasryeen" }
        val savedLocationElGiza = locations1.find { it.name == "ElGiza" }
        val savedLocationlocFaisal = locations1.find { it.name == "locFaisal" }
        val savedLocationCairoUniversity = locations1.find { it.name == "CairoUniversity" }
        val savedLocationElBohoth = locations1.find { it.name == "ElBohoth" }
        val savedLocationDokki = locations1.find { it.name == "Dokki" }
        val savedLocationOpera = locations1.find { it.name == "Opera" }
        val savedLocationSadat = locations1.find { it.name == "Sadat" }
        val savedLocationMohamedNaguib = locations1.find { it.name == "MohamedNaguib" }
        val savedLocationAttaba = locations1.find { it.name == "Attaba" }
        val savedLocationAlShohadaa = locations1.find { it.name == "AlShohadaa" }
        val savedLocationMassra = locations1.find { it.name == "massra" }
        val savedLocationRoadelfarg = locations1.find { it.name == "roadelfarg" }
        val savedLocationStTeresa = locations1.find { it.name == "StTeresa" }
        val savedLocationKhalafawy= locations1.find { it.name == "Khalafawy" }
        val savedLocationMezallat= locations1.find { it.name == "Mezallat" }
        val savedLocationKolleyyetElZeraa= locations1.find { it.name == "KolleyyetElZeraa" }
        val savedLocationShubraElKheima= locations1.find { it.name == "ShubraElKheima" }






        val locElMounib = Location("")
        locElMounib.latitude = savedLocationElMounib?.latitude ?: 0.0
        locElMounib.longitude = savedLocationElMounib?.longitude ?: 0.0

        val locSakiatMekky = Location("")
        locSakiatMekky.latitude = savedLocationSakiatMekky?.latitude ?: 0.0
        locSakiatMekky.longitude = savedLocationSakiatMekky?.longitude ?: 0.0

        val locOmmElMasryeen = Location("")
        locOmmElMasryeen.latitude = savedLocationOmmElMasryeen?.latitude ?: 0.0
        locOmmElMasryeen.longitude =  savedLocationOmmElMasryeen?.longitude ?: 0.0

        val locElGiza = Location("")
        locElGiza.latitude = savedLocationElGiza?.latitude ?: 0.0
        locElGiza.longitude = savedLocationElGiza?.longitude ?: 0.0

        val locFaisal = Location("")
        locFaisal.latitude = savedLocationlocFaisal?.latitude ?: 0.0
        locFaisal.longitude = savedLocationlocFaisal?.longitude ?: 0.0

        val locCairoUniversity = Location("")
        locCairoUniversity.latitude = savedLocationCairoUniversity?.latitude ?: 0.0
        locCairoUniversity.longitude = savedLocationCairoUniversity?.longitude ?: 0.0

        val locElBohoth = Location("")
        locElBohoth.latitude = savedLocationElBohoth?.latitude ?: 0.0
        locElBohoth.longitude = savedLocationElBohoth?.longitude ?: 0.0

        val locDokki = Location("")
        locDokki.latitude = savedLocationDokki?.latitude ?: 0.0
        locDokki.longitude = savedLocationDokki?.longitude ?: 0.0

        val locOpera = Location("")
        locOpera.latitude = savedLocationOpera?.latitude ?: 0.0
        locOpera.longitude = savedLocationOpera?.longitude ?: 0.0

        val locSadat = Location("")
        locSadat.latitude = savedLocationSadat?.latitude ?: 0.0
        locSadat.longitude = savedLocationSadat?.longitude ?: 0.0

        val locMohamedNaguib = Location("")
        locMohamedNaguib.latitude = savedLocationMohamedNaguib?.latitude ?: 0.0
        locMohamedNaguib.longitude = savedLocationMohamedNaguib?.longitude ?: 0.0

        val locAttaba = Location("")
        locAttaba.latitude = savedLocationAttaba?.latitude ?: 0.0
        locAttaba.longitude = savedLocationAttaba?.longitude ?: 0.0

        val locAlShohadaa = Location("")
        locAlShohadaa.latitude = savedLocationAlShohadaa?.latitude ?: 0.0
        locAlShohadaa.longitude = savedLocationAlShohadaa?.longitude ?: 0.0


        val locMassra = Location("")
        locMassra.latitude = savedLocationMassra?.latitude ?: 0.0
        locMassra.longitude = savedLocationMassra?.longitude ?: 0.0


        val locRoadElFarag = Location("")
        locRoadElFarag.latitude = savedLocationRoadelfarg?.latitude ?:0.0
        locRoadElFarag.longitude = savedLocationRoadelfarg?.longitude ?: 0.0

        val locStTerssa = Location("")
        locStTerssa.latitude = savedLocationStTeresa?.latitude ?: 0.0
        locStTerssa.longitude = savedLocationStTeresa?.longitude ?: 0.0


        val locKhalafawy = Location("")
        locKhalafawy.latitude = savedLocationKhalafawy?.latitude ?: 0.0
        locKhalafawy.longitude = savedLocationKhalafawy?.longitude ?: 0.0

        val locMezallat = Location("")
        locMezallat.latitude = savedLocationMezallat?.latitude ?: 0.0
        locMezallat.longitude = savedLocationMezallat?.longitude ?: 0.0

        val locKolleyyetElZeraa = Location("")
        locKolleyyetElZeraa.latitude = savedLocationKolleyyetElZeraa?.latitude ?: 0.0
        locKolleyyetElZeraa.longitude = savedLocationKolleyyetElZeraa?.longitude ?: 0.0

        val locShubraElKheima = Location("")
        locShubraElKheima.latitude = savedLocationShubraElKheima?.latitude ?: 0.0
        locShubraElKheima.longitude = savedLocationShubraElKheima?.longitude ?: 0.0



        val address66 = intent.getStringExtra("address")



        val coder= Geocoder(this)

        val myLoca="$address66"

        val addresses2=coder.getFromLocationName(myLoca,1)

         addLOCALL=Location("")
        addLOCALL!!.latitude= addresses2?.get(0)?.latitude ?: 0.0
        addLOCALL!!.longitude=addresses2?.get(0)?.longitude?:0.0




        val distances = listOf(
            addLOCALL!!.distanceTo(locElMounib),
            addLOCALL!!.distanceTo(locSakiatMekky),
            addLOCALL!!.distanceTo(locOmmElMasryeen),
            addLOCALL!!.distanceTo(locElGiza),
            addLOCALL!!.distanceTo(locFaisal),
            addLOCALL!!.distanceTo(locCairoUniversity),
            addLOCALL!!.distanceTo(locElBohoth),
            addLOCALL!!.distanceTo(locDokki),
            addLOCALL!!.distanceTo(locOpera),
            addLOCALL!!.distanceTo(locSadat),
            addLOCALL!!.distanceTo(locMohamedNaguib),
            addLOCALL!!.distanceTo(locAttaba),
            addLOCALL!!.distanceTo(locAlShohadaa),
            addLOCALL!!.distanceTo(locMassra),
            addLOCALL!!.distanceTo(locRoadElFarag),
            addLOCALL!!.distanceTo(locStTerssa),
            addLOCALL!!.distanceTo(locMezallat),
            addLOCALL!!.distanceTo(locKolleyyetElZeraa),
            addLOCALL!!.distanceTo(locShubraElKheima)
        )

        minDistanceIndex = distances.indexOf(distances.min())


        val switchFile = PreferenceManager.getDefaultSharedPreferences(this)
        val soundSetting = switchFile.getBoolean("savedBlack", false)


        val language = prefs.getString("langcode", null)



        if (minDistanceIndex == 0) {
            addView.append(" YOU ARE Close to ElMounib")
            found = "El Mounib"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 1) {
            addView.append(" YOU ARE Close to SakiatMekky")
            found = "Sakiat Mekky"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 2) {
            addView.append(" YOU ARE Close to OmmElMasryeen")
            found = "Omm El Masryeen"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 3) {
            addView.append(" YOU ARE Close to ElGiza")
            found = "ElGiza"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 4) {
            addView.append(" YOU ARE Close to Faisal")
            found = "Faisal"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 5) {
            addView.append(" YOU ARE Close to CairoUniversity")
            found = "Cairo University"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 6) {
            addView.append(" YOU ARE Close to El Bohoth")
            found = "El Bohoth"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 7) {
            addView.append(" YOU ARE Close to Dokki")
            found = "Dokki"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 8) {
            addView.append(" YOU ARE Close to Opera")
            found = "Opera"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 9) {
            addView.append(" YOU ARE Close to Sadat")
            found = "Sadat"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 10) {
            addView.append(" YOU ARE Close to MohamedNaguib")
            found = "Mohamed Naguib"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 11) {
            addView.append(" YOU ARE Close to Attaba")
            found = "Attaba"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 12) {
            addView.append(" YOU ARE Close to AlShohadaa")
            found = "Al Shohadaa"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 13) {
            found = "Masarra"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    if (language == "en") {
                        addView.append("YOU ARE Close To Masarra Station")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    if (language == "ar"){
                        addView.append("أنت قريب من محطة مسرة")
                        tts.language = Locale("ar")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            } else {
                if (language == "en") {
                    addView.append("YOU ARE Close To Masarra Station")
                }
                if (language == "ar"){
                    addView.append("أنت قريب من محطة مسرة")
                }
                tts.stop()
            }
        } else if (minDistanceIndex == 14) {

            found = "Road El Farag"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    if (language == "en") {
                        addView.append("YOU ARE Close To Road ElFarg Station")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    if (language == "ar"){
                        addView.append("أنت قريب من محطة روض الفرج")
                        tts.language = Locale("ar")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 15) {

            found = "St Teresa"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    if (language == "en") {
                        addView.append("YOU ARE Close To StTeresa Station")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    if (language == "ar"){
                        addView.append("أنت قريب من محطة سانت تريزا")
                        tts.language = Locale("ar")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 16) {
            found = "Khalafawy"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    if (language == "en") {
                        addView.append("YOU ARE Close To Khalafawy Station")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    if (language == "ar"){
                        addView.append("أنت قريب من محطة الخلفاوى")
                        tts.language = Locale("ar")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 17) {
            found = "Mezallat"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    if (language == "en") {
                        addView.append("YOU ARE Close To Mezallat Station")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    if (language == "ar"){
                        addView.append("أنت قريب من محطة المظلات")
                        tts.language = Locale("ar")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }

                    }
            } else {
                tts.stop()
            }
        } else if (minDistanceIndex == 18) {
            found = "Kolleyyet El Zeraa"

            if (soundSetting) {
                found.let {
                    if (language == "en") {
                        addView.append("YOU ARE Close To Kolleyyet El Zeraa Station")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    if (language == "ar"){
                        addView.append("أنت قريب من محطة كلية الزراعة")
                        tts.language = Locale("ar")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)

                }
            } else {
                if (language == "en") {
                    addView.append("YOU ARE Close To Kolleyyet El Zeraa Station")
                }
                if (language == "ar"){
                    addView.append("أنت قريب من محطة كلية الزراعة")
                    tts.language = Locale("ar")
                }
                YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
                tts.stop()
            }
        } else if (minDistanceIndex == 19) {
            found = "Shubra El Kheima"
            YoYo.with(Techniques.FadeIn).duration(500).playOn(addView)
            if (soundSetting) {
                found.let {
                    if (language == "en") {
                        addView.append("YOU ARE Close To Shubra El Kheima Station")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    if (language == "ar"){
                        addView.append("أنت قريب من محطة شبرا الخيمة")
                        tts.language = Locale("ar")
                        tts.speak(addView.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            } else {
                tts.stop()
            }
        }

         if (found != null){
             val cardView = findViewById<CardView>(R.id.routeCard)
             cardView.visibility = View.VISIBLE



             viewRouteBtn.setOnClickListener{
                 val destination = locations1[minDistanceIndex].name // Destination address

//                 val gmmIntentUri = Uri.parse("google.navigation:q=$destination")
//                 val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                 mapIntent.setPackage("com.google.android.apps.maps")
                 val webIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$destination&travelmode=walking")

                 val webIntent = Intent(Intent.ACTION_VIEW, webIntentUri)

                 if (webIntent.resolveActivity(packageManager) != null) {
                     startActivity(webIntent)
                 } else {
                     val webIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$destination&travelmode=walking")

                     val webIntent = Intent(Intent.ACTION_VIEW, webIntentUri)
                     startActivity(webIntent)
                     Toast.makeText(this, "opening Google Maps website in a browser", Toast.LENGTH_SHORT).show()
                 }
             }


         }

        if (found.isNotEmpty()) {
            val prefs = getSharedPreferences("stationNamePrefs", MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("stationName", found)
            editor.apply()
        }


        val smallestLocation = locations1[minDistanceIndex]
        latitude1 = smallestLocation.latitude
        longitude1 = smallestLocation.longitude




//        mapView.getMapAsync { googleMap ->
//            val startLatLng = LatLng(addLOCALL!!.latitude, addLOCALL!!.longitude)
//            val smallestLatLng = LatLng(latitude1, longitude1)
//
////            addView.append(" \n ( $startLatLng ) ( $smallestLatLng) \n")
//
//            googleMap.addMarker(
//                MarkerOptions().position(startLatLng).title("YOUR LOCATION")
//            )
//
//            googleMap.addMarker(
//                MarkerOptions().position(smallestLatLng).title("THE NEAREST STATION")
//            )
//
//            googleMap.addPolyline(
//                PolylineOptions()
//                    .add(startLatLng, smallestLatLng)
//                    .width(15f)
//                    .color(Color.BLACK)
//            )
//
//            val boundsBuilder = LatLngBounds.Builder()
//                .include(startLatLng)
//                .include(smallestLatLng)
//
//            val padding = resources.getDimensionPixelSize(R.dimen.map_padding)
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), padding))
//
//
//
//
//        }

        mapView.getMapAsync { googleMap ->
            val startLatLng = LatLng(addLOCALL!!.latitude, addLOCALL!!.longitude)
            val smallestLatLng = LatLng(latitude1, longitude1)

            googleMap.addMarker(
                MarkerOptions().position(startLatLng).title("YOUR LOCATION")
            )

            googleMap.addMarker(
                MarkerOptions().position(smallestLatLng).title("THE NEAREST STATION")
            )
            val geoApiContext = GeoApiContext.Builder()
                .apiKey("AIzaSyDLlZ5zIWvoumSyKTAuEi97gKXU6_JgpPs")
                .build()

            // Create a Directions API request
            val directionsRequest = DirectionsApi.newRequest(geoApiContext)
                .origin("$startLatLng")
                .destination("$smallestLatLng")
                .mode(TravelMode.WALKING) // You can change the travel mode if needed

            // Execute the request synchronously
            try {
                val result = directionsRequest.await()

                // Extract the polyline from the response
                val polyline = result.routes[0].overviewPolyline
                val decodedPath = PolyUtil.decode(polyline.encodedPath)

                // Add the polyline to the map
                googleMap.addPolyline(
                    PolylineOptions()
                        .addAll(decodedPath)
                        .width(5f)
                        .color(Color.BLACK)
                )

             //    Adjust the camera to fit both markers and the polyline
                val boundsBuilder = LatLngBounds.Builder()
                    .include(startLatLng)
                    .include(smallestLatLng)

                decodedPath.forEach { latLng ->
                    boundsBuilder.include(latLng)
                }

                val padding = resources.getDimensionPixelSize(R.dimen.map_padding)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), padding))
            } catch (e: Exception) {
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                val editor = prefs.edit()
                editor.putString("message", e.message)
                editor.apply()
            }
        }


    }




    fun checkConnection(context: Context) :Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.M){
            val network  = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return when {
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true ->{
                    // Connected via Wi-Fi
//                    showToast(context, "Good internet connection (Wi-Fi)")
                    true
                }
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ->{
                    // Connected via cellular network
//                    showToast(context, "Good internet connection (Cellular)")
                    true
                }
                else -> {
                    // Not connected to a valid network
                    showToast(context, "Poor or no internet connection")
                    false
                }
            }
        }else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            return activeNetworkInfo != null && activeNetworkInfo.isConnected

//            if (activeNetworkInfo != null && activeNetworkInfo.isConnected){
//                // Connected to the internet
//                showToast(context, "Good internet connection")
//            }else{
//                // Not connected to the internet
//                showToast(context, "Poor or no internet connection")
//            }

        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showStopMovingToast(context: Context, message: String) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager          ////get what activity is goin on
        val packageName = activityManager.runningAppProcesses?.get(0)?.processName

        val isGoogleMapsOpen = packageName == "com.google.android.apps.maps"

        if (!isGoogleMapsOpen) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onShakeDetected() {
    }

    override fun onShakeStopped() {
//        Toast.makeText(this, "plz stop moving to get an accurate station", Toast.LENGTH_SHORT).show()
        val message = "Please stop moving to get an accurate station"
        showStopMovingToast(this, message )
    }

    override fun onDestroy() {
        Sensey.getInstance().stopShakeDetection(this)
        Sensey.getInstance().stop()
        mapView.onDestroy()
        super.onDestroy()
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Sensey.getInstance().stopShakeDetection(this)
        Sensey.getInstance().stop()
        super.onBackPressed()

    }

    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS) {
            tts.setSpeechRate(0.7.toFloat())
            tts.setPitch(0.7.toFloat())
        }
    }


}