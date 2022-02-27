package com.example.todoapp.view.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.app.boundary.TodoRepository
import com.example.domain.app.entity.Todo
import com.example.domain.app.util.Resource
import com.example.todoapp.util.Constants.Routes.ADD_EDIT_TODO
import com.example.todoapp.util.UiEvent
import com.example.todoapp.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repo: TodoRepository
) : ViewModel() {

    private val _channel = Channel<UiEvent>()
    val channel = _channel.receiveAsFlow()

    private val _getTodosState: MutableStateFlow<ViewState<List<Todo>>> =
        MutableStateFlow(ViewState.Initial)
    val getTodosState: StateFlow<ViewState<List<Todo>>> get() = _getTodosState

    private val _deleteTodoState: MutableStateFlow<ViewState<Unit>> =
        MutableStateFlow(ViewState.Initial)
    val deleteTodoState: StateFlow<ViewState<Unit>> get() = _deleteTodoState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            getTodos()
            _isRefreshing.emit(false)
        }
    }

    fun onEvent(event: TodoListEvent) {
        when (event) {
            is TodoListEvent.AddTodoClick -> sendEvent(UiEvent.Navigate(ADD_EDIT_TODO))
            is TodoListEvent.EditTodoClick -> sendEvent(UiEvent.Navigate("$ADD_EDIT_TODO?id=${event.id}"))
            is TodoListEvent.DeleteTodoClick -> {
                viewModelScope.launch {
                    repo.deleteTodo(event.id).onStart {
                        _deleteTodoState.value = ViewState.Loading
                    }.collectLatest { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                _deleteTodoState.value = ViewState.Success(Unit)
                                getTodos()
                            }
                            is Resource.Error -> {
                                _deleteTodoState.value = ViewState.Error
                                sendEvent(UiEvent.ShowAlertDialog(resource.error))
                            }
                        }
                    }
                }
            }
        }
    }

    fun getTodos() {
        viewModelScope.launch {
            repo.getTodos().onStart {
                _getTodosState.value = ViewState.Loading
            }.collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        if (resource.data.isEmpty()) {
                            _getTodosState.value = ViewState.Empty
                        } else {
                            _getTodosState.value = ViewState.Success(resource.data)
                        }
                    }
                    is Resource.Error -> {
                        _getTodosState.value = ViewState.Error
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
}
