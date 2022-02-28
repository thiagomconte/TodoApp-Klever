package com.example.todoapp.view.addedittodo.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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

@ExperimentalAnimationApi
@Composable
fun AddEditTodoScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {

    var errorMsg by remember { mutableStateOf("") }
    var showErrorMsg by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    val state = viewModel.getTodoState.collectAsState(ViewState.Initial).value
    val createUpdateState = viewModel.createUpdateState.collectAsState(ViewState.Initial).value

    LaunchedEffect(Unit) {
        viewModel.channel.collect { event ->
            when (event) {
                is UiEvent.ShowAlertDialog -> {
                    errorMsg = event.msg
                    showErrorMsg = true
                }
                is UiEvent.PopBackStack -> onPopBackStack()
                else -> Unit
            }
        }
    }

    when (state) {
        is ViewState.Loading -> LoadingComponent()
        else -> Unit
    }

    when (createUpdateState) {
        is ViewState.Success -> onPopBackStack()
        is ViewState.Loading -> LoadingComponent()
        else -> Unit
    }

    if (showErrorMsg) {
        AlertDialogComponent(text = errorMsg) {
            showErrorMsg = false
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
                text = if (state is ViewState.Success) stringResource(R.string.update_task) else stringResource(
                    R.string.add_task
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = RobotoBold,
                fontSize = 42.sp
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.title, onValueChange = { value ->
                    viewModel.title = value
                    viewModel.validateTitle(onTitleError = {
                        titleError = it
                    })
                },
                label = {
                    NormalText(text = stringResource(R.string.title))
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedLabelColor = DarkBlue,
                    focusedLabelColor = DarkBlue,
                    cursorColor = DarkBlue,
                    focusedIndicatorColor = if (titleError) Color.Red else DarkBlue,
                    unfocusedIndicatorColor = if (titleError) Color.Red else DarkBlue,
                    textColor = DarkBlue,
                ),
                textStyle = TextStyle(fontFamily = RobotoRegular)
            )
            AnimatedVisibility(visible = titleError, enter = scaleIn()) {
                ErrorText(text = stringResource(R.string.title_validator))
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                value = viewModel.description, onValueChange = { value ->
                    if (value.length < 200) {
                        viewModel.description = value
                    }
                    viewModel.validateDescription(onDescriptionError = {
                        descriptionError = it
                    })
                },
                label = {
                    NormalText(text = stringResource(R.string.description))
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedLabelColor = DarkBlue,
                    focusedLabelColor = DarkBlue,
                    cursorColor = DarkBlue,
                    focusedIndicatorColor = if (descriptionError) Color.Red else DarkBlue,
                    unfocusedIndicatorColor = if (descriptionError) Color.Red else DarkBlue,
                    textColor = DarkBlue,
                ),
                textStyle = TextStyle(fontFamily = RobotoRegular)
            )
            AnimatedVisibility(visible = descriptionError, enter = scaleIn()) {
                ErrorText(text = stringResource(R.string.description_validator))
            }
            if (state is ViewState.Success) {
                Row() {
                    Checkbox(
                        checked = viewModel.isChecked,
                        onCheckedChange = { viewModel.isChecked = !viewModel.isChecked },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        stringResource(R.string.mark_as_done),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        fontFamily = RobotoRegular
                    )
                }
            }
            Button(
                onClick = {
                    viewModel.validate(
                        onTitleError = {
                            titleError = it
                        },
                        onDescriptionError = {
                            descriptionError = it
                        },
                        onValidate = {
                            if (state is ViewState.Success) viewModel.onEvent(
                                AddEditTodoEvent.UpdateTodoClick(
                                    Todo(
                                        viewModel.id,
                                        viewModel.title,
                                        viewModel.description,
                                        viewModel.isChecked
                                    )
                                )
                            ) else viewModel.onEvent(
                                AddEditTodoEvent.AddTodoClick(
                                    viewModel.title,
                                    viewModel.description
                                )
                            )
                        }
                    )
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
                    text = if (state is ViewState.Success) stringResource(R.string.update) else stringResource(
                        R.string.create
                    ),
                    modifier = Modifier.padding(vertical = 4.dp),
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}