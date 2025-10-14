package com.mod5.ae3_abp1_gestareav3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class VerTareasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewEmptyMessage: TextView
    private lateinit var taskAdapter : TaskAdapter
    // Referencia al ViewModel compartido con la Activity
    private lateinit var taskViewModel: TaskViewModel

    // allTasks: List<Task> ya no es necesaria, el ViewModel la gestiona.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.ver_tareas, container, false)

        // Inicializa el ViewModel
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        recyclerView = view.findViewById(R.id.recyclerViewTasks)
        textViewEmptyMessage = view.findViewById(R.id.textViewEmptyListMessage)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicializa el adaptador
        taskAdapter = TaskAdapter(
            tasks = emptyList(), // Empieza vacío
            onItemClick = { task ->
                (activity as? MainActivity)?.startTaskEdit(task)
            },
            onDeleteClick = { task ->
                // Muestra diálogo de confirmación antes de eliminar
                confirmAndDeleteTask(task)
            }
        )
        recyclerView.adapter = taskAdapter

        // **DEMOSTRACIÓN DE OBSERVADOR 2/2**: El LiveData activa la actualización de la UI.
        taskViewModel.allTasks.observe(viewLifecycleOwner, { allTasks ->
            // Filtra solo las tareas 'Pendientes' para la vista (Lógica de presentación)
            val pendingTasks = allTasks.filter { it.status == "Pendiente" }

            // Actualiza la lista en el adaptador y redibuja la lista (REACTIVIDAD)
            taskAdapter.updateTasks(pendingTasks)

            // Control de Visibilidad (Lógica de presentación)
            if (pendingTasks.isEmpty()) {
                textViewEmptyMessage.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                textViewEmptyMessage.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })

        return view
    }

    // Muestra un diálogo de confirmación antes de eliminar tarea.
    private fun confirmAndDeleteTask(task: Task) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Tarea")
            .setMessage("¿Deseas eliminar permanentemente la tarea '${task.name}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                // Llama al ViewModel para ejecutar la eliminación (Lógica de negocio)
                taskViewModel.deleteTask(task)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}