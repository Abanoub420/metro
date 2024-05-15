package com.example.metrov6

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.textclassifier.TextClassifier
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import mumayank.com.airlocationlibrary.AirLocation
import org.intellij.lang.annotations.Language
import java.util.Locale


class MainActivity3 : AppCompatActivity() ,  AirLocation.Callback {

    lateinit var airLocation : AirLocation
    lateinit var textAddress : EditText
    lateinit var foundStation : TextView

   lateinit var  loc1 : Location

   lateinit var viewLocation :Button

    var found = ""

    private var latitude1: Double = 0.0
    private var longitude1: Double = 0.0
    lateinit var mapView: MapView

    private lateinit var prefs : SharedPreferences



    class LocationData(val name: String, val latitude: Double, val longitude: Double)

    val locations = listOf(
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        textAddress = findViewById(R.id.textAddress)
        foundStation =findViewById(R.id.foundStation)
        viewLocation = findViewById(R.id.viewLocation)

        prefs = getSharedPreferences("saved", MODE_PRIVATE)

        val clearButton : Drawable? = ContextCompat.getDrawable(this , R.drawable.ic_clear_button)
        val clearButtonVisibleThreshold = 1


        clearButton?.let { drawable ->
            textAddress.setOnTouchListener{ _ , event ->
                if (event.action == MotionEvent.ACTION_UP ){
                    val clearBounds = drawable.bounds
                    if (event.rawX >= textAddress.right - clearBounds.width()) {
                       textAddress.text = null
                        return@setOnTouchListener true
                    }
                }
                false
            }
        }

        textAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                val currentLanguage = detectLanguage(p0)
//                updateClearButtonPosition(currentLanguage)
                if ((p0?.length ?: 0) >= clearButtonVisibleThreshold){
                    textAddress.setCompoundDrawablesWithIntrinsicBounds(null, null,clearButton,null)
                }else{
                    textAddress.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })



    }


//    private fun updateClearButtonPosition(language: Locale) {
//        if (language.language == "ar") {
//            textAddress.setCompoundDrawablesWithIntrinsicBounds(clearButton, null,null,null)
//        } else {
//            textAddress.setCompoundDrawablesWithIntrinsicBounds(null, null,clearButton,null)
//        }
//    }

