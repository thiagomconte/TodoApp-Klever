package com.example.todoapp.util.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RobotoRegular

@Composable
fun LoadingComponent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.3f))
            .zIndex(1f)
    ) {
        Card(
            modifier = Modifier
                .align(Center)
                .size(height = 150.dp, width = 200.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = DarkBlue,
                    modifier = Modifier.align(CenterHorizontally),
                )
                Text(
                    text = "Loading...",
                    fontFamily = RobotoRegular,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }

    }
}
