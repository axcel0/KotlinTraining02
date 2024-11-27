package com.example.kotlintraining02.screen

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.kotlintraining02.model.Memo
import com.example.kotlintraining02.ui.component.MemoItem
import com.example.kotlintraining02.viewmodel.MemoViewModel

@Composable
fun MemoGridScreen(viewModel: MemoViewModel = MemoViewModel(LocalContext.current), onEdit: (Memo) -> Unit, onDelete: (Memo) -> Unit) {
    val memos by viewModel.memos.collectAsState()

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(memos) { memo ->
            MemoItem(
                memo = memo,
                onClick = { onEdit(memo) },
                onLongClick = { onDelete(memo) }
            )
        }
    }
}