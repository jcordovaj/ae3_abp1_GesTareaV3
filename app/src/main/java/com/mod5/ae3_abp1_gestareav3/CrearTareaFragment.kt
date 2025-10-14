package com.mod5.ae3_abp1_gestareav3

import androidx.lifecycle.ViewModelProvider
import com.perasconmanzanas.gestareav3.viewmodel.TaskViewModel

class CrearTareaFragment : Fragment() {

    // ... (Propiedades y variables de estado, igual) ...

    // Referencia al ViewModel
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.crear_tarea, container, false)

        // Inicializa el ViewModel
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        // ... (Resto de inicialización de vistas, spinners, pickers, argumentos, igual) ...

        val buttonGrabar: Button = view.findViewById(R.id.buttonSaveTask)

        // Listener botón grabar (Refactorizado)
        buttonGrabar.setOnClickListener {
            // ... (Captura de campos, validación de permisos y validación de campos, igual) ...

            // Si la validación es exitosa, llama al ViewModel
            if (true /* asumiendo que la validación fue exitosa */) {
                // **Llama al ViewModel para guardar/actualizar**
                taskViewModel.saveOrUpdateTask(
                    id = taskId,
                    name = taskName,
                    description = taskDescription,
                    status = taskStatus,
                    date = taskDate,
                    time = taskTime,
                    category = taskCategory,
                    requiresAlarm = requiresAlarm
                )

                // Navegación (responsabilidad de la View)
                (activity as? MainActivity)?.loadFragment(VerTareasFragment())
                resetFormFields()
            }
        }
        return view
    }

    // ELIMINAMOS los métodos saveTaskToCSV() y updateTaskInCSV() de este fragment.

    // ... (Resto de métodos auxiliares, igual) ...
}