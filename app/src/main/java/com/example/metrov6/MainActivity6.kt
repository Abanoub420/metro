package com.example.metrov6

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity6 : AppCompatActivity() {

    lateinit var switchSound: Switch

    lateinit var switchFile: SharedPreferences
    lateinit var spinner: Spinner



    lateinit var firstASound: TextView
    lateinit var firstESound: TextView

    lateinit var secondALang: TextView
    lateinit var secondELang: TextView


    val languages: List<String> = listOf("English", "عربى", "French")

    private lateinit var prefs: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)


        spinner = findViewById(R.id.lang)
        switchSound = findViewById(R.id.switchSound)

        firstASound = findViewById(R.id.firstASound)
        firstESound = findViewById(R.id.firstESound)
        secondALang = findViewById(R.id.secondALang)
        secondELang = findViewById(R.id.secondELang)

        val inflater = LayoutInflater.from(this)
        val currentLocale = resources.configuration.locale
        val currentLanguage = currentLocale.language

        if (currentLanguage == "ar") {
            val arabicSound = inflater.inflate(R.layout.setting_text_arabic, null)
            arabicSound.visibility = View.VISIBLE
            firstASound.text = arabicSound.findViewById<TextView>(R.id.firstASound).text

            val arabicLang = inflater.inflate(R.layout.settings_language_arabic, null)
            arabicLang.visibility =View.VISIBLE
            secondALang.text = arabicLang.findViewById<TextView>(R.id.secondALang).text



//           setContentView(R.layout.setting_text_arabic)
            val englishSound = inflater.inflate(R.layout.setting_text_english, null)
            firstESound.text = englishSound.findViewById<TextView>(R.id.firstESound).text
            firstESound.visibility = View.GONE

            val englsihLang =  inflater.inflate(R.layout.setting_language_english,null)
            secondELang.text = englsihLang.findViewById<TextView>(R.id.secondELang).text
            secondELang.visibility = View.GONE
        }
        if (currentLanguage == "en"){
            val englishSound = inflater.inflate(R.layout.setting_text_english, null)
            englishSound.visibility = View.VISIBLE
            firstESound.text = englishSound.findViewById<TextView>(R.id.firstESound).text

            val englsihLang =  inflater.inflate(R.layout.setting_language_english,null)
            englsihLang.visibility = View.VISIBLE
            secondELang.text = englsihLang.findViewById<TextView>(R.id.secondELang).text

            val arabicLang = inflater.inflate(R.layout.settings_language_arabic, null)
            secondALang.text = arabicLang.findViewById<TextView>(R.id.secondALang).text
            secondALang.visibility = View.GONE

//           setContentView(R.layout.setting_text_english)
            val arabicSound = inflater.inflate(R.layout.setting_text_arabic, null)
            firstASound.text = arabicSound.findViewById<TextView>(R.id.firstASound).text
           firstASound.visibility = View.GONE
        }


        prefs = getSharedPreferences("saved", MODE_PRIVATE)


//        switchSound.isChecked = false
        switchFile = PreferenceManager.getDefaultSharedPreferences(this)
        switchSound.isChecked = switchFile.getBoolean("savedBlack", false)

        val savedThumbColor = prefs.getInt("savedThumbColor", 0)
        val savedTrackColor = prefs.getInt("savedTrackColor", 0)
        if (savedThumbColor != 0 && savedTrackColor != 0) {
            switchSound.thumbTintList = ColorStateList.valueOf(savedThumbColor)
            switchSound.trackTintList = ColorStateList.valueOf(savedTrackColor)
        }

        switchSound.setOnCheckedChangeListener { _, saveState ->

            when {
                saveState -> {
                    soundOn()
                }

                else -> {
                    soundOff()
                }
            }
            switchFile.edit().putBoolean("savedBlack", saveState).apply()


        }


        val langSpinner = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, languages)
        spinner.adapter = langSpinner

        var isInitialSelection = true

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {

                val language: String? = when (position) {
                    0 -> "en"
                    1 -> "ar"
                    2 -> "fr"
                    else -> null
                }

                val selectedItem = parent?.getItemAtPosition(position).toString()

                val editor = prefs.edit()
                editor.putString("lastItem", selectedItem)
                editor.putString("langcode", language)
                editor.apply()

                if (!isInitialSelection && language != null) {
                    setLanguage(this@MainActivity6, language)
                }

                isInitialSelection = false


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val lastItem = prefs.getString("lastItem", null)
        if (lastItem != null) {
            val position = langSpinner.getPosition(lastItem)
            spinner.setSelection(position)
        }

    }


    fun setLanguage(context: Context, language: String) {

        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

//            MainActivity.updateLocale(context, locale)

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)


    }

    fun toastsMassages(context: Context, language: String, indexToast : Int){
        val toastsM = when (language){
            "en" -> getEnglish().getOrNull(indexToast)
            "ar" -> getArabic().getOrNull(indexToast)
            else -> null

        }
        if (toastsM != null) {
                Toast.makeText(context, toastsM, Toast.LENGTH_SHORT).show()
        }
    }

    fun getEnglish(): List<String> {
        return listOf("Sound on","Sound Off")
    }

    fun getArabic(): List<String> {
        return listOf("صوت مفعل","صوت مقفل")
    }


    @RequiresApi(Build.VERSION_CODES.M)
     fun soundOn() {
        val language = prefs.getString("langcode", null)
        if (language != null) {
            toastsMassages(this,language ,0)
        }
        val blackValue = ContextCompat.getColor(this, R.color.switch_on_color)
        switchSound.thumbTintList = ColorStateList.valueOf(blackValue)
        switchSound.trackTintList = ColorStateList.valueOf(blackValue)
        val editor = prefs.edit()
        editor.putInt("savedThumbColor", blackValue)
        editor.putInt("savedTrackColor", blackValue)
        editor.apply()
    }



    @RequiresApi(Build.VERSION_CODES.M)
     fun soundOff() {
         val language = prefs.getString("langcode", null)
        if (language != null) {
            toastsMassages(this,language ,1)
        }
        val greyValue = ContextCompat.getColor(this, R.color.switch_thumb_off_color)
        switchSound.thumbTintList = ColorStateList.valueOf(greyValue)
        switchSound.trackTintList = ColorStateList.valueOf(greyValue)
        val editor = prefs.edit()
        editor.putInt("savedThumbColor", greyValue)
        editor.putInt("savedTrackColor", greyValue)
        editor.apply()
    }





}