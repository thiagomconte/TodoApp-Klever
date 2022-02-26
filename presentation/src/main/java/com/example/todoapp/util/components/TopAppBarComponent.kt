package com.example.todoapp.util.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.app.util.Constants.NAME
import com.example.data.app.util.Constants.USER_PREFERENCES
import com.example.data.app.util.GlobalNavigator
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RobotoRegular

@Composable
fun TopAppBarComponent() {
    val sharedPrefs = LocalContext.current.getSharedPreferences(USER_PREFERENCES, 0)
    TopAppBar(
        title = {
            Text(
                text = sharedPrefs.getString(NAME, "").toString(),
                modifier = Modifier.fillMaxWidth(0.8f),
                fontFamily = RobotoRegular,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            Text(
                modifier = Modifier
                    .clickable {
                        GlobalNavigator.logout()
                    }
                    .padding(horizontal = 8.dp),
                text = "Logout",
            )
        },
        backgroundColor = DarkBlue,
        contentColor = Color.White
    )
}
