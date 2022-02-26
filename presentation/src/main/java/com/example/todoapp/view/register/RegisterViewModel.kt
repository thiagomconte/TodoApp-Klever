package com.example.todoapp.view.register

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.app.boundary.UserRepository
import com.example.domain.app.util.Resource
import com.example.todoapp.util.Common
import com.example.todoapp.util.UiEvent
import com.example.todoapp.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: UserRepository
) : ViewModel() {

    private val _channel = Channel<UiEvent>()
    val channel = _channel.receiveAsFlow()

    private val _registerState: MutableStateFlow<ViewState<Unit>> =
        MutableStateFlow(ViewState.Initial)
    val registerState: StateFlow<ViewState<Unit>> get() = _registerState

    fun onEvent(event: RegisterEvent) {
        if (event is RegisterEvent.RegisterUser) {
            viewModelScope.launch {
                repo.register(event.name, event.email, event.password).onStart {
                    _registerState.value = ViewState.Loading
                }.collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> sendEvent(UiEvent.PopBackStack)
                        is Resource.Error -> {
                            _registerState.value = ViewState.Error
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

    fun validateName(
        textFieldValue: TextFieldValue,
        onNameError: (Boolean) -> Unit
    ) {
        onNameError(!isNameValid(textFieldValue.text))
    }

    fun validateEmail(
        textFieldValue: TextFieldValue,
        onEmailError: (Boolean) -> Unit
    ) {
        onEmailError(!Common.isEmailValid(textFieldValue.text))
    }

    fun validatePassword(
        textFieldValue: TextFieldValue,
        onPasswordError: (Boolean) -> Unit
    ) {
        onPasswordError(textFieldValue.text.length !in 9..19)
    }

    fun validate(
        email: String,
        password: String,
        name: String,
        onNameError: (Boolean) -> Unit,
        onEmailError: (Boolean) -> Unit,
        onPasswordError: (Boolean) -> Unit,
        onValidate: () -> Unit
    ) {
        onNameError(!isNameValid(name))
        onEmailError(!Common.isEmailValid(email))
        onPasswordError(!isPasswordValid(password))
        if (isNameValid(name) && isPasswordValid(password) && Common.isEmailValid(email)) {
            onValidate()
        }
    }

    private fun isNameValid(name: String) = name.length in 4..29

    private fun isPasswordValid(password: String) = password.length in 9..19
}
