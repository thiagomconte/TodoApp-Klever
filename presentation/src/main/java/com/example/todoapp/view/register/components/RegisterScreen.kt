package com.example.todoapp.view.register.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import androidx.compose.ui.text.input.TextFieldValue
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

@Composable
fun RegisterScreen(
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val loading = remember { mutableStateOf(false) }
    val passwordVisibility = remember { mutableStateOf(false) }
    val nameError = remember { mutableStateOf(false) }
    val emailError = remember { mutableStateOf(false) }
    val passwordError = remember { mutableStateOf(false) }
    val showMsgError = remember { mutableStateOf(false) }
    val msgError = remember { mutableStateOf("") }
    val registerState = viewModel.registerState.collectAsState(ViewState.Initial).value

    LaunchedEffect(Unit) {
        viewModel.channel.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> onPopBackStack(UiEvent.PopBackStack)
                is UiEvent.ShowAlertDialog -> {
                    showMsgError.value = true
                    msgError.value = event.msg
                }
                else -> Unit
            }
        }
    }

    if (showMsgError.value) {
        AlertDialogComponent(msgError.value) {
            showMsgError.value = false
        }
    }

    if (registerState is ViewState.Loading) {
        LoadingComponent()
    }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
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
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = {
                        NormalText(text = stringResource(id = R.string.name))
                    },
                    shape = RoundedCornerShape(20.dp),
                    value = name.value,
                    onValueChange = { value ->
                        viewModel.validateName(value, onNameError = {
                            nameError.value = it
                        })
                        name.value = value
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedLabelColor = DarkBlue,
                        focusedLabelColor = DarkBlue,
                        cursorColor = DarkBlue,
                        focusedIndicatorColor = if (nameError.value) Color.Red else DarkBlue,
                        unfocusedIndicatorColor = if (nameError.value) Color.Red else DarkBlue,
                        textColor = DarkBlue,
                    ),
                    textStyle = TextStyle(fontFamily = RobotoRegular)
                )
                if (nameError.value) {
                    ErrorText(text = stringResource(id = R.string.name_validator))
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    label = {
                        NormalText(text = stringResource(id = R.string.email))
                    },
                    shape = RoundedCornerShape(20.dp),
                    value = email.value,
                    onValueChange = { value ->
                        viewModel.validateEmail(value, onEmailError = {
                            emailError.value = it
                        })
                        email.value = value
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedLabelColor = DarkBlue,
                        focusedLabelColor = DarkBlue,
                        cursorColor = DarkBlue,
                        focusedIndicatorColor = if (emailError.value) Color.Red else DarkBlue,
                        unfocusedIndicatorColor = if (emailError.value) Color.Red else DarkBlue,
                        textColor = DarkBlue,
                    ),
                    textStyle = TextStyle(fontFamily = RobotoRegular)
                )
                if (emailError.value) {
                    ErrorText(text = stringResource(id = R.string.invalid_email))
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = {
                        NormalText(text = stringResource(id = R.string.password))
                    },
                    shape = RoundedCornerShape(20.dp),
                    value = password.value,
                    onValueChange = { value ->
                        viewModel.validatePassword(value, onPasswordError = {
                            passwordError.value = it
                        })
                        password.value = value
                    },
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }) {
                            Icon(
                                painter =
                                if (passwordVisibility.value)
                                    painterResource(id = R.drawable.ic_visibility_black_24dp)
                                else painterResource(
                                    id = R.drawable.ic_visibility_off_black_24dp
                                ),
                                contentDescription = ""
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        unfocusedLabelColor = DarkBlue,
                        focusedLabelColor = DarkBlue,
                        cursorColor = DarkBlue,
                        focusedIndicatorColor = if (passwordError.value) Color.Red else DarkBlue,
                        unfocusedIndicatorColor = if (passwordError.value) Color.Red else DarkBlue,
                        textColor = DarkBlue,
                    ),
                    textStyle = TextStyle(fontFamily = RobotoRegular)
                )
                if (passwordError.value) {
                    ErrorText(text = stringResource(id = R.string.password_validator))
                }
                Button(
                    onClick = {
                        viewModel.validate(
                            email.value.text,
                            password.value.text,
                            name.value.text,
                            onNameError = {
                                nameError.value = it
                            },
                            onPasswordError = {
                                passwordError.value = it
                            },
                            onEmailError = {
                                emailError.value = it
                            },
                            onValidate = {
                                viewModel.onEvent(
                                    RegisterEvent.RegisterUser(
                                        name.value.text,
                                        email.value.text,
                                        password.value.text
                                    )
                                )
                                loading.value = true
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