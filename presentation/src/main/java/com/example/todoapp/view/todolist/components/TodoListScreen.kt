package com.example.todoapp.view.todolist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.R
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RobotoBold
import com.example.todoapp.ui.theme.RobotoRegular
import com.example.todoapp.util.UiEvent
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.components.AlertDialogComponent
import com.example.todoapp.util.components.LoadingComponent
import com.example.todoapp.util.components.NormalText
import com.example.todoapp.util.components.TopAppBarComponent
import com.example.todoapp.view.todolist.TodoListEvent
import com.example.todoapp.view.todolist.TodoListViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collect

@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
) {

    var errorMsg by remember { mutableStateOf("") }
    var showErrorMsg by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var id by remember { mutableStateOf("") }

    val getTodosState = viewModel.getTodosState.collectAsState(ViewState.Initial).value
    val deleteTodoState = viewModel.deleteTodoState.collectAsState(ViewState.Initial).value
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTodos()
        viewModel.channel.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(UiEvent.Navigate(event.route))
                is UiEvent.ShowAlertDialog -> {
                    errorMsg = event.msg
                    showErrorMsg = true
                }
                else -> Unit
            }
        }
    }

    if (deleteTodoState is ViewState.Loading) LoadingComponent()

    if (showErrorMsg) {
        AlertDialogComponent(errorMsg, onDismiss = { showErrorMsg = false })
    }

    if (showConfirmDialog) OpenDialog(
        onDismiss = { showConfirmDialog = false },
        onConfirm = {
            viewModel.onEvent(it)
            showConfirmDialog = false
        },
        id = id
    )

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
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.refresh() },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.LightGray)
            ) {

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
                            item {
                                Text(
                                    stringResource(R.string.task_list),
                                    fontFamily = RobotoBold,
                                    fontSize = 42.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            items(getTodosState.data) { todo ->
                                TodoListItem(
                                    onDeleteTodoClick = {
                                        showConfirmDialog = true
                                        id = it.id
                                    },
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
}

@Composable
fun OpenDialog(
    onDismiss: () -> Unit,
    onConfirm: (TodoListEvent.DeleteTodoClick) -> Unit,
    id: String
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                NormalText(text = stringResource(id = R.string.are_you_sure))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = DarkBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(
                        onClick = {
                            onConfirm(TodoListEvent.DeleteTodoClick(id))
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = DarkBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = stringResource(id = R.string.confirm))
                    }
                }
            }
        }
    }
}
