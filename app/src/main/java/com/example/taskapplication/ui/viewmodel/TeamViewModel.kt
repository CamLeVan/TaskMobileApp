package com.example.taskapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapplication.data.model.Team
import com.example.taskapplication.data.model.TeamMember
import com.example.taskapplication.data.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeamViewModel(private val teamRepository: TeamRepository) : ViewModel() {

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    private val _teamState = MutableStateFlow<TeamState>(TeamState.Initial)
    val teamState: StateFlow<TeamState> = _teamState.asStateFlow()

    private val _teamMembers = MutableStateFlow<List<TeamMember>>(emptyList())
    val teamMembers: StateFlow<List<TeamMember>> = _teamMembers.asStateFlow()

    private val _teamMemberState = MutableStateFlow<TeamState>(TeamState.Initial)
    val teamMemberState: StateFlow<TeamState> = _teamMemberState.asStateFlow()

    fun getTeams() {
        viewModelScope.launch {
            _teamState.value = TeamState.Loading
            teamRepository.getTeams()
                .onSuccess { teams ->
                    _teams.value = teams
                    _teamState.value = TeamState.Success
                }
                .onFailure { error ->
                    _teamState.value = TeamState.Error(error.message ?: "Failed to get teams")
                }
        }
    }

    fun createTeam(team: Team) {
        viewModelScope.launch {
            _teamState.value = TeamState.Loading
            teamRepository.createTeam(team)
                .onSuccess {
                    getTeams()
                    _teamState.value = TeamState.Success
                }
                .onFailure { error ->
                    _teamState.value = TeamState.Error(error.message ?: "Failed to create team")
                }
        }
    }

    fun updateTeam(teamId: Long, team: Team) {
        viewModelScope.launch {
            _teamState.value = TeamState.Loading
            teamRepository.updateTeam(teamId, team)
                .onSuccess {
                    getTeams()
                    _teamState.value = TeamState.Success
                }
                .onFailure { error ->
                    _teamState.value = TeamState.Error(error.message ?: "Failed to update team")
                }
        }
    }

    fun deleteTeam(teamId: Long) {
        viewModelScope.launch {
            _teamState.value = TeamState.Loading
            teamRepository.deleteTeam(teamId)
                .onSuccess {
                    getTeams()
                    _teamState.value = TeamState.Success
                }
                .onFailure { error ->
                    _teamState.value = TeamState.Error(error.message ?: "Failed to delete team")
                }
        }
    }

    fun getTeamMembers(teamId: Long) {
        viewModelScope.launch {
            _teamMemberState.value = TeamState.Loading
            teamRepository.getTeamMembers(teamId)
                .onSuccess { members ->
                    _teamMembers.value = members
                    _teamMemberState.value = TeamState.Success
                }
                .onFailure { error ->
                    _teamMemberState.value = TeamState.Error(error.message ?: "Failed to get team members")
                }
        }
    }

    fun addTeamMember(teamId: Long, member: TeamMember) {
        viewModelScope.launch {
            _teamMemberState.value = TeamState.Loading
            teamRepository.addTeamMember(teamId, member)
                .onSuccess {
                    getTeamMembers(teamId)
                    _teamMemberState.value = TeamState.Success
                }
                .onFailure { error ->
                    _teamMemberState.value = TeamState.Error(error.message ?: "Failed to add team member")
                }
        }
    }

    fun removeTeamMember(teamId: Long, userId: Long) {
        viewModelScope.launch {
            _teamMemberState.value = TeamState.Loading
            teamRepository.removeTeamMember(teamId, userId)
                .onSuccess {
                    getTeamMembers(teamId)
                    _teamMemberState.value = TeamState.Success
                }
                .onFailure { error ->
                    _teamMemberState.value = TeamState.Error(error.message ?: "Failed to remove team member")
                }
        }
    }
}

sealed class TeamState {
    object Initial : TeamState()
    object Loading : TeamState()
    object Success : TeamState()
    data class Error(val message: String) : TeamState()
} 