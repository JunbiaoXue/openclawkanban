package com.openclaw.kanban.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Note(
    val id: Int,
    val title: String,
    val content: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen() {
    var notes by remember { mutableStateOf(listOf<Note>()) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }
    var nextId by remember { mutableStateOf(0) }
    var isEditing by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Note" else "Notes") }
            )
        },
        floatingActionButton = {
            if (!isEditing) {
                FloatingActionButton(
                    onClick = {
                        isEditing = true
                        selectedNote = null
                        titleText = ""
                        contentText = ""
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add note")
                }
            }
        }
    ) { padding ->
        if (isEditing) {
            // Edit screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contentText,
                    onValueChange = { contentText = it },
                    label = { Text("Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    minLines = 5
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            isEditing = false
                            titleText = ""
                            contentText = ""
                            selectedNote = null
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (titleText.isNotBlank()) {
                                if (selectedNote != null) {
                                    notes = notes.map { 
                                        if (it.id == selectedNote!!.id) 
                                            it.copy(title = titleText, content = contentText) 
                                        else it 
                                    }
                                } else {
                                    notes = notes + Note(nextId, titleText, contentText)
                                    nextId++
                                }
                                isEditing = false
                                titleText = ""
                                contentText = ""
                                selectedNote = null
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save")
                    }
                }
            }
        } else {
            // List screen
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        onClick = {
                            selectedNote = note
                            titleText = note.title
                            contentText = note.content
                            isEditing = true
                        },
                        onDelete = {
                            notes = notes.filter { it.id != note.id }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            if (note.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
        }
    }
}
