package com.example.urbandictionary.customUtils

class CustomMatchers {
    companion object {
        fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher? {
            return RecyclerViewMatcher(
                recyclerViewId
            )
        }
    }
}