package com.example.kotlintraining02.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlintraining02.model.Memo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MemoViewModel(context: Context) : ViewModel() {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("memos", Context.MODE_PRIVATE)
    private val _memos = MutableStateFlow(loadMemos())
    val memos: StateFlow<List<Memo>> = _memos

    private fun loadMemos(): List<Memo> {
        return sharedPreferences.all.mapNotNull { (key, value) ->
            if (value is String) Memo(key, value) else null
        }
    }

    fun addMemo(memo: Memo) {
        viewModelScope.launch {
            _memos.value = _memos.value + memo
            sharedPreferences.edit().putString(memo.title, memo.content).apply()
        }
    }

    fun updateMemo(memo: Memo) {
        viewModelScope.launch {
            _memos.value = _memos.value.map { if (it.title == memo.title) memo else it }
            sharedPreferences.edit().putString(memo.title, memo.content).apply()
        }
    }

    fun deleteMemo(memo: Memo) {
        viewModelScope.launch {
            _memos.value = _memos.value.filter { it.title != memo.title }
            sharedPreferences.edit().remove(memo.title).apply()
        }
    }
}