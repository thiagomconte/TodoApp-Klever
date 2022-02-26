package com.example.todoapp.view.todolist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.R
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RobotoRegular
import com.example.todoapp.util.UiEvent
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.components.LoadingComponent
import com.example.todoapp.util.components.TopAppBarComponent
import com.example.todoapp.view.todolist.TodoListEvent
import com.example.todoapp.view.todolist.TodoListViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
) {

    val errorMsg = remember { mutableStateOf("") }
    val showErrorMsg = remember { mutableStateOf(false) }
    val showConfirmDialog = remember { mutableStateOf(false) }

    val getTodosState = viewModel.getTodosState.collectAsState(ViewState.Initial).value
    val deleteTodoState = viewModel.deleteTodoState.collectAsState(ViewState.Initial).value

    LaunchedEffect(Unit) {
        viewModel.getTodos()
        viewModel.channel.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(UiEvent.Navigate(event.route))
                is UiEvent.ShowAlertDialog -> {
                    errorMsg.value = event.msg
                    showErrorMsg.value = true
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = { TopAppBarComponent() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(TodoListEvent.AddTodoClick) },
                backgroundColor = DarkBlue, contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (getTodosState) {
                is ViewState.Empty -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_task_black_24dp),
                            contentDescription = "Empty list",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            tint = DarkBlue,
                        )
                        Text(
                            text = stringResource(R.string.start_adding_task),
                            textAlign = TextAlign.Center,
                            color = DarkBlue,
                            fontFamily = RobotoRegular,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
                is ViewState.Loading -> LoadingComponent()
                is ViewState.Success -> {
                    LazyColumn() {
                        items(getTodosState.data) { todo ->
                            TodoListItem(
                                onDeleteTodoClick = { viewModel.onEvent(it) },
                                onEditTodoClick = { viewModel.onEvent(it) },
                                todo
                            )
                        }
                    }
                }
                is ViewState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_error_outline_black_24dp),
                            contentDescription = "Error",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            tint = DarkBlue
                        )
                        Text(
                            text = stringResource(R.string.error_fetch_task),
                            textAlign = TextAlign.Center,
                            color = DarkBlue,
                            fontFamily = RobotoRegular,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
                else -> Unit
            }
        }
    }
}
