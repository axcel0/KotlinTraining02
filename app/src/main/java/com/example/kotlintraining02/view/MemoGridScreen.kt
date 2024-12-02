package com.example.kotlintraining02.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kotlintraining02.model.Memo
import com.example.kotlintraining02.ui.component.MemoItem
import com.example.kotlintraining02.viewmodel.MemoViewModel

@Composable
fun MemoGridScreen(viewModel: MemoViewModel = MemoViewModel(LocalContext.current),
                   onEdit: (Memo) -> Unit, onDelete: (Memo) -> Unit) {
    val memos by viewModel.memos.collectAsState()

    Box(modifier = Modifier.animateContentSize(tween(1000))) {
      LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)) {
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