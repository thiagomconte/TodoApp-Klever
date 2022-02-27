package com.example.todoapp.view.todolist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.app.entity.Todo
import com.example.todoapp.R
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RobotoRegular
import com.example.todoapp.view.todolist.TodoListEvent

@Composable
fun TodoListItem(
    onDeleteTodoClick: (TodoListEvent.DeleteTodoClick) -> Unit,
    onEditTodoClick: (TodoListEvent.EditTodoClick) -> Unit,
    todo: Todo
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 24.dp)
            .clickable {
                onEditTodoClick(TodoListEvent.EditTodoClick(todo.id))
            },
        backgroundColor = DarkBlue,
        shape = RoundedCornerShape(32.dp),
        elevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.9f),
                text = todo.title,
                fontFamily = RobotoRegular,
                color = if (todo.completed) Color.White.copy(0.5f) else Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                style = if (todo.completed) TextStyle(textDecoration = TextDecoration.LineThrough) else TextStyle.Default
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_delete_black_24dp),
                contentDescription = "Delete todo",
                tint = Color.Red,
                modifier = Modifier.clickable {
                    onDeleteTodoClick(TodoListEvent.DeleteTodoClick(todo.id))
                }
            )
        }
    }
}
