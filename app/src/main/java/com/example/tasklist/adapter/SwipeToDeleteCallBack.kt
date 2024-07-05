package com.example.tasklist.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(private val adapter: ItemTouchHelperAdapter) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        override fun onMove( recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder,
                             target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            adapter.onItemDismiss(position)
        }
    }

    interface ItemTouchHelperAdapter {
        fun onItemDismiss(position: Int)

        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    }


//ItemTouchHelper.LEFT or