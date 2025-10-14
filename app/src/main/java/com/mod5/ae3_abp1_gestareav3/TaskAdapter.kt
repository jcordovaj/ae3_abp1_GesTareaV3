package com.mod5.ae3_abp1_gestareav3

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

data class TaskAdapter()

class TaskAdapter(
    // La lista ahora es 'var' para poder actualizarla
    private var tasks: List<Task>,
    private val onItemClick: (Task) -> Unit,
    // NUEVO: Función lambda para manejar el evento de eliminación
    private val onDeleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // ... (onCreateViewHolder, getItemCount, igual) ...

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        // Se pasa la lambda de eliminación al ViewHolder
        holder.bind(task, onItemClick, onDeleteClick, position + 1)
    }

    /**
     * Método para actualizar la lista reactivamente, llamado desde el LiveData.
     */
    fun updateTasks(newTasks: List<Task>) {
        this.tasks = newTasks
        notifyDataSetChanged()
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ... (resto de referencias a TextViews igual) ...
        // NUEVO: Referencia al botón de eliminación
        private val buttonDelete: Button = itemView.findViewById(R.id.buttonDeleteTask)

        fun bind(task: Task, onItemClick: (Task) -> Unit, onDeleteClick: (Task) -> Unit, ordinalId: Int) {
            // ... (resto de la asignación de textos igual) ...

            itemView.setOnClickListener { onItemClick(task) }

            // Listener para el botón de eliminación
            buttonDelete.setOnClickListener { onDeleteClick(task) }
        }
    }
}