package com.example.metrov6

import LineAdapter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity5 : AppCompatActivity() {

    val line1: List<String> = listOf(
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
        "Sadat",
        "Nasser",
        "Orabi",
        "Al Shohadaa",
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
        "New El Marg"
    )
    val line2: List<String> = listOf(
        "El Mounib",
        "Sakiat Mekky",
        "Omm El Masryeen",
        "El Giza",
        "Faisal",
        "Cairo University",
        "El Bohoth",
        "Dokki",
        "Opera",
        "Sadat",
        "Mohamed Naguib",
        "Attaba",
        "Al Shohadaa",
        "Masarra",
        "Road El Farag",
        "St Teresa",
        "Khalafawy",
        "Mezallat",
        "Kolleyyet El Zeraa",
        "Shubra El Kheima"
    )

    val line3: List<String> = listOf(
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
        "Attaba",
        "Nasser",
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
        "Cairo University"
    )

    lateinit var textView3: TextView
    lateinit var textView4: TextView
    lateinit var detialView: TextView
     lateinit var recyclerView1: RecyclerView
    lateinit var recyclerView2: RecyclerView

    lateinit var adapter1: LineAdapter
     lateinit var adapter2 : LineAdapter2

    var here = ""

    val stationList = mutableListOf<String>()
    val stationList2 = mutableListOf<String>()
    val stationList3 = mutableListOf<String>()
    val stationList4 = mutableListOf<String>()
    val stationList5 = mutableListOf<String>()
    val stationList6 = mutableListOf<String>()
    val stationList7 = mutableListOf<String>()
    val stationList8 = mutableListOf<String>()
    val stationList9 = mutableListOf<String>()
    val stationList10 = mutableListOf<String>()
    val stationList11 = mutableListOf<String>()
    val stationList12 = mutableListOf<String>()
    val stationList13 = mutableListOf<String>()
    val stationList14 = mutableListOf<String>()
    val stationList15 = mutableListOf<String>()
    val stationList16 = mutableListOf<String>()
    val stationList17 = mutableListOf<String>()
    val stationList18 = mutableListOf<String>()
    val stationList19 = mutableListOf<String>()
    val stationList20 = mutableListOf<String>()


    val numberOfStations = mutableListOf<Int>()
    val numberOfStations2 = mutableListOf<Int>()
    val numberOfStations3 = mutableListOf<Int>()
    val numberOfStations4 = mutableListOf<Int>()
    val numberOfStations5 = mutableListOf<Int>()
    val numberOfStations6 = mutableListOf<Int>()
    val numberOfStations7 = mutableListOf<Int>()
    val numberOfStations8 = mutableListOf<Int>()
    val numberOfStations9 = mutableListOf<Int>()
    val numberOfStations10 = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

//        textView3 = findViewById(R.id.textView3)
//        textView4 = findViewById(R.id.textView4)
        detialView = findViewById(R.id.detialView)


        val receiveDataStart = intent.getStringExtra("stationNameStart")
        val receiveDataEnd = intent.getStringExtra("stationNameEnd")

//           textView3.setText(receiveDataStart)
//        textView4.setText(receiveDataEnd)








        if (line1.contains(receiveDataStart) && line1.contains(receiveDataEnd)) {

            val startIndex = line1.indexOf(receiveDataStart)
            val endIndex = line1.indexOf(receiveDataEnd)


            val stations = if (startIndex > endIndex) {
                line1.subList(endIndex, startIndex + 1).reversed()
            } else {
                line1.subList(startIndex, endIndex + 1)

            }
            recyclerView1 = findViewById(R.id.stationRecyclerView1)
            recyclerView1.layoutManager = LinearLayoutManager(this)
            adapter1 = LineAdapter(stations)
            recyclerView1.adapter = adapter1

        }

        if (line2.contains(receiveDataStart) && line2.contains(receiveDataEnd)) {

            val startIndex = line2.indexOf(receiveDataStart)
            val endIndex = line2.indexOf(receiveDataEnd)


            val stations = if (startIndex > endIndex) {
                line2.subList(endIndex, startIndex + 1).reversed()
            } else {
                line2.subList(startIndex, endIndex + 1)

            }
            recyclerView1 = findViewById(R.id.stationRecyclerView1)
            recyclerView1.layoutManager = LinearLayoutManager(this)
            adapter1 = LineAdapter(stations)
            recyclerView1.adapter = adapter1

        }

        if (line3.contains(receiveDataStart) && line3.contains(receiveDataEnd)) {


            val startIndex = line3.indexOf(receiveDataStart)
            val endIndex = line3.indexOf(receiveDataEnd)


            val stations = if (startIndex > endIndex) {
                line3.subList(endIndex, startIndex + 1).reversed()
            } else {
                line3.subList(startIndex, endIndex + 1)

            }
            recyclerView1 = findViewById(R.id.stationRecyclerView1)
            recyclerView1.layoutManager = LinearLayoutManager(this)

            adapter1 = LineAdapter(stations)
            recyclerView1.adapter = adapter1


        }

/////////////////////////////////////////////////////////////////////////////////////////////////


        if (line1.contains(receiveDataStart) && line2.contains(receiveDataEnd)) {


            val startIndex = line1.indexOf(receiveDataStart)
            val endIndex = line2.indexOf(receiveDataEnd)

            if (startIndex > line1.indexOf("Sadat")) {
                val move1 = line1.slice(startIndex downTo line1.indexOf("Sadat"))
                stationList.addAll(move1)

            } else {
                val move2 = line1.slice(startIndex..line1.indexOf("Sadat"))
                stationList.addAll(move2)

            }
            if (line2.contains("Sadat")) {

                if (endIndex > line2.indexOf("Sadat")) {
                    val move3 = line2.slice(line2.indexOf("Sadat")..endIndex)
                    stationList2.addAll(move3)
                } else {
                    val move4 = line2.slice(endIndex..line2.indexOf("Sadat"))
                    stationList2.addAll(move4)
                }

            }
            val distanceinline1 = Math.abs(startIndex - line1.indexOf("Sadat"))
            numberOfStations.add(distanceinline1)
            val distanceinline2 = Math.abs(endIndex - line2.indexOf("Sadat"))
            numberOfStations.add(distanceinline2)
            val sumdistance1 = numberOfStations.sum()

            ///////////////////////////////////////////////////////////////////////////////////
            if (startIndex > line1.indexOf("Al Shohadaa")) {
                val move5 = line1.slice(startIndex downTo line1.indexOf("Al Shohadaa"))
                stationList3.addAll(move5)
            } else {
                val move6 = line1.slice(startIndex..line1.indexOf("Al Shohadaa"))
                stationList3.addAll(move6)
            }
            if (line2.contains("Al Shohadaa")) {
                if (endIndex > line2.indexOf("Al Shohadaa")) {
                    val move7 = line2.slice(line2.indexOf("Al Shohadaa")..endIndex)
                    stationList4.addAll(move7)
                } else {
                    val move8 = line2.slice(endIndex..line2.indexOf("Al Shohadaa"))
                    stationList4.addAll(move8)
                }
            }
            val distanceinline3 = Math.abs(startIndex - line1.indexOf("Al Shohadaa"))
            numberOfStations2.add(distanceinline3)
            val distanceinline4 = Math.abs(endIndex - line2.indexOf("Al Shohadaa"))
            numberOfStations2.add(distanceinline4)
            val sumdistance2 = numberOfStations2.sum()



            //////////////////////////////////////////////////////////////////////////////////////////////

            recyclerView1 = findViewById(R.id.stationRecyclerView1)
            recyclerView1.layoutManager = LinearLayoutManager(this)
            recyclerView2 = findViewById(R.id.stationRecyclerView2)
            recyclerView2.layoutManager = LinearLayoutManager(this)



            if (sumdistance1 < sumdistance2){
                adapter1 = LineAdapter(stationList)
                adapter2 = LineAdapter2(stationList2.reversed())
                here = "1.1"

            }

            if (sumdistance1 < sumdistance2 && endIndex < line2.indexOf("Sadat")){
                adapter1 = LineAdapter(stationList)
                adapter2 = LineAdapter2(stationList2.reversed())
                here = "1.1.2"

            }
            if (sumdistance1 < sumdistance2 && endIndex > line2.indexOf("Sadat")){
                adapter1 = LineAdapter(stationList)
                adapter2 = LineAdapter2(stationList2)
                here = "1.1.3"

            }

            if (sumdistance2 <= sumdistance1 ){
                adapter1 = LineAdapter(stationList3)
                adapter2 = LineAdapter2(stationList4.reversed())
                here = "2.2"

            }
            if (sumdistance2 < sumdistance1 && endIndex < line2.indexOf("Al Shohadaa")){
                adapter1 = LineAdapter(stationList3)
                adapter2 = LineAdapter2(stationList4.reversed())
                here = "2.2.1"
            }

            if (sumdistance2 <= sumdistance1 && endIndex > line2.indexOf("Al Shohadaa")){
                adapter1 = LineAdapter(stationList3)
                adapter2 = LineAdapter2(stationList4)
                here = "2.2.2"
            }
            recyclerView1.adapter = adapter1
            recyclerView2.adapter = adapter2

            detialView.setText("sumdistance1 $sumdistance1 , sumdistance2 $sumdistance2 , here $here \n")
            detialView.append("$stationList3 - $stationList4\n")





        }

        if (line2.contains(receiveDataStart) && line1.contains(receiveDataEnd)){


            val startIndex = line2.indexOf(receiveDataStart)
            val endIndex = line1.indexOf(receiveDataEnd)


            if (startIndex > line2.indexOf("Sadat")){
                val move9 = line2.slice(startIndex downTo line2.indexOf("Sadat"))
                stationList5.addAll(move9)
            }else{
                val move10 = line2.slice(startIndex..line2.indexOf("Sadat"))
                stationList5.addAll(move10)

            }
            if(line1.contains("Sadat")){
                if (endIndex > line1.indexOf("Sadat")){
                    val move11 = line1.slice(endIndex downTo line1.indexOf("Sadat"))
                    stationList6.addAll(move11)
                }else{
                    val move12 = line1.slice(endIndex..line1.indexOf("Sadat"))
                    stationList6.addAll(move12)
                }

            }
            val distanceinline7 = Math.abs(startIndex - line2.indexOf("Sadat"))
            numberOfStations3.add(distanceinline7)
            val distanceinline8 = Math.abs(endIndex - line1.indexOf("Sadat"))
            numberOfStations3.add(distanceinline8)
            val sumdistance3 = numberOfStations3.sum()


            /////////////////////////////////////////////////////////////////////////////////
            if (startIndex > line2.indexOf("Al Shohadaa")){
                val move13 = line2.slice(startIndex downTo  line2.indexOf("Al Shohadaa"))
                stationList7.addAll(move13)
            }else {
                val move14 = line2.slice(startIndex .. line2.indexOf("Al Shohadaa"))
                stationList7.addAll(move14)
            }
            if (line1.contains("Al Shohadaa")){
                if (endIndex > line1.indexOf("Al Shohadaa")){
                    val move15 = line1.slice(endIndex downTo line1.indexOf("Al Shohadaa"))
                    stationList8.addAll(move15)
                }else{
                    val move16 = line1.slice(endIndex..line1.indexOf("Al Shohadaa"))
                    stationList8.addAll(move16)
                }
            }
            val distanceinline9 = Math.abs(startIndex - line2.indexOf("Al Shohadaa"))
            numberOfStations4.add(distanceinline9)
            val distanceinline10 = Math.abs(endIndex - line1.indexOf("Al Shohadaa"))
            numberOfStations4.add(distanceinline10)
            val sumdistance4 = numberOfStations4.sum()
            /////////////////////////////////////////////////////////////////////////

            recyclerView1 = findViewById(R.id.stationRecyclerView1)
            recyclerView1.layoutManager = LinearLayoutManager(this)
            recyclerView2 = findViewById(R.id.stationRecyclerView2)
            recyclerView2.layoutManager = LinearLayoutManager(this)


            if (sumdistance3 < sumdistance4){
                adapter1 = LineAdapter(stationList5)
                adapter2 = LineAdapter2(stationList6.reversed())
                here = "3.1"

            }

            if (sumdistance3 < sumdistance4 && endIndex < line2.indexOf("Sadat")){
                adapter1 = LineAdapter(stationList5)
                adapter2 = LineAdapter2(stationList6.reversed())
                here = "3.2"

            }
            if (sumdistance3 < sumdistance4 && endIndex > line2.indexOf("Sadat")){
                adapter1 = LineAdapter(stationList5)
                adapter2 = LineAdapter2(stationList6.reversed())
                here = "3.3"

            }


            if (sumdistance4 <= sumdistance3 ){
                adapter1 = LineAdapter( stationList7)
                adapter2 = LineAdapter2(stationList8.reversed())
                here = "4.1"

            }
            if (sumdistance4 < sumdistance3 && endIndex < line2.indexOf("Al Shohadaa")){
                adapter1 = LineAdapter( stationList7)
                adapter2 = LineAdapter2(stationList8)
                here = "4.2"

            }

            if (sumdistance4 <= sumdistance3 && endIndex > line2.indexOf("Al Shohadaa")){
                adapter1 = LineAdapter( stationList7)
                adapter2 = LineAdapter2(stationList8.reversed())
                here = "4.3"

            }

            recyclerView1.adapter = adapter1
            recyclerView2.adapter = adapter2



            detialView.setText("sumdistance3 $sumdistance3 , sumdistance4 $sumdistance4 , here $here\n")
            detialView.append("$stationList7 - $stationList8\n")


        }






    }
}