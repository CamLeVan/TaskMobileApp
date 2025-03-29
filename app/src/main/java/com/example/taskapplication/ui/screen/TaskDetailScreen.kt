package com.example.taskapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskapplication.ui.viewmodel.TaskState
import com.example.taskapplication.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Long,
    onBackClick: () -> Unit,
    viewModel: TaskViewModel = viewModel()
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val personalTasks by viewModel.personalTasks.collectAsState()
    val personalTaskState by viewModel.personalTaskState.collectAsState()

    val task = personalTasks.find { it.taskId == taskId }

    LaunchedEffect(Unit) {
        viewModel.getPersonalTasks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (personalTaskState) {
                is TaskState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is TaskState.Error -> {
                    Text(
                        text = (personalTaskState as TaskState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    task?.let { currentTask ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = currentTask.title,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            if (currentTask.description != null) {
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = currentTask.description,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(bottom = 24.dp)
                                )
                            }

                            Text(
                                text = "Status: ${currentTask.status}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            if (currentTask.deadline != null) {
                                Text(
                                    text = "Deadline: ${currentTask.deadline}",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            if (currentTask.priority != null) {
                                Text(
                                    text = "Priority: ${currentTask.priority}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    } ?: run {
                        Text(
                            text = "Task not found",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        task?.let { currentTask ->
            EditTaskDialog(
                task = currentTask,
                onDismiss = { showEditDialog = false },
                onConfirm = { title, description, deadline, priority, status ->
                    viewModel.updatePersonalTask(
                        taskId = currentTask.taskId,
                        task = currentTask.copy(
                            title = title,
                            description = description,
                            deadline = deadline,
                            priority = priority,
                            status = status
                        )
                    )
                    showEditDialog = false
                }
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Task") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePersonalTask(taskId)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EditTaskDialog(
    task: PersonalTask,
    onDismiss: () -> Unit,
    onConfirm: (String, String?, String?, Int?, String) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description ?: "") }
    var deadline by remember { mutableStateOf(task.deadline ?: "") }
    var priority by remember { mutableStateOf(task.priority?.toString() ?: "") }
    var status by remember { mutableStateOf(task.status) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Deadline") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = priority,
                    onValueChange = { priority = it },
                    label = { Text("Priority") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text("Status") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        title,
                        description.takeIf { it.isNotBlank() },
                        deadline.takeIf { it.isNotBlank() },
                        priority.toIntOrNull(),
                        status
                    )
                },
                enabled = title.isNotBlank() && status.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 