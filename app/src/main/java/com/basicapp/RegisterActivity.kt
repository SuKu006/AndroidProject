package com.basicapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.basicapp.api.APIClient
import com.basicapp.api.APIInterface
import com.basicapp.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class RegisterActivity : AppCompatActivity() {
    private val EMPTY = ""
    private var username: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null
    private var fullName: String? = null
    private var email: String? = null
    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityRegisterBinding = DataBindingUtil.setContentView(
            this@RegisterActivity, R.layout.activity_register)

        //Launch Login screen when Login Button is clicked
        activityRegisterBinding.btnRegisterLogin.setOnClickListener {
            val i = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        activityRegisterBinding.btnRegister.setOnClickListener { //Retrieve the data entered in the edit texts
            username = activityRegisterBinding.etUsername.text.toString().lowercase(Locale.getDefault())
                    .trim { it <= ' ' }
            password = activityRegisterBinding.etPassword.text.toString().trim { it <= ' ' }
            confirmPassword = activityRegisterBinding.etConfirmPassword.text.toString().trim { it <= ' ' }
            fullName = activityRegisterBinding.etFullName.text.toString().trim { it <= ' ' }
            email = activityRegisterBinding.etEmailId.text.toString().trim { it <= ' ' }
            if (validateInputs()) {
                activityRegisterBinding.loader.visibility = View.VISIBLE
                registerUser()
            }
        }
    }

    private fun registerUser() {
        val call: Call<com.basicapp.api.Response> =
            APIClient.getClient().create(APIInterface::class.java)
                .registerUserApi(fullName,username,password,confirmPassword,email)
        call.enqueue(object : Callback<com.basicapp.api.Response?> {
            override fun onResponse(
                call: Call<com.basicapp.api.Response?>,
                response: Response<com.basicapp.api.Response?>
            ) {
                try {
                    activityRegisterBinding.loader.visibility = View.GONE
                    if(response.body()!!.success)
                        loadDashboard()
                    else  Toast.makeText(
                        this@RegisterActivity,
                        "Unable to connect to server or host",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<com.basicapp.api.Response?>, t: Throwable) {
                try {
                    activityRegisterBinding.loader.visibility = View.GONE
                    Toast.makeText(
                        this@RegisterActivity,
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
     * Launch Dashboard Activity on Successful Sign Up
     */
    private fun loadDashboard() {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }


    /**
     * Validates inputs and shows error if any
     * @return
     */
    private fun validateInputs(): Boolean {
        if (EMPTY == fullName) {
            activityRegisterBinding.etFullName.error = "Full Name cannot be empty"
            activityRegisterBinding.etFullName.requestFocus()
            return false
        }
        if (EMPTY == username) {
            activityRegisterBinding.etUsername.error = "Username cannot be empty"
            activityRegisterBinding.etUsername.requestFocus()
            return false
        }

        if (EMPTY == email) {
            activityRegisterBinding.etEmailId.error = "Email cannot be empty"
            activityRegisterBinding.etEmailId.requestFocus()
            return false
        }

        if (!isValidEmail(email)) {
            activityRegisterBinding.etEmailId.error = "Email is not valid"
            activityRegisterBinding.etEmailId.requestFocus()
            return false
        }
        if (EMPTY == password) {
            activityRegisterBinding.etPassword.error = "Password cannot be empty"
            activityRegisterBinding.etPassword.requestFocus()
            return false
        }
        if (EMPTY == confirmPassword) {
            activityRegisterBinding.etConfirmPassword.error = "Confirm Password cannot be empty"
            activityRegisterBinding.etConfirmPassword.requestFocus()
            return false
        }
        if (password != confirmPassword) {
            activityRegisterBinding.etConfirmPassword.error = "Password and Confirm Password does not match"
            activityRegisterBinding.etConfirmPassword.requestFocus()
            return false
        }
        return true
    }
}