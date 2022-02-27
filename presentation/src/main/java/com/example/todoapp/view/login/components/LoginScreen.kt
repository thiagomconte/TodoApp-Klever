package com.example.todoapp.view.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.todoapp.util.Constants.Routes.REGISTER
import com.example.todoapp.util.UiEvent
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.components.AlertDialogComponent
import com.example.todoapp.util.components.ErrorText
import com.example.todoapp.util.components.LoadingComponent
import com.example.todoapp.util.components.NormalText
import com.example.todoapp.view.login.LoginEvent
import com.example.todoapp.view.login.LoginViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun LoginScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    var passwordVisibility by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var showMsgError by remember { mutableStateOf(false) }
    var msgError by remember { mutableStateOf("") }
    val context = LocalContext.current
    val loginState = viewModel.loginState.collectAsState(ViewState.Initial).value

    LaunchedEffect(Unit) {
        viewModel.channel.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(UiEvent.Navigate(event.route))
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

    if (loginState is ViewState.Loading) {
        LoadingComponent()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DarkBlue),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Account icon",
            modifier = Modifier
                .size(200.dp)
                .align(CenterHorizontally),
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
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                        viewModel.validateEmail(onEmailError = { emailError = it })
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
                if (emailError) {
                    ErrorText(text = stringResource(id = R.string.invalid_email))
                }
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
                                contentDescription = "",
                                tint = DarkBlue
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
                if (passwordError) {
                    ErrorText(text = stringResource(id = R.string.password_required))
                }
                Button(
                    onClick = {
                        viewModel.validate(onEmailError = {
                            emailError = it
                        }, onPasswordError = {
                            passwordError = it
                        }, onValidate = {
                            viewModel.onEvent(
                                LoginEvent.LoginUser(
                                    viewModel.email,
                                    viewModel.password
                                ),
                                context
                            )
                        })
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
                        text = stringResource(id = R.string.login),
                        modifier = Modifier.padding(vertical = 4.dp),
                        style = MaterialTheme.typography.h6
                    )
                }
                OutlinedButton(
                    onClick = { onNavigate(UiEvent.Navigate(REGISTER)) },
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
                        text = stringResource(id = R.string.register),
                        modifier = Modifier.padding(vertical = 4.dp),
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
    }
}
