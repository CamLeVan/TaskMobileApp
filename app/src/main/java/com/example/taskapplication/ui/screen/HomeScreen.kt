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
import com.example.taskapplication.ui.viewmodel.AuthViewModel
import com.example.taskapplication.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onTaskClick: (Long) -> Unit,
    onTeamClick: (Long) -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    taskViewModel: TaskViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val personalTasks by taskViewModel.personalTasks.collectAsState()
    val personalTaskState by taskViewModel.personalTaskState.collectAsState()

    LaunchedEffect(Unit) {
        taskViewModel.getPersonalTasks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Manager") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Task, contentDescription = "Tasks") },
                    label = { Text("Tasks") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Group, contentDescription = "Teams") },
                    label = { Text("Teams") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> PersonalTasksScreen(
                tasks = personalTasks,
                state = personalTaskState,
                onTaskClick = onTaskClick,
                modifier = Modifier.padding(paddingValues)
            )
            1 -> TeamsScreen(
                onTeamClick = onTeamClick,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun PersonalTasksScreen(
    tasks: List<PersonalTask>,
    state: TaskState,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is TaskState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is TaskState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            else -> {
                if (tasks.isEmpty()) {
                    Text(
                        text = "No tasks yet",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tasks) { task ->
                            TaskItem(
                                task = task,
                                onClick = { onTaskClick(task.taskId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: PersonalTask,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium
            )
            if (task.description != null) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status: ${task.status}",
                    style = MaterialTheme.typography.bodySmall
                )
                if (task.deadline != null) {
                    Text(
                        text = "Deadline: ${task.deadline}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
} 