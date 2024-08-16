package com.vishu.projemanag.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vishu.projemanag.activities.TaskListActivity
import com.vishu.projemanag.databinding.ItemTaskBinding
import com.vishu.projemanag.models.Task

open class TaskListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Task>
) : RecyclerView.Adapter<TaskListItemsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(context), parent, false)
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins((15.toDp()).toPx(), 0, (40.toDp()).toPx(), 0)
        binding.root.layoutParams = layoutParams
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        if (position == list.size - 1) {
            holder.binding.tvAddTaskList.visibility = View.VISIBLE
            holder.binding.llTaskItem.visibility = View.GONE
        } else {
            holder.binding.tvAddTaskList.visibility = View.GONE
            holder.binding.llTaskItem.visibility = View.VISIBLE
        }

        holder.binding.tvTaskListTitle.text = model.title

        holder.binding.tvAddTaskList.setOnClickListener {
            holder.binding.tvAddTaskList.visibility = View.GONE
            holder.binding.cvAddTaskListName.visibility = View.VISIBLE
        }

        holder.binding.ibCloseListName.setOnClickListener {
            holder.binding.tvAddTaskList.visibility = View.VISIBLE
            holder.binding.cvAddTaskListName.visibility = View.GONE
        }

        holder.binding.ibDoneListName.setOnClickListener {
            val listName = holder.binding.etTaskListName.text.toString()
            if (listName.isNotEmpty()) {
                if (context is TaskListActivity) {
                    context.createTaskList(listName)
                }
            } else {
                Toast.makeText(context, "Please Enter List Name.", Toast.LENGTH_SHORT).show()
            }
        }

        holder.binding.ibEditListName.setOnClickListener {
            holder.binding.etEditTaskListName.setText(model.title)
            holder.binding.llTitleView.visibility = View.GONE
            holder.binding.cvEditTaskListName.visibility = View.VISIBLE
        }

        holder.binding.ibCloseEditableView.setOnClickListener {
            holder.binding.llTitleView.visibility = View.VISIBLE
            holder.binding.cvEditTaskListName.visibility = View.GONE
        }

        holder.binding.ibDoneEditListName.setOnClickListener {
            val listName = holder.binding.etEditTaskListName.text.toString()
            if (listName.isNotEmpty()) {
                if (context is TaskListActivity) {
                    context.updateTaskList(position, listName, model)
                }
            } else {
                Toast.makeText(context, "Please Enter List Name.", Toast.LENGTH_SHORT).show()
            }
        }

        holder.binding.ibDeleteList.setOnClickListener {
            alertDialogForDeleteList(position, model.title)
        }

        holder.binding.tvAddCard.setOnClickListener {
            holder.binding.tvAddCard.visibility = View.GONE
            holder.binding.cvAddCard.visibility = View.VISIBLE
        }

        holder.binding.ibCloseCardName.setOnClickListener {
            holder.binding.tvAddCard.visibility = View.VISIBLE
            holder.binding.cvAddCard.visibility = View.GONE
        }

        holder.binding.ibDoneCardName.setOnClickListener {
            val cardName = holder.binding.etCardName.text.toString()
            if (cardName.isNotEmpty()) {
                if (context is TaskListActivity) {
                    context.addCardToTaskList(position, cardName)
                }
            } else {
                Toast.makeText(context, "Please Enter Card Detail.", Toast.LENGTH_SHORT).show()
            }
        }

        holder.binding.rvCardList.layoutManager = LinearLayoutManager(context)
        holder.binding.rvCardList.setHasFixedSize(true)
        val adapter = CardListItemsAdapter(context, model.cards)
        holder.binding.rvCardList.adapter = adapter
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun Int.toDp(): Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun alertDialogForDeleteList(position: Int, title: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
            .setMessage("Are you sure you want to delete $title.")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Yes") { dialogInterface, _ ->
                dialogInterface.dismiss()
                if (context is TaskListActivity) {
                    context.deleteTaskList(position)
                }
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    class MyViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)
}
