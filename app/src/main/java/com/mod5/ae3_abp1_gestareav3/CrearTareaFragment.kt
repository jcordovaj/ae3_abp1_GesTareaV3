package com.mod5.ae3_abp1_gestareav3

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.util.Calendar

class CrearTareaFragment : Fragment() {
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
                    id            = taskId,
                    name          = taskName,
                    description   = taskDescription,
                    status        = taskStatus,
                    date          = taskDate,
                    time          = taskTime,
                    category      = taskCategory,
                    requiresAlarm = requiresAlarm
                )

                // Navegación (responsabilidad de la View)
                (activity as? MainActivity)?.loadFragment(VerTareasFragment())
                resetFormFields()
            }
        }
        return view
    }

    // MÉTODOS AUXILIARES (DatePicker, TimePicker, etc.), más adelante los llevaremos a otro paquete
    // *********************************************************************************************

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(),
                "Permiso de notificación concedido. Intente grabar de nuevo.",
                Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(),
                "ALERTA: Sin permiso, la alarma no funcionará.",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun resetFormFields() {
        view?.let {
            it.findViewById<EditText>(R.id.editTextTaskName)?.setText("")
            it.findViewById<EditText>(R.id.editTextTaskDescription)?.setText("")
            editTextTaskDate.setText("")
            editTextTaskTime.setText("")
            selectedDate = ""
            selectedTime = ""

            // Resetea los Spinners
            (spinnerStatus.adapter as? ArrayAdapter<String>)?.let { adapter ->
                spinnerStatus.setSelection(adapter.getPosition("Pendiente"))
            }
            (spinnerCategory.adapter as? ArrayAdapter<String>)?.let { adapter ->
                spinnerCategory.setSelection(adapter.getPosition("Evento"))
            }

            taskId    = null
            isEditing = false
        }
    }

    private fun showDatePickerDialog() {
        val calendar   = Calendar.getInstance()
        val year       = calendar.get(Calendar.YEAR)
        val month      = calendar.get(Calendar.MONTH)
        val day        = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(requireContext(),
            { _, selectedYear,
              selectedMonth, selectedDay ->
                // Guarda la fecha
                updateDateTimeFields(selectedYear,
                                    selectedMonth,
                                    selectedDay,
                                    -1,
                                    -1)
            }, year, month, day)
        datePicker.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour     = calendar.get(Calendar.HOUR_OF_DAY)
        val minute   = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(requireContext(),
            { _, selectedHour, selectedMinute ->
                updateDateTimeFields(-1, -1, -1, selectedHour, selectedMinute)
            }, hour, minute, true) // true para formato 24 horas

        timePicker.show()
    }

    private fun updateDateTimeFields(year: Int,
                                     month: Int,
                                     day: Int,
                                     hour: Int,
                                     minute: Int) {
        if (year != -1) {
            // Nos aseguramos de actualizar la fecha en formato Latino DD/MM/YYYY)
            selectedDate = String.format("%02d/%02d/%d", day, month + 1, year)
            editTextTaskDate.setText(selectedDate)
        }
        if (hour != -1) {
            // Actualiza la hora (formato HH:MM)
            selectedTime = String.format("%02d:%02d", hour, minute)
            editTextTaskTime.setText(selectedTime)
        }
    }

    companion object {
        // args keys como constantes
        const val TASK_ID_KEY          = "task_id"
        const val TASK_NAME_KEY        = "task_name"
        const val TASK_DESCRIPTION_KEY = "task_description"
        const val TASK_STATUS_KEY      = "task_status"
        const val TASK_DATE_KEY        = "task_date"
        const val TASK_TIME_KEY        = "task_time"
        const val TASK_CATEGORY_KEY    = "task_category"
        const val TASK_ALARM_KEY       = "task_alarm"

        @JvmStatic
        fun newInstanceForEditing(taskId         : String,
                                  taskName       : String,
                                  taskDescription: String,
                                  taskStatus     : String,
                                  taskDate       : String,
                                  taskTime       : String,
                                  taskCategory   : String,
                                  requiresAlarm  : Boolean): CrearTareaFragment {
            val fragment = CrearTareaFragment()
            val args     = Bundle().apply {
                putString(TASK_ID_KEY, taskId)
                putString(TASK_NAME_KEY, taskName)
                putString(TASK_DESCRIPTION_KEY, taskDescription)
                putString(TASK_STATUS_KEY, taskStatus)
                putString(TASK_DATE_KEY, taskDate)
                putString(TASK_TIME_KEY, taskTime)
                putString(TASK_CATEGORY_KEY, taskCategory)
                putBoolean(TASK_ALARM_KEY, requiresAlarm)
            }
            fragment.arguments = args
            return fragment
        }

        // Método para crear una tarea.
        fun newInstanceForCreation(): CrearTareaFragment {
            return CrearTareaFragment()
        }
    }
}