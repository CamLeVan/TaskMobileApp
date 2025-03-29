package com.example.taskapplication.ui.personal

// app/src/main/java/com/example/taskmanager/ui/personal/PersonalTaskViewModel.kt
@HiltViewModel
class PersonalTaskViewModel @Inject constructor(
    private val repository: PersonalTaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<PersonalTask>>(emptyList())
    val tasks: StateFlow<List<PersonalTask>> = _tasks.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.getPersonalTasks()
                    .collect { tasks ->
                        _tasks.value = tasks
                        _loading.value = false
                    }
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    fun createTask(task: CreateTaskRequest) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.createTask(task)
                    .onSuccess {
                        loadTasks()
                    }
                    .onFailure { e ->
                        _error.value = e.message
                    }
            } finally {
                _loading.value = false
            }
        }
    }
}