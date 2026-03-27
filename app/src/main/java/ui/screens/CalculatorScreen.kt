package com.openclaw.kanban.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorScreen() {
    var display by remember { mutableStateOf("0") }
    var previousValue by remember { mutableStateOf<Double?>(null) }
    var operation by remember { mutableStateOf<String?>(null) }
    var shouldResetDisplay by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = display,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.End
                )
            }
        }
        
        // Buttons
        val buttons = listOf(
            listOf("C", "±", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "⌫", "=")
        )
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { button ->
                        CalculatorButton(
                            text = button,
                            isOperation = button in listOf("÷", "×", "-", "+", "="),
                            isFunction = button in listOf("C", "±", "%", "⌫"),
                            modifier = Modifier
                                .weight(if (button == "0") 2f else 1f)
                                .aspectRatio(if (button == "0") 2f else 1f),
                            onClick = {
                                when (button) {
                                    "C" -> {
                                        display = "0"
                                        previousValue = null
                                        operation = null
                                        shouldResetDisplay = false
                                    }
                                    "⌫" -> {
                                        if (display.length > 1) {
                                            display = display.dropLast(1)
                                        } else {
                                            display = "0"
                                        }
                                    }
                                    "±" -> {
                                        val value = display.toDoubleOrNull() ?: 0.0
                                        display = (-value).toString().removeSuffix(".0")
                                    }
                                    "%" -> {
                                        val value = display.toDoubleOrNull() ?: 0.0
                                        display = (value / 100).toString().removeSuffix(".0")
                                    }
                                    in listOf("÷", "×", "-", "+") -> {
                                        previousValue = display.toDoubleOrNull()
                                        operation = button
                                        shouldResetDisplay = true
                                    }
                                    "=" -> {
                                        val current = display.toDoubleOrNull() ?: 0.0
                                        val previous = previousValue ?: current
                                        val result = when (operation) {
                                            "÷" -> previous / current
                                            "×" -> previous * current
                                            "-" -> previous - current
                                            "+" -> previous + current
                                            else -> current
                                        }
                                        display = if (result % 1 == 0.0) {
                                            result.toInt().toString()
                                        } else {
                                            String.format("%.8f", result).trimEnd('0').trimEnd('.')
                                        }
                                        previousValue = null
                                        operation = null
                                        shouldResetDisplay = true
                                    }
                                    "." -> {
                                        if (!display.contains(".")) {
                                            display += "."
                                        }
                                    }
                                    else -> {
                                        if (shouldResetDisplay || display == "0") {
                                            display = button
                                            shouldResetDisplay = false
                                        } else {
                                            display += button
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    isOperation: Boolean,
    isFunction: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val containerColor = when {
        isOperation -> MaterialTheme.colorScheme.primary
        isFunction -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    val contentColor = when {
        isOperation -> MaterialTheme.colorScheme.onPrimary
        isFunction -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Text(
            text = text,
            fontSize = 24.sp
        )
    }
}
