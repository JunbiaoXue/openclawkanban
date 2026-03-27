package com.openclaw.kanban.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class TodoItem(
    val id: Int,
    val text: String,
    val isDone: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen() {
    var todos by remember { mutableStateOf(listOf<TodoItem>()) }
    var newTodoText by remember { mutableStateOf("") }
    var nextId by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (newTodoText.isNotBlank()) {
                        todos = todos + TodoItem(nextId, newTodoText)
                        newTodoText = ""
                        nextId++
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Input field
            OutlinedTextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                label = { Text("New todo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    if (newTodoText.isNotBlank()) {
                        IconButton(onClick = {
                            todos = todos + TodoItem(nextId, newTodoText)
                            newTodoText = ""
                            nextId++
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                }
            )
            
            // Todo list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(todos, key = { it.id }) { todo ->
                    TodoItemRow(
                        todo = todo,
                        onToggle = {
                            todos = todos.map { 
                                if (it.id == todo.id) it.copy(isDone = !it.isDone) else it 
                            }
                        },
                        onDelete = {
                            todos = todos.filter { it.id != todo.id }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItemRow(
    todo: TodoItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (todo.isDone) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = if (todo.isDone) "Done" else "Not done",
                    tint = if (todo.isDone) MaterialTheme.colorScheme.primary 
                           else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = todo.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                color = if (todo.isDone) MaterialTheme.colorScheme.onSurfaceVariant 
                        else MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
