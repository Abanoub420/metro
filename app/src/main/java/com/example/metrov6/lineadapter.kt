import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.metrov6.R

class LineAdapter(var stations: List<String>) : RecyclerView.Adapter<LineAdapter.LineViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item3, parent, false)
        return LineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val station = stations[position]
        holder.stationTextView.text = station





//        holder.bind(station, position)

        holder.heynan(station, position)


    }

    override fun getItemCount(): Int {
        return stations.size
    }

    inner class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationTextView: TextView = itemView.findViewById(R.id.stationTextView)

        ////        val topPoint: View = itemView.findViewById(R.id.topPoint)
//        val bottomPoint: View = itemView.findViewById(R.id.bottomPoint)
//        val verticalLine: View = itemView.findViewById(R.id.verticalLine)
        val circleTop: View = itemView.findViewById(R.id.circleTop)
        val circleButtom : View = itemView.findViewById(R.id.circleButtom)
        val lineView :View = itemView.findViewById(R.id.lineView)

        val cirOnStation : View = itemView.findViewById(R.id.circleStationID)

        fun bind(station: String, position: Int) {

            stationTextView.text = station
//            if (position == 0 || position == itemCount - 1) {
//                stationTextView.setTypeface(null, Typeface.BOLD)
//            } else {
//                stationTextView.setTypeface(null, Typeface.NORMAL)
//            }


        }

        fun heynan(station: String, position: Int) {

            if (position == 0 ) {
                lineView.visibility = View.INVISIBLE


                circleButtom.visibility = View.INVISIBLE


               circleTop.visibility = View.VISIBLE
                cirOnStation.visibility = View.INVISIBLE


//                val circleTopLayoutParams = circleTop.layoutParams as ConstraintLayout.LayoutParams
//                circleTopLayoutParams.topToTop = R.id.verticalLine
//                circleTopLayoutParams.bottomToBottom = R.id.verticalLine
//                circleTop.layoutParams = circleTopLayoutParams

            }else{

                lineView.visibility = View.VISIBLE
//                 circleButtom.visibility=View.INVISIBLE
                circleTop.visibility = View.INVISIBLE
                circleButtom.visibility = View.INVISIBLE
                cirOnStation.visibility = View.VISIBLE


            }

            if (position == itemCount -1){
                lineView.visibility = View.INVISIBLE
                circleButtom.visibility =View.VISIBLE
                circleTop.visibility = View.INVISIBLE
                cirOnStation.visibility = View.INVISIBLE


            }



//            circle.visibility = if (position == 0 || position== stations.size - 1) View.VISIBLE else View.INVISIBLE
//            topPoint.visibility = if (position == 0) View.VISIBLE else View.INVISIBLE
//            bottomPoint.visibility = if (position == stations.size - 1) View.VISIBLE else View.INVISIBLE
//            verticalLine.visibility = if (position > 0 && position < stations.size - 1) View.VISIBLE else View.INVISIBLE        }
        }
    }
}