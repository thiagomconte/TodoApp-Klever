package com.example.todoapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.data.app.remote.TokenInterceptor
import com.example.data.app.util.Constants.TOKEN
import com.example.data.app.util.Constants.USER_PREFERENCES
import com.example.data.app.util.GlobalNavigator
import com.example.data.app.util.GlobalNavigatorHandler
import com.example.todoapp.ui.theme.TodoAppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity(), GlobalNavigatorHandler {

    @Inject
    lateinit var tokenInterceptor: TokenInterceptor
    private val sharedPrefs = this.getSharedPreferences(USER_PREFERENCES, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenInterceptor.token = sharedPrefs.getString(TOKEN, "").toString()
        GlobalNavigator.registerHandler(this)
        setContent {
            TodoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }

    override fun logout() {
        sharedPrefs.edit().clear().apply()
    }

    override fun onStop() {
        super.onStop()
        GlobalNavigator.unregisterHandler()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TodoAppTheme {
        Greeting("Android")
    }
}
