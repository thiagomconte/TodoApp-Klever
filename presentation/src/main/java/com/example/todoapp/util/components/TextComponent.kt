package com.example.todoapp.util.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.ui.theme.RobotoBold
import com.example.todoapp.ui.theme.RobotoRegular

@Composable
fun NormalText(text: String) {
    Text(text, fontFamily = RobotoRegular)
}

@Composable
fun BoldText(text: String) {
    Text(text, fontFamily = RobotoBold, fontWeight = FontWeight.Bold)
}

@Composable
fun ErrorText(text: String) {
    Text(
        text,
        fontFamily = RobotoRegular,
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 8.dp),
        color = Color.Red,
    )
}
