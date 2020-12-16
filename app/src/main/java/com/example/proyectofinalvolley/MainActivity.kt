package com.example.proyectofinalvolley

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    //Declaracion global
    var adaptadorPaises: PaisesAdapter? = null
    var listaPaises: ArrayList<Pais>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listaPaises = ArrayList<Pais>()

        adaptadorPaises = PaisesAdapter(listaPaises, this)
        miRecyclerCovid.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        miRecyclerCovid.adapter = adaptadorPaises

        val queue = Volley.newRequestQueue(this)
        val url = "https://wuhan-coronavirus-api.laeyoung.endpoint.ainize.ai/jhu-edu/latest"

        val peticionDatosCovid = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            for (index in 0 until response.length()) {
                try {
                    val paisJson = response.getJSONObject(index)
                    val nombrePais = paisJson.getString("countryregion")
                    val numeroConfirmados = paisJson.getInt("confirmed")
                    val numeroMuertos = paisJson.getInt("deaths")
                    val numeroRecuperados = paisJson.getInt("recovered")
                    val countryCodeJson = paisJson.getJSONObject("countrycode")
                    val codigoPais = countryCodeJson.getString("iso2")
                    //numeroConfirmados = DecimalFormat(numeroConfirmados)

                    //objeto de kotlin
                    val paisIndividual = Pais(
                        nombrePais,
                        numeroConfirmados,
                        numeroMuertos,
                        numeroRecuperados,
                        codigoPais
                    )
                    listaPaises.add(paisIndividual)
                } catch (e: JSONException) {
                    Log.wtf("JsonError", e.localizedMessage)
                }
            }

            listaPaises.sortByDescending { it.confirmados }
            adaptadorPaises!!.notifyDataSetChanged()

        },
            { error ->
                Log.e("error_volley", error.localizedMessage)
            })

        queue.add(peticionDatosCovid)
    }
}