package org.wit.houses.views.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.houses.views.houselist.HouseListView

class LoginPresenter (val view: LoginView)  {
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>

    init{
        registerLoginCallback()
    }

    fun doLogin(email: String, password: String) {
        val launcherIntent = Intent(view, HouseListView::class.java)
        loginIntentLauncher.launch(launcherIntent)
    }

    fun doSignUp(email: String, password: String) {
        val launcherIntent = Intent(view, HouseListView::class.java)
        loginIntentLauncher.launch(launcherIntent)
    }
    private fun registerLoginCallback(){
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}