package ua.ma.httprequests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var requestButto:Button = findViewById<Button>(R.id.Request)
        requestButto.setOnClickListener{
            request("https://api.openweathermap.org/data/2.5/weather?units=metric&q=Kropyvnytskyi&appid=d1c14c40806c571998d65cf2c1d8d818")
        }
    }

    private fun request(webAdress: String) {
        val handler = Handler(Looper.getMainLooper())
        var urlConnection:HttpURLConnection? = null
        thread {
            val url = URL(webAdress)
            urlConnection = url.openConnection() as? HttpURLConnection
            urlConnection?.let {
                val code = urlConnection?.responseCode
                if (code ==200){
                    val inputStreamReader = InputStreamReader(urlConnection?.inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    var line = ""
                    var tempLine = bufferedReader.readLine()
                    while ( tempLine != null){
                        line += tempLine
                        tempLine = bufferedReader.readLine()
                    }
                    handler.post{

                        val employee = Gson().fromJson(line, Employee::class.java)

                        val nameCity:TextView = findViewById(R.id.nameCity)

                        nameCity.text = employee.name;

                    }
                }
            }
            urlConnection?.disconnect()
        }
    }
}

data class Employee(val main:main, val wind:wind, val clouds:clouds, val name:String)

data class main(val temp:String, val feels_like:String, val pressure:String, val humidity:String) // температура, відчувається як, тиск, вологіст

data class wind(val speed:String, val gust:String) // Швидкість вітру, порив

data class clouds(val all:String) // хмарність