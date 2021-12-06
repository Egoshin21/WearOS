package com.example.we

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONObject

class Login : WearableActivity() {

    private lateinit var app: MyApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = applicationContext as MyApp

        val loginText = findViewById<EditText>(R.id.login)

        val passwordText = findViewById<EditText>(R.id.password)

        val login = findViewById<Button>(R.id.log)

        val exit = findViewById<Button>(R.id.exit)

        login.setOnClickListener {

            if(loginText.text!!.isNotEmpty() && passwordText.text!!.isNotEmpty())
            {
                app.loginText = loginText.text.toString()
                app.passwordText = passwordText.text.toString()
                startActivity(Intent(this, MainActivity::class.java))
            }
            else
                AlertDialog.Builder(this)
                    .setTitle("Ошибка")
                    .setMessage("Должны быть введены логин и пароль")
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
        }
        exit.setOnClickListener {
            HTTP.requestPOST(
                "http://s4a.kolei.ru/logout",
                JSONObject().put("username", app.username),
                mapOf(
                    "Content-Type" to "application/json"
                )
            ) { result, error ->
                // при выходе не забываем стереть существующий токен
                app.token = ""

                // каких-то осмысленных действий дальше не предполагается
                // разве что снова вызвать форму авторизации
                runOnUiThread {
                    if (result != null) {
                        Toast.makeText(this, "Logout success!", Toast.LENGTH_LONG).show()
                    } else {
                        androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Ошибка http-запроса")
                            .setMessage(error)
                            .setPositiveButton("OK", null)
                            .create()
                            .show()
                    }
                }
            }
        }
    }
}