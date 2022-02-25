package com.example.todoapp.util.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.todoapp.ui.theme.DarkBlue

@Composable
fun AlertDialogComponent(text: String, onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = { onDismiss() }, confirmButton = {
        Button(
            onClick = { onDismiss() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = DarkBlue,
                contentColor = Color.White
            )
        ) {
            Text(text = "Close")
        }
    }, text = {
        Text(text)
    }, title = { Text("Error") })
}
