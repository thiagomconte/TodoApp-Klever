package com.example.todoapp.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.data.app.remote.TokenInterceptor
import com.example.data.app.util.Constants.NAME
import com.example.data.app.util.Constants.TOKEN
import com.example.data.app.util.Constants.USER_PREFERENCES
import com.example.data.app.util.GlobalNavigator
import com.example.data.app.util.GlobalNavigatorHandler
import com.example.todoapp.navgraph.NavGraph
import com.example.todoapp.ui.theme.TodoAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), GlobalNavigatorHandler {

    @Inject
    lateinit var tokenInterceptor: TokenInterceptor
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var token: String
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToken()
        GlobalNavigator.registerHandler(this)
        setContent {
            TodoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavGraph(token, name)
                }
            }
        }
    }

    private fun setupToken() {
        sharedPrefs = this.getSharedPreferences(USER_PREFERENCES, 0)
        token = sharedPrefs.getString(TOKEN, "").toString()
        name = sharedPrefs.getString(NAME, "").toString()
        tokenInterceptor.token = token
    }

    override fun logout() {
        sharedPrefs.edit().clear().apply()
        val intent = intent
        finish()
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        GlobalNavigator.unregisterHandler()
    }
}
