package com.mod5.ae3_abp1_gestareav3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var mainContentLayout: LinearLayout
    // Se crea el ViewModel a nivel de Activity para compartirlo entre Fragments
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el ViewModel
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        // Carga el layout del splash
        setContentView(R.layout.main_splash)

        val buttonStartApp: Button = findViewById(R.id.buttonStartApp)
        buttonStartApp.setOnClickListener {
            setupMainLayout()
        }
    }

    private fun setupMainLayout() {
        setContentView(R.layout.main)
        // ... (resto de la inicialización del layout igual) ...

        // **DEMOSTRACIÓN DE OBSERVADOR 1/2**: Observa mensajes del ViewModel
        taskViewModel.statusMessage.observe(this, { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                taskViewModel.clearStatusMessage() // Importante: limpia el mensaje después de mostrarlo
            }
        })

        // ... (resto de la configuración de navegación igual) ...
    }

    fun startTaskEdit(task: Task) {
        // ... (método startTaskEdit igual) ...
    }

    fun loadFragment(fragment: Fragment) {
        // ... (método loadFragment igual) ...
    }

    // ELIMINAMOS el método resetTaskFile() de la Activity. La lógica de persistencia está en el Repository.
}