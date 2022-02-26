package com.example.todoapp.view.addedittodo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.app.boundary.TodoRepository
import com.example.domain.app.entity.Todo
import com.example.domain.app.util.Resource
import com.example.todoapp.util.UiEvent
import com.example.todoapp.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repo: TodoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _channel = Channel<UiEvent>()
    val channel = _channel.receiveAsFlow()

    var id: String = savedStateHandle.get<String>("id").toString()

    private val _getTodoState: MutableStateFlow<ViewState<Todo>> =
        MutableStateFlow(ViewState.Initial)
    val getTodoState: StateFlow<ViewState<Todo>> get() = _getTodoState

    private val _createUpdateState: MutableStateFlow<ViewState<Unit>> =
        MutableStateFlow(ViewState.Initial)
    val createUpdateState: StateFlow<ViewState<Unit>> get() = _createUpdateState

    init {
        if (id.isNotBlank()) {
            getTodoById(id)
        }
    }

    private fun getTodoById(id: String) {
        viewModelScope.launch {
            repo.getTodoById(id).onStart {
                _getTodoState.value = ViewState.Loading
            }.collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> _getTodoState.value = ViewState.Success(resource.data)
                    is Resource.Error -> sendEvent(UiEvent.ShowAlertDialog(resource.error))
                    else -> Unit
                }
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.AddTodoClick -> {
                viewModelScope.launch {
                    repo.createTodo(event.title, event.description).onStart {
                        _createUpdateState.value = ViewState.Loading
                    }.collectLatest { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                sendEvent(UiEvent.PopBackStack)
                            }
                            is Resource.Error -> {
                                sendEvent(UiEvent.ShowAlertDialog(resource.error))
                            }
                            else -> Unit
                        }
                    }
                }
            }
            is AddEditTodoEvent.UpdateTodoClick -> {
                viewModelScope.launch {
                    repo.updateTodo(event.todo).onStart {
                        _createUpdateState.value = ViewState.Loading
                    }.collectLatest { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                sendEvent(UiEvent.PopBackStack)
                            }
                            is Resource.Error -> {
                                sendEvent(UiEvent.ShowAlertDialog(resource.error))
                            }
                            else -> Unit
                        }
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

    fun validateDescription(description: String, onDescriptionError: (Boolean) -> Unit) {
        onDescriptionError(!isDescriptionValid(description))
    }

    fun validateTitle(title: String, onTitleError: (Boolean) -> Unit) {
        onTitleError(!isTitleValid(title))
    }

    fun validate(
        title: String,
        description: String,
        onTitleError: (Boolean) -> Unit,
        onDescriptionError: (Boolean) -> Unit,
        onValidate: () -> Unit
    ) {
        onDescriptionError(!isDescriptionValid(description))
        onTitleError(!isTitleValid(title))
        if (isDescriptionValid(description) && isTitleValid(title)) onValidate()
    }

    private fun isTitleValid(title: String) = title.length >= 5
    private fun isDescriptionValid(description: String) = description.length in 20..200
}
