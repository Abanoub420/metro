package com.example.metrov6

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener

class MainActivity4 : AppCompatActivity() {

    val lineStartEnglish = mutableListOf(
        "Helwan",
        "Ain Helwan",
        "Helwan University",
        "Wadi Hof",
        "Hadayek Helwan",
        "El Maasara",
        "Tora El Asmant",
        "Kozzika",
        "Tora El Balad",
        "Sakanat El Maadi",
        "Maadi",
        "Hadayek El maadi",
        "Dar El Salam",
        "El Zahraa",
        "Mar Girgis",
        "El Malek El Saleh",
        "Al Sayeda Zeinab",
        "Saad Zaghloul",
        "Sadat",                     /// <  - sadat no 1
        "Nasser",                     /// < - nasser no 1
        "Orabi",
        "Al Shohadaa",               /// < - Al shohadaa no 1
        "Ghamra",
        "El Demerdash",
        "Manshiet El Sadr",
        "Kobri El Qobba",
        "Hammamat El Qobba",
        "Saray El Qobba",
        "Hadayeq El Zaitoun",
        "Helmeyet El Zaitoun",
        "El Matareyya",
        "Ain Shams",
        "Ezbet El Nakhl",
        "El Marg",
        "New El Marg",                ////////////////// /// < - end of line 1
        "El Mounib",
        "Sakiat Mekky",
        "Omm El Masryeen",
        "El Giza",
        "Faisal",
//        "Cairo University",                /// < - cu no 1
        "El Bohoth",
        "Dokki",
        "Opera",
//        "Sadat",                             /// < - sadat no 2
        "Mohamed Naguib",
        "Attaba",                           //// < - attaba no 1
//        "Al shohadaa",                       /// < - Al shohadaa no 2
        "Masarra",
        "Road El Farag",
        "St Teresa",
        "Khalafawy",
        "Mezallat",
        "Kolleyyet El Zeraa",
        "Shubra El Kheima",           //////   < - end of line 2
        "Airport",
        "Ahmed Galal",
        "Adly Mansour",
        "El Haykestep",
        "Omar Ibn El Khattab",
        "Qobaa",
        "Hesham Barakat",
        "El Nozha",
        "Nadi El Shams",
        "Alf Maskan",
        "Heliopolis Square",
        "Haroun",
        "Al-Ahram",
        "Koleyet El Banat",
        "Stadium",
        "Fair Zone",
        "Abbassia",
        "Abdou Pasha",
        "El Geish",
        "Bab El Shaaria",
//        "Attaba",                        /// <- attaba no 2
//        "Nasser",                         ///// < - nasser no 2
        "Maspero",
        "Safaa Hegazy",
        "Kit Kat",
        "Sudan Street",
        "Imbaba",
        "El Bohy",
        "Al Qawmeya Al Arabiya",
        "Ring Road",
        "Rod Al Farag Axis",
        "El Tawfikeya",
        "Wadi El Nil",
        "Gamaat El Dowal Al Arabiya",
        "Bulaq El Dakroor",
        "Cairo University"                         //// <- cu no 2

    )

