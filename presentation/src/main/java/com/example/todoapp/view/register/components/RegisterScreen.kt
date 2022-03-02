package com.example.todoapp.view.register.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.R
import com.example.todoapp.ui.theme.DarkBlue
import com.example.todoapp.ui.theme.RobotoRegular
import com.example.todoapp.util.UiEvent
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.components.AlertDialogComponent
import com.example.todoapp.util.components.ErrorText
import com.example.todoapp.util.components.LoadingComponent
import com.example.todoapp.util.components.NormalText
import com.example.todoapp.view.register.RegisterEvent
import com.example.todoapp.view.register.RegisterViewModel
import kotlinx.coroutines.flow.collect

@ExperimentalAnimationApi
@Composable
fun RegisterScreen(
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    var loading by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var showMsgError by remember { mutableStateOf(false) }
    var msgError by remember { mutableStateOf("") }
    val registerState = viewModel.registerState.collectAsState(ViewState.Initial).value

    LaunchedEffect(Unit) {
        viewModel.channel.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack(UiEvent.PopBackStack)
                is UiEvent.ShowAlertDialog -> {
                    showMsgError = true
                    msgError = event.msg
                }
                else -> Unit
            }
        }
    }

    if (showMsgError) {
        AlertDialogComponent(msgError) {
            showMsgError = false
        }
    }

    if (registerState is ViewState.Loading) {
        LoadingComponent()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DarkBlue),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_person_add_black_48dp),
            contentDescription = "Account icon",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally),
            tint = Color.White
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                .background(color = Color.White)
                .padding(horizontal = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = {
                        NormalText(text = stringResource(id = R.string.name))
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_person_black_24dp),
                            contentDescription = "",
                            tint = DarkBlue
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    value = viewModel.name,
                    onValueChange = { value ->
                        viewModel.name = value
                        viewModel.validateName(onNameError = {
                            nameError = it
                        })
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedLabelColor = DarkBlue,
                        focusedLabelColor = DarkBlue,
                        cursorColor = DarkBlue,
                        focusedIndicatorColor = if (nameError) Color.Red else DarkBlue,
                        unfocusedIndicatorColor = if (nameError) Color.Red else DarkBlue,
                        textColor = DarkBlue,
                    ),
                    textStyle = TextStyle(fontFamily = RobotoRegular)
                )
                ErrorText(
                    error = nameError,
                    text = stringResource(id = R.string.name_validator)
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    label = {
                        NormalText(text = stringResource(id = R.string.email))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "",
                            tint = DarkBlue
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    value = viewModel.email,
                    onValueChange = { value ->
                        viewModel.email = value
                        viewModel.validateEmail(onEmailError = {
                            emailError = it
                        })
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedLabelColor = DarkBlue,
                        focusedLabelColor = DarkBlue,
                        cursorColor = DarkBlue,
                        focusedIndicatorColor = if (emailError) Color.Red else DarkBlue,
                        unfocusedIndicatorColor = if (emailError) Color.Red else DarkBlue,
                        textColor = DarkBlue,
                    ),
                    textStyle = TextStyle(fontFamily = RobotoRegular)
                )
                ErrorText(
                    error = emailError,
                    text = stringResource(id = R.string.invalid_email)
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = {
                        NormalText(text = stringResource(id = R.string.password))
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_lock_black_24dp),
                            contentDescription = "",
                            tint = DarkBlue
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    value = viewModel.password,
                    onValueChange = { value ->
                        viewModel.password = value
                        viewModel.validatePassword(onPasswordError = {
                            passwordError = it
                        })
                    },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility = !passwordVisibility
                        }) {
                            Icon(
                                painter =
                                if (passwordVisibility)
                                    painterResource(id = R.drawable.ic_visibility_black_24dp)
                                else painterResource(
                                    id = R.drawable.ic_visibility_off_black_24dp
                                ),
                                contentDescription = "", tint = DarkBlue
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedLabelColor = DarkBlue,
                        focusedLabelColor = DarkBlue,
                        cursorColor = DarkBlue,
                        focusedIndicatorColor = if (passwordError) Color.Red else DarkBlue,
                        unfocusedIndicatorColor = if (passwordError) Color.Red else DarkBlue,
                        textColor = DarkBlue,
                    ),
                    textStyle = TextStyle(fontFamily = RobotoRegular)
                )
                ErrorText(
                    error = passwordError,
                    text = stringResource(id = R.string.password_validator)
                )
                Button(
                    onClick = {
                        viewModel.validate(
                            onNameError = {
                                nameError = it
                            },
                            onPasswordError = {
                                passwordError = it
                            },
                            onEmailError = {
                                emailError = it
                            },
                            onValidate = {
                                viewModel.onEvent(
                                    RegisterEvent.RegisterUser(
                                        viewModel.name,
                                        viewModel.email,
                                        viewModel.password
                                    )
                                )
                                loading = true
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
                        text = stringResource(id = R.string.confirm_register),
                        modifier = Modifier.padding(vertical = 4.dp),
                        style = MaterialTheme.typography.h6
                    )
                }
                OutlinedButton(
                    onClick = { onPopBackStack(UiEvent.PopBackStack) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(32.dp),
                    border = BorderStroke(2.dp, DarkBlue),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White, contentColor = DarkBlue
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.back),
                        modifier = Modifier.padding(vertical = 4.dp),
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
    }
}
