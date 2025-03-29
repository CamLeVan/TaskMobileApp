package com.example.taskapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskapplication.data.model.Team
import com.example.taskapplication.ui.viewmodel.TeamState
import com.example.taskapplication.ui.viewmodel.TeamViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    onTeamClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamViewModel = viewModel()
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    val teams by viewModel.teams.collectAsState()
    val teamState by viewModel.teamState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTeams()
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (teamState) {
            is TeamState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is TeamState.Error -> {
                Text(
                    text = (teamState as TeamState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            else -> {
                if (teams.isEmpty()) {
                    Text(
                        text = "No teams yet",
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
                        items(teams) { team ->
                            TeamItem(
                                team = team,
                                onClick = { onTeamClick(team.teamId) }
                            )
                        }
                    }
                }
            }
        }
    }

    FloatingActionButton(
        onClick = { showCreateDialog = true },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
    ) {
        Icon(Icons.Default.Add, contentDescription = "Create Team")
    }

    if (showCreateDialog) {
        CreateTeamDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { name, description ->
                viewModel.createTeam(
                    Team(
                        teamId = 0,
                        name = name,
                        description = description,
                        createdBy = 0, // Will be set by the server
                        createdAt = "",
                        updatedAt = ""
                    )
                )
                showCreateDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamItem(
    team: Team,
    onClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    
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
                text = team.name,
                style = MaterialTheme.typography.titleMedium
            )
            
            if (!team.description.isNullOrBlank()) {
                Text(
                    text = team.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Text(
                text = "Created: ${dateFormat.format(Date(team.createdAt))}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun CreateTeamDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Team") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Team Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        name,
                        description.takeIf { it.isNotBlank() }
                    )
                },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 