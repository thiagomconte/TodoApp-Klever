package com.example.todoapp.view.todolist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            .clickable {
                onEditTodoClick(TodoListEvent.EditTodoClick(todo.id))
            }
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 24.dp),
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
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