    val lineStartArabic = mutableListOf(
        "حلوان",
        "عين حلوان",
        "جامعة حلوان",
        "وادي حوف",
        "حدائق حلوان",
        "المعصرة",
        "طرة الأسمنت",
        "كوتسيكا",
        "طرة البلد",
        "ثكنات المعادي",
        "المعادي",
        "حدائق المعادي",
        "دار السلام",
        "الزهراء",
        "مار جرجس",
        "الملك الصالح",
        "السيدة زينب",
        "سعد زغلول",
        "السادات",                     /// <  - sadat no 1
        "جمال عبدالناصر",                     /// < - nasser no 1
        "عرابي",
        "الشهداء",               /// < - Al shohadaa no 1
        "غمرة",
        "الدمرداش",
        "منشية الصدر",
        "كوبري القبة",
        "حمامات القبة",
        "ساراي القبة",
        "حدائق الزيتون",
        "حلمية الزيتون",
        "المطرية",
        "عين شمس",
        "عزبة النخل",
        "المرج",
        "المرج الجديدة",                ////////////////// /// < - end of line 1
        "المنيب",
        "ساقية مكي",
        "أم المصريين",
        "الجيزة",
        "فيصل",
//        "Cairo University",                /// < - cu no 1
        "البحوث",
        "الدقي",
        "الأوبرا",
//        "Sadat",                             /// < - sadat no 2
        "محمد نجيب",
        "العتبة",                           //// < - attaba no 1
//        "Al shohadaa",                       /// < - Al shohadaa no 2
        "مسرة",
        "روض الفرج",
        "سانتا تريزا",
        "الخلفاوي",
        "المظلات",
        "كلية الزراعة",
        "شبرا الخيمة",           //////   < - end of line 2
        "المطار",
        "أحمد جلال",
        "عدلي منصور",
        "الهايكستب",
        "عمر بن الخطاب",
        "قباء",
        "هشام بركات",
        "النزهة",
        "نادي الشمس",
        "ألف مسكن",
        "ميدان هليوبوليس",
        "هارون",
        "الأهرام",
        "كلية البنات",
        "الإستاد",
        "أرض المعارض",
        "العباسية",
        "عبده باشا",
        "الجيش",
        "باب الشعرية",
//        "Attaba",                        /// <- attaba no 2
//        "Nasser",                         ///// < - nasser no 2
        "ماسبيرو",
        "صفاء حجازي",
        "الكيت كات",
        "السودان",
        "إمبابة",
        "البوهي",
        "القومية العربية",
        "الطريق الدائري",
        "محور روض الفرج",
        "التوفيقية",
        "وادي النيل",
        "جامعة الدول العربية",
        "بولاق الدكرور",
        "جامعة القاهرة"                         //// <- cu no 2
    )

    val lineEnd = mutableListOf(
        "Helwan",
        "Ain Helwan",
        "Helwan University",
        "Wadi Hof",
        "Hadayek Helwan",
        "El Maasara",
        "Tora El Asmant",
        "Kozzika",
        "Tora El Balad",
        "Sakanat El Maadi",
        "Maadi",
        "Hadayek El maadi",
        "Dar El Salam",
        "El Zahraa",
        "Mar Girgis",
        "El Malek El Saleh",
        "Al Sayeda Zeinab",
        "Saad Zaghloul",
        "Sadat",                     /// <  - sadat no 1
        "Nasser",                     /// < - nasser no 1
        "Orabi",
        "Al Shohadaa",               /// < - Al shohadaa no 1
        "Ghamra",
        "El Demerdash",
        "Manshiet El Sadr",
        "Kobri El Qobba",
        "Hammamat El Qobba",
        "Saray El Qobba",
        "Hadayeq El Zaitoun",
        "Helmeyet El Zaitoun",
        "El Matareyya",
        "Ain Shams",
        "Ezbet El Nakhl",
        "El Marg",
        "New El Marg",                ////////////////// /// < - end of line 1
        "El Mounib",
        "Sakiat Mekky",
        "Omm El Masryeen",
        "El Giza",
        "Faisal",
//        "Cairo University",                /// < - cu no 1
        "El Bohoth",
        "Dokki",
        "Opera",
//        "Sadat",                             /// < - sadat no 2
        "Mohamed Naguib",
        "Attaba",                           //// < - attaba no 1
//        "Al shohadaa",                       /// < - Al shohadaa no 2
        "Masarra",
        "Road El Farag",
        "St Teresa",
        "Khalafawy",
        "Mezallat",
        "Kolleyyet El Zeraa",
        "Shubra El Kheima",           ///////////////////////   < - end of line 2
        "Airport",
        "Ahmed Galal",
        "Adly Mansour",
        "El Haykestep",
        "Omar Ibn El Khattab",
        "Qobaa",
        "Hesham Barakat",
        "El Nozha",
        "Nadi El Shams",
        "Alf Maskan",
        "Heliopolis Square",
        "Haroun",
        "Koleyet El Banat",
        "Stadium",
        "Fair Zone",
        "Abbassia",
        "Abdou Pasha",
        "El Geish",
        "Bab El Shaaria",
//        "Attaba",                        /// <- attaba no 2
//        "Nasser",                         ///// < - nasser no 2
        "Maspero",
        "Safaa Hegazy",
        "Kit Kat",
        "Sudan Street",
        "Imbaba",
        "El Bohy",
        "Al Qawmeya Al Arabiya",
        "Ring Road",
        "Rod Al Farag Axis",
        "El Tawfikeya",
        "Wadi El Nil",
        "Gamaat El Dowal Al Arabiya",
        "Bulaq El Dakroor",
        "Cairo University"                         //// <- cu no 2

    )


//    lateinit var autoLocation: AutoCompleteTextView
    lateinit var autoDestination: AutoCompleteTextView

