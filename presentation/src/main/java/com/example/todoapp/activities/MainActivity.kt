package com.example.todoapp.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.data.app.remote.TokenInterceptor
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToken()
        GlobalNavigator.registerHandler(this)
        setContent {
            TodoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavGraph()
                }
            }
        }
    }

    private fun setupToken() {
        sharedPrefs = this.getSharedPreferences(USER_PREFERENCES, 0)
        tokenInterceptor.token = sharedPrefs.getString(TOKEN, "").toString()
    }

    override fun logout() {
        sharedPrefs.edit().clear().apply()
    }

    override fun onStop() {
        super.onStop()
        GlobalNavigator.unregisterHandler()
    }
}
