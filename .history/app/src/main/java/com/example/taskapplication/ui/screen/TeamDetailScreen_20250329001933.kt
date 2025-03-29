package com.example.taskapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.taskapplication.data.model.Task
import com.example.taskapplication.data.model.Team
import com.example.taskapplication.ui.viewmodel.TaskViewModel
import com.example.taskapplication.ui.viewmodel.TeamViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailScreen(
    teamId: String,
    onBackClick: () -> Unit,
    onTaskClick: (String) -> Unit,
    viewModel: TeamViewModel,
    taskViewModel: TaskViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    
    val teamState by viewModel.teamState.collectAsState()
    val tasksState by taskViewModel.tasksState.collectAsState()
    
    LaunchedEffect(teamId) {
        viewModel.getTeam(teamId)
        taskViewModel.getTeamTasks(teamId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Team Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Team")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Team")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddTaskDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                teamState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                teamState.error != null -> {
                    Text(
                        text = teamState.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                teamState.team != null -> {
                    val team = teamState.team
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        item {
                            TeamInfo(team)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Team Tasks",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        
                        when {
                            tasksState.isLoading -> {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                            tasksState.error != null -> {
                                item {
                                    Text(
                                        text = tasksState.error,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            else -> {
                                items(tasksState.tasks) { task ->
                                    TaskItem(
                                        task = task,
                                        onClick = { onTaskClick(task.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (showEditDialog) {
        EditTeamDialog(
            team = teamState.team,
            onDismiss = { showEditDialog = false },
            onConfirm = { name, description ->
                viewModel.updateTeam(teamId, name, description)
                showEditDialog = false
            }
        )
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Team") },
            text = { Text("Are you sure you want to delete this team?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTeam(teamId)
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
    
    if (showAddTaskDialog) {
        CreateTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onConfirm = { title, description, deadline, priority ->
                taskViewModel.createTeamTask(
                    teamId = teamId,
                    title = title,
                    description = description,
                    deadline = deadline,
                    priority = priority
                )
                showAddTaskDialog = false
            }
        )
    }
}

@Composable
private fun TeamInfo(team: Team) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()) }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = team.name,
            style = MaterialTheme.typography.headlineMedium
        )
        
        if (!team.description.isNullOrBlank()) {
            Text(
                text = team.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        Text(
            text = "Created: ${dateFormat.format(Date(team.createdAt))}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        if (team.updatedAt != team.createdAt) {
            Text(
                text = "Updated: ${dateFormat.format(Date(team.updatedAt))}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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
            
            if (!task.description.isNullOrBlank()) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.status,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Priority: ${task.priority}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 