package com.example.kotlintraining02

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kotlintraining02.model.Memo
import com.example.kotlintraining02.screen.MemoGridScreen
import com.example.kotlintraining02.screen.MemoListScreen
import com.example.kotlintraining02.ui.theme.KotlinTraining02Theme
import com.example.kotlintraining02.viewmodel.MemoViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            var useDynamicColor by remember { mutableStateOf(false) }
            KotlinTraining02Theme(darkTheme = isDarkTheme, dynamicColor = useDynamicColor) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MemoApp(
                        isDarkTheme = isDarkTheme,
                        useDynamicColor = useDynamicColor,
                        onToggleTheme = { isDarkTheme = !isDarkTheme },
                        onToggleDynamicColor = { useDynamicColor = !useDynamicColor }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoApp(isDarkTheme: Boolean, useDynamicColor: Boolean, onToggleTheme: () -> Unit, onToggleDynamicColor: () -> Unit) {
    var isGridView by remember { mutableStateOf(false) }
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel = MemoViewModel(LocalContext.current)
    var showDialog by remember { mutableStateOf(false) }
    var currentMemo by remember { mutableStateOf<Memo?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Settings", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Dark Theme")
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(checked = isDarkTheme, onCheckedChange = { onToggleTheme() })
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Dynamic Color")
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(checked = useDynamicColor, onCheckedChange = { onToggleDynamicColor() })
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Memo App") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isGridView = !isGridView }) {
                            Icon(
                                imageVector = if (isGridView) Icons.AutoMirrored.Default.ViewList else Icons.Filled.ViewModule,
                                contentDescription = if (isGridView) "Switch to List View" else "Switch to Grid View"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    currentMemo = null
                    showDialog = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Memo")
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                if (isGridView) {
                    MemoGridScreen(
                        viewModel = viewModel,
                        onEdit = { memo ->
                            showDialog = true
                            currentMemo = memo
                        },
                        onDelete = { memo ->
                            viewModel.deleteMemo(memo)
                        }
                    )
                } else {
                    MemoListScreen(
                        viewModel = viewModel,
                        onEdit = { memo ->
                            showDialog = true
                            currentMemo = memo
                        },
                        onDelete = { memo ->
                            viewModel.deleteMemo(memo)
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        MemoDialog(
            memo = currentMemo,
            onDismiss = { showDialog = false },
            onSave = { memo ->
                if (currentMemo == null) {
                    viewModel.addMemo(memo)
                } else {
                    viewModel.updateMemo(memo)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun MemoDialog(memo: Memo?, onDismiss: () -> Unit, onSave: (Memo) -> Unit) {
    var title by remember { mutableStateOf(memo?.title ?: "") }
    var content by remember { mutableStateOf(memo?.content ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (memo == null) "Add Memo" else "Edit Memo") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(Memo(title, content)) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}