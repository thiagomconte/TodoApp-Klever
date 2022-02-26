package com.example.todoapp.view.login

import android.content.Context
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.app.util.Constants.EMAIL
import com.example.data.app.util.Constants.NAME
import com.example.data.app.util.Constants.TOKEN
import com.example.data.app.util.Constants.USER_PREFERENCES
import com.example.domain.app.boundary.UserRepository
import com.example.domain.app.util.Resource
import com.example.todoapp.util.Common
import com.example.todoapp.util.Constants.Routes.TODO_LIST
import com.example.todoapp.util.UiEvent
import com.example.todoapp.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: UserRepository,
) : ViewModel() {

    private val _channel = Channel<UiEvent>()
    val channel = _channel.receiveAsFlow()

    private val _loginState: MutableStateFlow<ViewState<Unit>> = MutableStateFlow(ViewState.Initial)
    val loginState: StateFlow<ViewState<Unit>> get() = _loginState

    fun onEvent(event: LoginEvent, context: Context) {
        if (event is LoginEvent.LoginUser) {
            viewModelScope.launch {
                repo.login(event.email, event.password).onStart {
                    _loginState.value = ViewState.Loading
                }.collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val sharedPref = context.getSharedPreferences(USER_PREFERENCES, 0)
                            sharedPref.edit().apply {
                                putString(TOKEN, resource.data.token)
                                putString(EMAIL, resource.data.email)
                                putString(NAME, resource.data.name)
                                apply()
                            }
                            sendEvent(UiEvent.Navigate(TODO_LIST))
                        }
                        is Resource.Error -> {
                            _loginState.value = ViewState.Error
                            sendEvent(UiEvent.ShowAlertDialog(resource.error))
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _channel.send(event)
        }
    }

    fun validatePassword(textFieldValue: TextFieldValue, onPasswordError: (Boolean) -> Unit) {
        onPasswordError(textFieldValue.text.isBlank())
    }

    fun validateEmail(textFieldValue: TextFieldValue, onEmailError: (Boolean) -> Unit) {
        onEmailError(!Common.isEmailValid(textFieldValue.text))
    }

    fun validate(
        email: String,
        password: String,
        onEmailError: (Boolean) -> Unit,
        onPasswordError: (Boolean) -> Unit,
        onValidate: () -> Unit
    ) {
        onEmailError(!Common.isEmailValid(email))
        onPasswordError(password.isBlank())
        if (email.isNotBlank() && password.isNotBlank()) {
            onValidate()
        }
    }
}