    lateinit var autoLocation: TextView




    //    lateinit var startSpinner : Spinner
//    lateinit var endSpinner : Spinner
    lateinit var btnPath: Button

    lateinit var btnSwitch : ImageView

    lateinit var prefsLang: SharedPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        prefsLang = getSharedPreferences("saved", MODE_PRIVATE)

        autoLocation = findViewById(R.id.autoLocation)
        autoDestination = findViewById(R.id.autoDestination)



//        startSpinner = findViewById(R.id.startSpinner)
//        endSpinner = findViewById(R.id.endSpinner)
        btnPath = findViewById(R.id.btnPath)

        btnSwitch = findViewById(R.id.switchStations)


        val clearAutoLocation: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_clear_button)
        val clearAutoDestination: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_clear_button)

//        val spinnerStart = TextSpinnerAdapter(this , lineStart)
//        startSpinner.adapter = spinnerStart
//
//        val spinnerEnd = ArrayAdapter(this , android.R.layout.simple_dropdown_item_1line , lineEnd)
//        endSpinner.adapter = spinnerEnd



        val clearButtonVisibleThreshold = 1
        autoLocation.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        autoDestination.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)



//       clearAutoLocation?.let { drawable ->
//           autoLocation.setOnTouchListener { _, event ->
//               if (event.action == MotionEvent.ACTION_UP) {
//                   val clearBounds = drawable.bounds
//                   if (event.rawX >= autoLocation.right - clearBounds.width()) {
//                       autoLocation.text = null
//                       return@setOnTouchListener true
//                   }
//               }
//               false
//           }
//
//        }

     clearAutoDestination?.let { drawable ->
         autoDestination.setOnTouchListener{ _, event ->
             if (event.action == MotionEvent.ACTION_UP){
                 val  clearBounds = drawable.bounds
                 if (event.rawX >= autoDestination.right - clearBounds.width()){
                     autoDestination.text= null
                     return@setOnTouchListener true
                 }

             }
             false
         }

     }

        val prefs = getSharedPreferences("stationNamePrefs", MODE_PRIVATE)
        val location = prefs.getString("stationName", "")


        if (!location.isNullOrEmpty()) {
            val builder = Builder(this)
            builder.setTitle("Set your startpoint")
            builder.setMessage("The app senses your possible location is '$location' , Do you want to set it as your startpoint?")
//                .setCancelable(false)
//                .setPositiveButton("OK") { _, _ -> }

            builder.setPositiveButton("OK"){
                                           dialogInterface ,which ->   autoLocation.setText("$location")
                dialogInterface.cancel()

            }
            builder.setNegativeButton("Cancel") {
                dialogInterface ,which ->
                dialogInterface.cancel()

            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        val file = getSharedPreferences("destinationStationFile", MODE_PRIVATE)
        val destination = file.getString("destinationStation", "")

        if (!destination.isNullOrEmpty()){
            val builder = Builder(this)
            builder.setTitle("Set your endpoint")
            builder.setMessage("You have been searched for '$destination' ,Do you want to set it as your endpoint?")
            builder.setPositiveButton("OK"){
                dialogInterface,which -> autoDestination.setText("$destination")
                dialogInterface.cancel()
            }
            builder.setNegativeButton("Cancel"){
                dialogInterface,which ->
                dialogInterface.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        var start = ""
        var end = ""

        val language = prefsLang.getString("langcode", null)

        if (language == "en") {
            autoLocation = findViewById(R.id.autoLocation)
            val arrowDown: Drawable? =
                ContextCompat.getDrawable(this@MainActivity4, R.drawable.arrow_drop_down_40)
            autoLocation.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null)
        }else{
            val arrowDown: Drawable? =
                ContextCompat.getDrawable(this@MainActivity4, R.drawable.arrow_drop_down_40)
            autoLocation.setCompoundDrawablesWithIntrinsicBounds(arrowDown, null, null, null)
        }

        autoLocation.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.your_custom_layout)

            dialog.window?.setLayout(800,1500)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val editTextSpinner : EditText = dialog.findViewById(R.id.editID)
            val listViewSpinner : ListView = dialog.findViewById(R.id.listviewmain4ID)

            val adStart = if (language == "en") {
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, lineStartEnglish)
            }else{
                ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, lineStartArabic)
            }
           listViewSpinner.setAdapter(adStart)

            editTextSpinner.addTextChangedListener (object  :TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, startInt: Int, count: Int, after: Int) {

                }

                override fun onTextChanged( s: CharSequence?, startInt: Int, before: Int, count: Int) {
                    adStart.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            listViewSpinner.setOnItemClickListener { parent, view, position, id ->
                autoLocation.setText(adStart.getItem(position))
                dialog.dismiss()
            }


        }

        autoLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, startInt: Int, before: Int, count: Int) {
                val selected = autoLocation.text.toString()
                start = selected
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

//        autoLocation.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(edit: Editable?) {
//
//                if ((edit?.length ?: 0) >= clearButtonVisibleThreshold) {
//                    autoLocation.setCompoundDrawablesWithIntrinsicBounds(null, null, clearAutoLocation, null)
//                } else {
//                    autoLocation.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
//                }
//
//
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                val selectedStart = autoLocation.text.toString()
//                start = selectedStart
//
//
//            }
//
//
//        })

        val adEnd = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, lineEnd)
        autoDestination.setAdapter(adEnd)

        autoDestination.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if ((p0?.length ?: 0) >= clearButtonVisibleThreshold) {
                    autoDestination.setCompoundDrawablesWithIntrinsicBounds(null, null, clearAutoDestination, null)
                } else {
                    autoDestination.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val selectedEnd = autoDestination.text.toString()
                end = selectedEnd


            }
        })

