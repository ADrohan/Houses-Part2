package org.wit.houses.views.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.houses.databinding.ActivityLoginBinding

class LoginView : AppCompatActivity(){
    lateinit var presenter: LoginPresenter
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        presenter = LoginPresenter( this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        binding.signUp.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email == "" || password == "") {
                Snackbar.make(binding.root, "please provide email and password", Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                presenter.doSignUp(email,password)
            }
        }

        binding.logIn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email == "" || password == "") {
                Snackbar.make(binding.root, "please provide email and password", Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                presenter.doLogin(email,password)
            }
        }
    }
}