package com.proway.whereisthesunapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import org.w3c.dom.Text
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val city: String = "blumenau,br"
    val api: String = "246d9ab9438614fec5f7e902322dbf84"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }
    inner class weatherTask(): AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errortext).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response:String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=Blumenau, Br&units=metric&appid=246d9ab9438614fec5f7e902322dbf84")
            //    response = URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$api")
                    .readText(Charsets.UTF_8)
            }
            catch (e : Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try{
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAT:Long = jsonObj.getLong("dt")
                val updatedAtText = "Atualizado em: "+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updatedAT*1000)
                )
                val temp = main.getString("temp")+"ºC"
                val tempMin = "Min Temp: "+main.getString("temp_min")+"ºC"
                val tempMax = "Max Temp: "+main.getString("temp_max")+"ºC"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescripition = weather.getString("descripition")
                val address = jsonObj.getString("name")+", "+sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescripition.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            }
            catch (e :  Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
            }
        }
    }
}