//    val clearButton : Drawable? = ContextCompat.getDrawable(this , R.drawable.ic_clear_button)
//
//    private fun detectLanguage(text: CharSequence?): Locale {
//        var lang = Locale.getDefault()
//        if (!text.isNullOrEmpty()) {
//            val firstChar = text[0]
//            if (Character.getDirectionality(firstChar.toInt()) == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
//                Character.getDirectionality(firstChar.toInt()) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
//            ) {
//                lang = Locale("ar")
//            } else {
//                lang = Locale("en")
//            }
//        }
//        return lang
//    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLocation.onActivityResult(requestCode, resultCode, data) // ADD THIS LINE INSIDE onActivityResult

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults) // ADD THIS LINE INSIDE onRequestPermissionResult
    }


    override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(locations: ArrayList<Location>) {
    }



    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun checkConnection(context :Context) : Boolean{
        val connectivityManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){

            val network =  connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

            return when {
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> {
                    true
                }
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ->{
                    true
                }

                else -> {
                    showToast(context, "Check you internet connection")
                    false
                }
            }

        }else{
            val  activeNetworkInfo =  connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    fun nearByMe2(view: View) {

        ///////////////////////////////////////////////////////////////////////////         hide the keyboard
        val hideKeyb = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideKeyb.hideSoftInputFromWindow(foundStation.windowToken, 0)
//////////////////////////////////////////////////////////


        val language = prefs.getString("langcode", null)


        foundStation.text=""

        val coder = Geocoder(this)
        var address="${textAddress.text.toString().trim()}"
        if (address.isNotEmpty()) {


            if (!checkConnection(this)) {
                if (language != null) {
                    showToasts(this,language,2)
                }
                return
            }


            val addresses = coder.getFromLocationName(address, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                loc1 = Location("")
                loc1.latitude = addresses[0].latitude
                loc1.longitude = addresses[0].longitude
                val cairoLat = 30.044417
                val cairoLong = 31.235721


                if (loc1.latitude < cairoLat && loc1.longitude < cairoLong){

                    Toast.makeText(this, "inside cairo", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "outside cairo", Toast.LENGTH_SHORT).show()

                }

            } else {
                if (language != null){
                    showToasts(this, language,1)
                }
                YoYo.with(Techniques.Shake).duration(300).repeat(2).playOn(textAddress)
                return
            }
        } else {
            if (language != null) {
                showToasts(this,language,0)
            }
            YoYo.with(Techniques.Shake).duration(300).repeat(2).playOn(textAddress)
            val cardView11 = findViewById<CardView>(R.id.desStation)
            cardView11.visibility = View.GONE
            return
        }

        val savedLocationElMounib = locations.find { it.name == "ElMounib" }
        val savedLocationSakiatMekky = locations.find { it.name == "SakiatMekky" }
        val savedLocationOmmElMasryeen = locations.find { it.name == "OmmElMasryeen" }
        val savedLocationElGiza = locations.find { it.name == "ElGiza" }
        val savedLocationlocFaisal = locations.find { it.name == "locFaisal" }
        val savedLocationCairoUniversity = locations.find { it.name == "CairoUniversity" }
        val savedLocationElBohoth = locations.find { it.name == "ElBohoth" }
        val savedLocationDokki = locations.find { it.name == "Dokki" }
        val savedLocationOpera = locations.find { it.name == "Opera" }
        val savedLocationSadat = locations.find { it.name == "Sadat" }
        val savedLocationMohamedNaguib = locations.find { it.name == "MohamedNaguib" }
        val savedLocationAttaba = locations.find { it.name == "Attaba" }
        val savedLocationAlShohadaa = locations.find { it.name == "AlShohadaa" }
        val savedLocationMassra = locations.find { it.name == "massra" }
        val savedLocationRoadelfarg = locations.find { it.name == "roadelfarg" }
        val savedLocationStTeresa = locations.find { it.name == "StTeresa" }
        val savedLocationKhalafawy= locations.find { it.name == "Khalafawy" }
        val savedLocationMezallat= locations.find { it.name == "Mezallat" }
        val savedLocationKolleyyetElZeraa= locations.find { it.name == "KolleyyetElZeraa" }
        val savedLocationShubraElKheima= locations.find { it.name == "ShubraElKheima" }




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


        val locroadelfarg = Location("")
        locroadelfarg.latitude = savedLocationRoadelfarg?.latitude ?: 0.0
        locroadelfarg.longitude = savedLocationRoadelfarg?.longitude ?: 0.0

        val locStTeresa = Location("")
        locStTeresa.latitude = savedLocationStTeresa?.latitude ?: 0.0
        locStTeresa.longitude = savedLocationStTeresa?.longitude ?: 0.0

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

        val distances = listOf(
            loc1.distanceTo(locElMounib),
            loc1.distanceTo(locSakiatMekky),
            loc1.distanceTo(locOmmElMasryeen),
            loc1.distanceTo(locElGiza),
            loc1.distanceTo(locFaisal),
            loc1.distanceTo(locCairoUniversity),
            loc1.distanceTo(locElBohoth),
            loc1.distanceTo(locDokki),
            loc1.distanceTo(locOpera),
            loc1.distanceTo(locSadat),
            loc1.distanceTo(locMohamedNaguib),
            loc1.distanceTo(locAttaba),
            loc1.distanceTo(locAlShohadaa),
            loc1.distanceTo(locMassra),
            loc1.distanceTo(locroadelfarg),
            loc1.distanceTo(locStTeresa),
            loc1.distanceTo(locMezallat),
            loc1.distanceTo(locKolleyyetElZeraa),
            loc1.distanceTo(locShubraElKheima)
        )


        val minDistanceIndex = distances.indexOf(distances.min()!!)

        if (minDistanceIndex != null){

            val cardView1 = findViewById<CardView>(R.id.desStation)
            cardView1.visibility = View.VISIBLE



            if (minDistanceIndex == 0) {
                foundStation.append("This location is close to ElMounib")
                found="El Mounib"
            } else if (minDistanceIndex == 1) {
                foundStation.append("This location is close to SakiatMekky")
                found = "Sakiat Mekky"
            } else if (minDistanceIndex == 2) {
                foundStation.append("This location is close to OmmElMasryeen")
                found = "Omm El Masryeen"
            } else if (minDistanceIndex == 3) {
                foundStation.append("This location is close to ElGiza")
                found = "ElGiza"
            }else if (minDistanceIndex == 4) {
                foundStation.append("This location is close to Faisal")
                found = "Faisal"

            }else if (minDistanceIndex == 5) {
                foundStation.append("This location is close to CairoUniversity")
                found = "Cairo University"

            }else if (minDistanceIndex == 6) {
                foundStation.append("This location is close to El Bohoth")
                found = "El Bohoth"

            }else if (minDistanceIndex == 7) {
                foundStation.append("This location is close to Dokki")
                found = "Dokki"

            }else if (minDistanceIndex == 8) {
                foundStation.append("This location is close to Opera")
                found = "Opera"

            }else if (minDistanceIndex == 9) {
                foundStation.append("This location is close to Sadat")
                found = "Sadat (IN LINE 2)"

            }else if (minDistanceIndex == 10) {
                foundStation.append("This location is close to MohamedNaguib")
                found = "Mohamed Naguib"

            }else if (minDistanceIndex == 11) {
                foundStation.append("This location is close to to Attaba")
                found = "Attaba"

            }else if (minDistanceIndex == 12) {
                foundStation.append("This location is close to AlShohadaa")
                found = "Al Shohadaa"

            }else if (minDistanceIndex == 13) {
                foundStation.append("This location is close to Massra")
                found = "Masarra"

            }else if (minDistanceIndex == 14) {
                foundStation.append("This location is close to Road ElFarg")
                found = "Road El Farag"

            }else if (minDistanceIndex == 15) {
                foundStation.append("This location is close to StTeresa")
                found = "St Teresa"

            }else if (minDistanceIndex == 16) {
                foundStation.append("This location is close to Khalafawy")
                found = "Khalafawy"

            }else if (minDistanceIndex == 17) {
                foundStation.append("This location is close to Mezallat")
                found = "Mezallat"

            }else if (minDistanceIndex == 18) {
                foundStation.append("This location is close to Kolleyyet El Zeraa")
                found = "Kolleyyet El Zeraa"

            }else if (minDistanceIndex == 19) {
                foundStation.append("This location is close to Shubra El Kheima")
                found = "Shubra El Kheima"

            }

            if (found != null){
                val file = getSharedPreferences("destinationStationFile", MODE_PRIVATE)
                val editor = file.edit()
                editor.putString("destinationStation", found)
                editor.apply()
            }


        }


        if (address != null){
            val cardView = findViewById<CardView>(R.id.desStation)
            cardView.visibility = View.VISIBLE

            viewLocation.setOnClickListener{


                val colestStation = locations[minDistanceIndex].name

                    address = "${textAddress.text.toString().trim()}"

                val uri = Uri.parse("http://maps.google.com/maps?dirflg=w&saddr=$address&daddr=$colestStation+station+Cairo+metro")
                val intent = Intent(Intent.ACTION_VIEW, uri)

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    val webIntentUri = Uri.parse("http://maps.google.com/maps?dirflg=w&saddr=$colestStation+station+cairo+metro&daddr=$address")
                    val webIntent = Intent(Intent.ACTION_VIEW, webIntentUri)
                    startActivity(webIntent)
                    Toast.makeText(this, "opening Google Maps", Toast.LENGTH_SHORT).show()
                }



            }

        }



    }

     fun getEnglish(): List<String> {
        return listOf("Please enter a location", "An invalid location","Check your internet connection")
    }

     fun getArabic(): List<String> {
        return listOf("الرجاء إدخال موقع","موقع غير صالح", "تحقق من اتصالك بالإنترنت")
    }

    fun showToasts(context: Context,language: String , index : Int){
        val toasts = when (language){
            "en" -> getEnglish().getOrNull(index)
            "ar" -> getArabic().getOrNull(index)
            else -> null
        }

        if (toasts != null){
            Toast.makeText(context, toasts, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed() {
        finish()
        super.onBackPressed()

    }




}