//        btnSwitch.setOnClickListener {
//            textViewValues()
//
//        }

        btnPath.setOnClickListener{

            if (autoLocation.text.toString().isEmpty() && autoDestination.text.toString().isEmpty()){
                val builder = Builder(this)
                builder.setTitle("ERROR")
                builder.setMessage("PLZ YOU SHOULD Enter A StartPoint And A EndPoint \uD83E\uDD7A \uD83E\uDD7A PLZ")
                builder.setPositiveButton("OK"){
                        dialogInterface,which ->
                    dialogInterface.cancel()
                }
                val alertDialog = builder.create()
                alertDialog.show()
                return@setOnClickListener
            }

            if (autoLocation.text.toString().isEmpty()){
                val builder = Builder(this)
                builder.setTitle("ERROR")
                builder.setMessage("NOW YOU SHOULD Enter A Startpoint \uD83D\uDE12")
                builder.setPositiveButton("OK"){
                        dialogInterface,which ->
                    dialogInterface.cancel()
                }
                val alertDialog = builder.create()
                alertDialog.show()
                return@setOnClickListener
            }
            if (autoDestination.text.toString().isEmpty()){
                val builder = Builder(this)
                builder.setTitle("ERROR")
                builder.setMessage("NOW YOU SHOULD Enter A Endpoint \uD83D\uDE12")
                builder.setPositiveButton("OK"){
                        dialogInterface,which ->
                    dialogInterface.cancel()
                }
                val alertDialog = builder.create()
                alertDialog.show()
                return@setOnClickListener
            }

            if (autoLocation.text.toString().isNotEmpty() && autoDestination.text.toString().isNotEmpty()) {
                if (start == end) {
                    val builder = Builder(this)
                    builder.setTitle("ERROR 404 \uD83D\uDC80\uD83D\uDC80\uD83D\uDC80")
                    builder.setMessage("YOU HAVE BEEN ENTERED THE SAME STATION \uD83E\uDD2C")
                    builder.setPositiveButton("OK"){
                            dialogInterface,which ->
                        dialogInterface.cancel()
                    }
                    val alertDialog = builder.create()
                    alertDialog.show()
                    return@setOnClickListener


                }
            }

              val startValue = lineStartEnglish.find { it == start }
              val endValue = lineStartEnglish.find { it == end }

            if (autoLocation.text.toString().isNotEmpty() && autoDestination.text.toString().isNotEmpty() && startValue.isNullOrEmpty() && endValue.isNullOrEmpty()) {
                val builder = Builder(this)
                builder.setTitle("ERROR \uD83D\uDE31\uD83D\uDE31")
                builder.setMessage("CAN NOT FIND ANY OF THESE STARIONS")
                builder.setPositiveButton("OK"){
                        dialogInterface,which ->
                    dialogInterface.cancel()
                }
                val alertDialog = builder.create()
                alertDialog.show()
                return@setOnClickListener
            }
            if (autoLocation.text.toString().isNotEmpty() && autoDestination.text.toString().isNotEmpty() && startValue.isNullOrEmpty() && !endValue.isNullOrEmpty()) {
                val builder = Builder(this)
                builder.setTitle("ERROR ")
                builder.setMessage("CAN NOT FIND THE STARTPOINT STATION YOU HAVE BEEN ENTERED\uD83D\uDE33\uD83D\uDE33")
                builder.setPositiveButton("OK"){
                        dialogInterface,which ->
                    dialogInterface.cancel()
                }
                val alertDialog = builder.create()
                alertDialog.show()
                return@setOnClickListener
            }
            if (autoLocation.text.toString().isNotEmpty() && autoDestination.text.toString().isNotEmpty() && !startValue.isNullOrEmpty() && endValue.isNullOrEmpty()) {
                val builder = Builder(this)
                builder.setTitle("ERROR ")
                builder.setMessage("CAN NOT FIND THE ENDPOINT STATION YOU HAVE BEEN ENTERED\uD83D\uDE33\uD83D\uDE33")
                builder.setPositiveButton("OK"){
                        dialogInterface,which ->
                    dialogInterface.cancel()
                }
                val alertDialog = builder.create()
                alertDialog.show()
                return@setOnClickListener
            }
            if (autoLocation.text.toString().isNotEmpty() && autoDestination.text.toString().isNotEmpty() && !startValue.isNullOrEmpty() && !endValue.isNullOrEmpty()) {
                val intent = Intent(this@MainActivity4, MainActivity5::class.java)
                    intent.putExtra("stationNameStart", start)
                    intent.putExtra("stationNameEnd", end)
                    startActivity(intent)
            }



        }

    }

//     fun textViewValues() {
//         val temporary = autoLocation.text.toString()
//         autoLocation.setText(autoDestination.text, false)
//         autoDestination.setText(temporary,false)
//    }


}