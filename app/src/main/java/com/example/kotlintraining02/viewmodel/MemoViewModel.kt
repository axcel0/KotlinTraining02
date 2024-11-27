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
        val memos = mutableListOf<Memo>()
        sharedPreferences.all.forEach { (key, value) ->
            if (value is String) {
                memos.add(Memo(key, value))
            }
        }
        return memos
    }

    private fun saveMemo(memo: Memo) {
        sharedPreferences.edit().putString(memo.title, memo.content).apply()
    }

    private fun removeMemo(title: String) {
        sharedPreferences.edit().remove(title).apply()
    }

    fun addMemo(memo: Memo) {
        viewModelScope.launch {
            val updatedMemos = _memos.value + memo
            _memos.value = updatedMemos
            saveMemo(memo)
        }
    }

    fun updateMemo(memo: Memo) {
        viewModelScope.launch {
            val updatedMemos = _memos.value.map {
                if (it.title == memo.title) memo else it
            }
            _memos.value = updatedMemos
            saveMemo(memo)
        }
    }

    fun deleteMemo(memo: Memo) {
        viewModelScope.launch {
            val updatedMemos = _memos.value.filter { it.title != memo.title }
            _memos.value = updatedMemos
            removeMemo(memo.title)
        }
    }
}