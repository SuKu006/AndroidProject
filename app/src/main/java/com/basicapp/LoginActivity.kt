package com.basicapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.basicapp.api.APIClient
import com.basicapp.api.APIInterface
import com.basicapp.api.SearchResponse
import com.basicapp.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var activityLoginBinding: ActivityLoginBinding
    private val KEY_EMPTY = ""
    private var username: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding = DataBindingUtil.setContentView(
            this@LoginActivity, R.layout.activity_login)

        //Launch Registration screen when Register Button is clicked
        activityLoginBinding.btnLoginRegister.setOnClickListener {
            val i = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(i)
            finish()
        }

        activityLoginBinding.btnLogin.setOnClickListener { //Retrieve the data entered in the edit texts
            username =
                activityLoginBinding.etLoginUsername.text.toString().lowercase(Locale.getDefault())
                    .trim { it <= ' ' }
            password = activityLoginBinding.etLoginPassword.text.toString().trim { it <= ' ' }
            if (validateInputs()) {
                login()
            }
        }
    }

    private fun login() {
        val call: Call<SearchResponse> =
            APIClient.getClient().create(APIInterface::class.java)
                .callSearchApi(username)
        call.enqueue(object : Callback<SearchResponse?> {
            override fun onResponse(
                call: Call<SearchResponse?>,
                response: retrofit2.Response<SearchResponse?>
            ) {
                try {
                    activityLoginBinding.loader.visibility = View.GONE
                    if(response.body()!!.search.email.equals(username,true)) {
                        if(response.body()!!.search.password.equals(password)) {
                            loadDashboard()
                        } else  Toast.makeText(
                            this@LoginActivity,
                            "Password is not correct",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else  Toast.makeText(
                        this@LoginActivity,
                        "Unable to connect to server or host",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<SearchResponse?>, t: Throwable) {
                try {
                    activityLoginBinding.loader.visibility = View.GONE
                    Toast.makeText(
                        this@LoginActivity,
                        "Unable to connect to server or host",
                        Toast.LENGTH_LONG
                    ).show()
                    call.cancel()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }


    /**
     * Launch Dashboard Activity on Successful Login
     */
    private fun loadDashboard() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private fun validateInputs(): Boolean {
        if (KEY_EMPTY == username) {
            activityLoginBinding.etLoginUsername.error = "Username cannot be empty"
            activityLoginBinding.etLoginUsername.requestFocus()
            return false
        }
        if (KEY_EMPTY == password) {
            activityLoginBinding.etLoginPassword.error = "Password cannot be empty"
            activityLoginBinding.etLoginPassword.requestFocus()
            return false
        }
        return true
    }
}