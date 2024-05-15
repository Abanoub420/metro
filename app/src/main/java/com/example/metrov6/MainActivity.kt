package com.example.metrov6

import com.example.metrov6.R
import android.app.AlertDialog.Builder
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.widget.NestedScrollView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Circle
import com.github.ybq.android.spinkit.style.DoubleBounce
import mumayank.com.airlocationlibrary.AirLocation
import java.util.*


class MainActivity : AppCompatActivity() {


    lateinit var airLocation: AirLocation
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var nestedScrollView: NestedScrollView

//    private lateinit var nearButton : Button

    private lateinit var prefs: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /// gets a language code from main6 in onCreate once i opened the app
        prefs = getSharedPreferences("saved", MODE_PRIVATE)
        val language = prefs.getString("langcode", null)
        if (language != null) {
            setLanguage(this, language)
        }
        setContentView(R.layout.activity_main)


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        nestedScrollView = findViewById(R.id.nestedScrollView)


        var isLoadActivityStarted = false

        val nearButton = findViewById<LinearLayout>(R.id.nearByMe)
        nearButton.setOnClickListener {
            if (!isLoadActivityStarted) {
                isLoadActivityStarted = true

                Handler().postDelayed({
                    if (!checkConnection(this)) {
                        if (language != null) {
                            specificToast(this, language, 0)
                        }
                    } else {
                        val intent = Intent(this, load_activity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }, 2000)
            }
        }


        swipeRefreshLayout.setOnRefreshListener {
            // Perform your refresh operation here

            // Simulate a delay for demonstration purposes

//            val cardView = findViewById<CardView>(R.id.routeCard)
//            cardView.visibility = View.GONE


            if (!checkConnection(this)) {
                if (language != null) {
                    specificToast(this, language, 0)
                }
            }

            val open = findViewById<TextView>(R.id.openmetro)
            setTextVisibility(open)

            val closed = findViewById<TextView>(R.id.closemetro)
            closed.visibility = if (open.visibility == View.VISIBLE) View.GONE else View.VISIBLE

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


        val open = findViewById<TextView>(R.id.openmetro)
        setTextVisibility(open)

        val closed = findViewById<TextView>(R.id.closemetro)
        closed.visibility = if (open.visibility == View.VISIBLE) View.GONE else View.VISIBLE

    }



    fun setLanguage(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

    }


    fun specificToast(context: Context, language: String, indexToast: Int) {
        val toastsM = when (language) {
            "en" -> getEnglish().getOrNull(indexToast)
            "ar" -> getArabic().getOrNull(indexToast)
            else -> null

        }
        if (toastsM != null) {
            Toast.makeText(context, toastsM, Toast.LENGTH_SHORT).show()
        }
    }

    fun dialogueView(context: Context, language: String, indexToast: Int): String? {
        val dialogText = when (language) {
            "en" -> getEnglish().getOrNull(indexToast)
            "ar" -> getArabic().getOrNull(indexToast)
            else -> null

        }
        return dialogText
    }


    fun getEnglish(): List<String> {
        return listOf(
            "no internet",
            "Confirm Exit",
            "Are you sure you want to exit the app?",
            "YES",
            "NO"
        )
    }

    fun getArabic(): List<String> {
        return listOf(
            "لا يوجد اتصال بالانترنت",
            "تأكيد الخروج",
            "هل أنت متأكد أنك تريد الخروج من التطبيق؟",
            "نعم",
            "لا"
        )
    }


    fun checkConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return when {
                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> {
                    // Connected via Wi-Fi
//                    showToast(context, "Good internet connection (Wi-Fi)")
                    true
                }

                networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> {
                    // Connected via cellular network
//                    showToast(context, "Good internet connection (Cellular)")
                    true
                }

                else -> {
                    // Not connected to a valid network
                    false
                }
            }
        } else {
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


    fun setTextVisibility(textView: TextView) {
        val current = Calendar.getInstance()
//        val currentHour = current.get(Calendar.HOUR_OF_DAY)
//         val currentMinute = current.get(Calendar.MINUTE)
//        if (currentHour >= 5) {
//            textView.visibility = View.VISIBLE
//        }else{
//            textView.visibility=View.GONE
//        }
        val start = Calendar.getInstance()
        start.set(Calendar.HOUR_OF_DAY, 5)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.SECOND, 0)

        val end = Calendar.getInstance()
        end.set(Calendar.HOUR_OF_DAY, 1)
        end.set(Calendar.MINUTE, 0)
        end.set(Calendar.SECOND, 0)

//         end.add(Calendar.DAY_OF_MONTH, 1)

        if (current.after(start) || current.before(end)) {
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }

    }


    fun stationNearDestination(view: View) {
        val intent = Intent(this, MainActivity3::class.java)
        startActivity(intent)

    }

    fun detailsTrip(view: View) {
        val intent = Intent(this, MainActivity4::class.java)
        startActivity(intent)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        airLocation.onActivityResult(requestCode, resultCode, data) // ADD THIS LINE INSIDE onActivityResult
//    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//       airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults) // ADD THIS LINE INSIDE onRequestPermissionResult
//    }

//    override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
//        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
//     }

//    override fun onSuccess(locations: ArrayList<Location>) {
//
//
//
//        if (!checkConnection(this)) {
//            showToast(this, "Check your internet connection")
//            return
//        }
//

//        val lat=locations[0].latitude
//        val lon=locations[0].longitude
//
////        val a=Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${locations[0].latitude},${locations[0].longitude}"))
////        startActivity(a)
//
//        val coder= Geocoder(this)//->lat,lon ->address
//
//            val addresses = coder.getFromLocation(lat, lon, 1)
//            val address = addresses?.get(0)?.getAddressLine(0)
//            if (address != null) {
//                val intent = Intent(this, MainActivity2::class.java)
//                intent.putExtra("ADDRESS", address)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
////            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            finish()
//            }


//    }


    override fun onBackPressed() {
        val language = prefs.getString("langcode", null)
        val builder = Builder(this)
        builder.setTitle(language?.let { dialogueView(this, it, 1) })
        builder.setMessage(language?.let { dialogueView(this, it, 2) })
        builder.setPositiveButton(language?.let {
            dialogueView(
                this,
                it,
                3
            )
        }) { dialogInterface, _ ->
            dialogInterface.cancel()
            finish()
            super.onBackPressed()
        }
        builder.setNegativeButton(language?.let {
            dialogueView(
                this,
                it,
                4
            )
        }) { dialogInterface, _ ->
            dialogInterface.cancel()
        }

        val dialog = builder.create()
        dialog.show()


    }

    fun settingsFun(view: View) {
        val intent = Intent(this, MainActivity6::class.java)
        startActivity(intent)
    }

//    private fun setSplashy() {
//       Splashy(this)
//            .setLogo(R.drawable.splashy)
//            .setAnimation(Splashy.Animation.GROW_LOGO_FROM_CENTER)
//            .setBackgroundResource(R.color.black)
//            .setTitleColor(R.color.white)
//            .setProgressColor(R.color.white)
//            .setTitle(R.string.splashy)
//            .setSubTitle(R.string.splash_screen_made_easy)
//            .setFullScreen(true)
//            .setSubTitleFontStyle("fonts/satisfy_regular.ttf")
//            .setClickToHide(true)
//            .setDuration(5000)
//            .show()
//
//        Splashy.onComplete(object : Splashy.OnComplete {
//            override fun onComplete() {
//                Toast.makeText(this@MainActivity, "Welcome", Toast.LENGTH_SHORT).show()
//            }
//
//        })
//    }
//
//    public fun showSplash(v: View) {
//        setSplashy()
//        // Hides after 1sec
//        Handler().postDelayed({
//            Splashy.hide()
//        }, 1000)
//    }
//



}





