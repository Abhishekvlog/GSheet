package com.dexter.gsheet

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSubmit.setOnClickListener {
            addItemToSheet()
            etEmail.text.clear()
        }


    }

    private fun addItemToSheet() {
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setTitle("Progress Bar")
        progressDialog.setMessage("Please wait")
        progressDialog.show()
        email = etEmail.text.toString().trim()

        val stringRequest: StringRequest =
            object : StringRequest(
                Request.Method.POST, "https://script.google.com/macros/s/AKfycbwz16tF0HNabBBKHJVGgFDlWZHHN2edAH8fjDjKXdJe-PcIZEPvptTflq6YGH4Jsq6YHw/exec",
                Response.Listener { response ->
                    progressDialog.dismiss()
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                    print(response)

                },
                Response.ErrorListener { }
            ) {
                override fun getParams(): Map<String, String>? {
                    val parmas: MutableMap<String, String> = HashMap()

                    //here we pass params
                    parmas["action"] = "addItem"
                    parmas["EmailId"] = email
                    return parmas
                }
            }

        val socketTimeOut = 5000


        val retryPolicy: RetryPolicy =
            DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = retryPolicy

        val queue = Volley.newRequestQueue(this)

        queue.add(stringRequest)


    }
}