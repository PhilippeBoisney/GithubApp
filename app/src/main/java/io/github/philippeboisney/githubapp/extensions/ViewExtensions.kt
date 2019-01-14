package io.github.philippeboisney.githubapp.extensions

import androidx.appcompat.widget.SearchView

fun SearchView.onQueryTextChange(action: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            return true
        }
        override fun onQueryTextChange(newText: String): Boolean {
            action.invoke(newText)
            return true
        }
    })
}