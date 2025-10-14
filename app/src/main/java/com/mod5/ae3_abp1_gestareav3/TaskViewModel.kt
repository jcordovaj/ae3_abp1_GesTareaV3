package com.mod5.ae3_abp1_gestareav3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

// ViewModel para gestionar las tareas. Usa LiveData para notificar a la UI.
class TaskViewModel(application: Application) : AndroidViewModel(application) {

    // Inicialización del Repository
    private val repository = TaskRepository(application)

    // LiveData principal que contiene todas las tareas.
    private val _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>> get() = _allTasks

    // LiveData para notificar mensajes de estado a la UI (Toast).
    private val _statusMessage = MutableLiveData<String?>()
    val statusMessage: LiveData<String?> get() = _statusMessage

    init {
        loadTasks() // Carga inicial
    }

    /**
     * Carga las tareas desde el repositorio y actualiza el LiveData.
     * Este es el núcleo del patrón OBSERVADOR.
     */
    fun loadTasks() {
        // Ejecuta la lectura de datos en un hilo de fondo (IO)
        viewModelScope.launch(Dispatchers.IO) {
            val tasks = repository.readAllTasks()
            _allTasks.postValue(tasks) // postValue para actualizar LiveData desde un hilo de fondo
        }
    }

    fun saveOrUpdateTask(
                            id           : String?,
                            name         : String,
                            description  : String,
                            status       : String,
                            date         : String,
                            time         : String,
                            category     : String,
                            requiresAlarm: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val isEditing = id != null
            // Usa UUID para ID si es nueva tarea
            val taskId = id ?: UUID.randomUUID().toString()
            val task = Task(taskId, name, description, status, date, time, category, requiresAlarm)

            val success = if (isEditing) {
                repository.updateTaskInCSV(task)
            } else {
                repository.saveTaskToCSV(task)
            }

            if (success) {
                _statusMessage.postValue("Tarea ${if (isEditing) "actualizada" else "guardada"} correctamente")
                loadTasks() // Recarga para que los observadores (Fragments) se actualicen.
            } else {
                _statusMessage.postValue("Error al guardar la tarea")
            }
        }
    }

    // Marca una tarea como completada (actualiza el estado).
    fun markTaskAsCompleted(task: Task) {
        val completedTask = task.copy(status = "Completada")
        viewModelScope.launch(Dispatchers.IO) {
            val success = repository.updateTaskInCSV(completedTask)
            if (success) {
                _statusMessage.postValue("Tarea marcada como 'Completada'")
                loadTasks() // Recarga para actualizar la vista
            } else {
                _statusMessage.postValue("Error al marcar la tarea")
            }
        }
    }

    // NUEVO: Llama al repositorio para eliminar una tarea.
    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = repository.deleteTaskById(task.id)
            if (success) {
                _statusMessage.postValue("Tarea '${task.name}' eliminada (persistencia en CSV actualizada)")
                loadTasks() // Recarga para que la lista se actualice automáticamente (OBSERVADOR).
            } else {
                _statusMessage.postValue("Error al eliminar la tarea")
            }
        }
    }

    // Limpia el mensaje de estado después de ser consumido por la UI.
    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}