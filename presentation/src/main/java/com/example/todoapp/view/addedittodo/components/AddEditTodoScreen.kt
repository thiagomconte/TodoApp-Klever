package com.example.todoapp.view.addedittodo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.app.entity.Todo
import com.example.todoapp.R
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RobotoBold
import com.example.todoapp.ui.theme.RobotoRegular
import com.example.todoapp.util.UiEvent
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.components.*
import com.example.todoapp.view.addedittodo.AddEditTodoEvent
import com.example.todoapp.view.addedittodo.AddEditTodoViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun AddEditTodoScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {

    val errorMsg = remember { mutableStateOf("") }
    val showErrorMsg = remember { mutableStateOf(false) }
    val title = remember { mutableStateOf(TextFieldValue("")) }
    val titleError = remember { mutableStateOf(false) }
    val description = remember { mutableStateOf(TextFieldValue("")) }
    val descriptionError = remember { mutableStateOf(false) }
    val isChecked = remember { mutableStateOf(false) }
    val state = viewModel.getTodoState.collectAsState(ViewState.Initial).value
    val createUpdateState = viewModel.createUpdateState.collectAsState(ViewState.Initial).value

    LaunchedEffect(Unit) {
        viewModel.channel.collect { event ->
            when (event) {
                is UiEvent.ShowAlertDialog -> {
                    errorMsg.value = event.msg
                    showErrorMsg.value = true
                }
                is UiEvent.PopBackStack -> onPopBackStack()
                else -> Unit
            }
        }
    }

    when (state) {
        is ViewState.Loading -> LoadingComponent()
        is ViewState.Success -> {
            title.value = TextFieldValue(state.data.title)
            description.value = TextFieldValue(state.data.description)
            isChecked.value = state.data.completed
        }
        else -> Unit
    }

    when (createUpdateState) {
        is ViewState.Success -> onPopBackStack()
        is ViewState.Loading -> LoadingComponent()
        else -> Unit
    }

    if (showErrorMsg.value) {
        AlertDialogComponent(text = errorMsg.value) {
            showErrorMsg.value = false
            onPopBackStack()
        }
    }

    Scaffold(
        topBar = { TopAppBarComponent() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (state is ViewState.Success) "Update Task" else "Add Task",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = RobotoBold,
                fontSize = 42.sp
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title.value, onValueChange = { value ->
                    viewModel.validateTitle(value.text, onTitleError = {
                        titleError.value = it
                    })
                    title.value = value
                },
                label = {
                    NormalText(text = "Title")
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedLabelColor = DarkBlue,
                    focusedLabelColor = DarkBlue,
                    cursorColor = DarkBlue,
                    focusedIndicatorColor = if (titleError.value) Color.Red else DarkBlue,
                    unfocusedIndicatorColor = if (titleError.value) Color.Red else DarkBlue,
                    textColor = DarkBlue,
                ),
                textStyle = TextStyle(fontFamily = RobotoRegular)
            )
            if (titleError.value) {
                ErrorText(text = stringResource(R.string.title_validator))
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                value = description.value, onValueChange = { value ->
                    viewModel.validateDescription(value.text, onDescriptionError = {
                        descriptionError.value = it
                    })
                    if (value.text.length < 200) {
                        description.value = value
                    }
                },
                label = {
                    NormalText(text = "Description")
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedLabelColor = DarkBlue,
                    focusedLabelColor = DarkBlue,
                    cursorColor = DarkBlue,
                    focusedIndicatorColor = if (descriptionError.value) Color.Red else DarkBlue,
                    unfocusedIndicatorColor = if (descriptionError.value) Color.Red else DarkBlue,
                    textColor = DarkBlue,
                ),
                textStyle = TextStyle(fontFamily = RobotoRegular)
            )
            if (descriptionError.value) {
                ErrorText(text = stringResource(R.string.description_validator))
            }
            if (state is ViewState.Success) {
                Row() {
                    Checkbox(
                        checked = isChecked.value,
                        onCheckedChange = { isChecked.value = !isChecked.value },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    NormalText(text = "Mark as done")
                }
            }
            Button(
                onClick = {
                    if (state is ViewState.Success) {
                        viewModel.validate(
                            title.value.text, description.value.text, onTitleError = {
                                titleError.value = it
                            },
                            onDescriptionError = {
                                descriptionError.value = it
                            }, onValidate = {
                            viewModel.onEvent(
                                AddEditTodoEvent.UpdateTodoClick(
                                    Todo(
                                        state.data.id,
                                        title.value.text,
                                        description.value.text,
                                        isChecked.value
                                    )
                                )
                            )
                        }
                        )
                    } else {
                        viewModel.validate(
                            title.value.text, description.value.text, onTitleError = {
                                titleError.value = it
                            },
                            onDescriptionError = {
                                descriptionError.value = it
                            }, onValidate = {
                            viewModel.onEvent(
                                AddEditTodoEvent.AddTodoClick(
                                    title.value.text,
                                    description.value.text,
                                )
                            )
                        }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = DarkBlue, contentColor = Color.White
                )
            ) {
                Text(
                    text = if (state is ViewState.Success) "Update" else "Create",
                    modifier = Modifier.padding(vertical = 4.dp),
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}
