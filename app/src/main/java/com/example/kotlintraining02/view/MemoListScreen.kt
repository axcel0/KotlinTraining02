package com.example.kotlintraining02.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.kotlintraining02.model.Memo
import com.example.kotlintraining02.ui.component.MemoItem
import com.example.kotlintraining02.viewmodel.MemoViewModel

@Composable
fun MemoListScreen(viewModel: MemoViewModel = MemoViewModel(LocalContext.current),
                   onEdit: (Memo) -> Unit, onDelete: (Memo) -> Unit) {
    val memos by viewModel.memos.collectAsState()

    Box(modifier = Modifier.animateContentSize(tween(1000))) {
        LazyColumn {
            items(memos) { memo ->
                MemoItem(
                    memo = memo,
                    onClick = { onEdit(memo) },
                    onLongClick = { onDelete(memo) }
                )
            }
        }
    }
}