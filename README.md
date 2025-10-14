# CASO AE2-ABPRO1: DESARROLLO AGENDA V2 📅 - PERSISTENCIA, PERMISOS E INTEGRACIÓN

<p float="center">
  <img src="scrapbook/perasconmanzanas_icon.png" alt="Logo" width="200"/>
</p>

Este proyecto es una mini aplicación nativa para Android, desarrollada en Kotlin. Su objetivo es ilustrar la gestión completa del ciclo de vida de una tarea, incluyendo la persistencia local de datos (CSV), cambio de estado, filtrado dinámico, la gestión de permisos sensibles en tiempo de ejecución (Notifications y Calendar), y la interoperabilidad de la aplicación con servicios del sistema Android (BottomNavigationView, Fragments, y el Calendar Provider).

**Nota**: Se reutiliza el código del Caso AE1-ABPRO1 Agenda V1.

## Requerimientos del Caso

1. Registrar eventos con título, descripción y fecha/hora de recordatorio.
2. Liste los eventos registrados aplicando filter y sorting sobre los datos.
3. Gestionar correctamente el ciclo de vida de Activities y Fragments
4. Solicitar permisos sensibles (como notificaciones o calendario si es necesario) de forma segura y solo cuando se requiera
5. Agregue y gestione Fragments dinámicamente con paso de parámetros
6. Implemente un RecyclerView para mostrar las tareas creadas
7. Utilice Intents y Bundles para pasar datos entre pantallas o componentes
8. Proporcione feedback visual al usuario sobre los cambios en el ciclo de vida (por ejemplo, mediante logs o toasts)

## Requerimientos Técnicos

1. Mostrar el cambio de estado de la Activity y los Fragments con Toasts
2. Manejar la recreación de la Activity ante cambios de configuración (rotación de pantalla)
3. Crear y añadir Fragments de forma dinámica usando método de factoría y pasándoles parámetros
4. Permisos Sensibles
5. Solicitar permisos para notificaciones o calendario solo si se usa la función de recordatorio
6. Explicar cómo se integra esta solicitud dentro del ciclo de vida de la Activity
7. Implementar un Adapter personalizado para mostrar la lista de tareas
8. Explicar brevemente la razón del uso de RecyclerView en lugar de otros adaptadores tradicionales
9. Crear un Intent explícito para ir a una pantalla de edición de tareas
10. Utilizar Bundle para pasar datos entre Activities o Fragments
11. Implementar un flujo de startActivityForResult() para editar una tarea y recibir el resultado (DEPRECADO). Se reemplazó por Transacción de Fragmentos y Persistencia.

## Características y Usabilidad

1. Manejo de ClassCastException y Tipado estricto en la interfaz de usuario.
2. Uso de registerForActivityResult para la gestión moderna de permisos de Android 13+ (Tiramisu).
3. Navegación unificada: La lógica de navegación ha sido centralizada en la "Barra de Navegación Inferior" (BottomNavigationView).
4. Gestión de tareas/eventos: Permite registrar y modificar eventos o recordatorios, capturando información esencial como Nombre, Descripción, Fecha, Hora, Estado, Categoría, y si requiere Alarma.
5. Recordatorios Funcionales (MVP): Luego de probar varias opciones, la aplicación delega la creación de recordatorios y alarmas al Calendar Provider de Android, garantizando que las notificaciones se disparen correctamente y eviten restricciones de ahorro de batería (Doze).
6. Permisos Seguros: Se solicita el permiso de Notificaciones (POST_NOTIFICATIONS) solo cuando el usuario activa la alarma, utilizando el contrato ActivityResultContracts.RequestPermission().
7. Persistencia local: La aplicación utiliza un archivo 'tareas.csv' como objeto persistente, para el almacenamiento y la manipulación de registros.

## Tecnologías usadas 🛠️

- IDE: Android Studio (Narwhal 3, basado en IntelliJ IDEA)
- Plataforma: Android Nativo
- SDK mínimo: 29
- SDK Target: 36
- Kotlin: 1.9.22
- Java: 21
- UI/Navegación: Activity, Fragment y BottomNavigationView
- Almacenamiento: Archivos CSV (ubicados en getExternalFilesDir(null), en el dispositivo)
- APIs de Sistema: AlarmManager, CalendarContract (Calendar Provider).
- Otras tecnologías: Git, Github, Github Desktop.

## Estructura de Datos (CSV) 💾

El archivo tareas.csv se compone de 8 atributos, con la siguiente estructura:

| Campo       | Descripción                                                 | Tipo de Dato |
| ----------- | ----------------------------------------------------------- | ------------ |
| ID          | UUID                                                        | String       |
| Título      | Título de la actividad                                      | String       |
| Descripción | Breve descripción                                           | String       |
| Estado      | Estado actual (Pendiente, Completada, Eliminada)            | String       |
| Fecha       | Fecha programada                                            | String       |
| Hora        | Hora programada                                             | String       |
| Categoría   | Clasificación del registro (Evento, recordatorio, Tarea)    | String       |
| Alarma      | Switch que activa o desactiva alarma recordatorio (boolean) | String       |

## Funcionamiento de la Aplicación

La aplicación es un prototipo para la gestión de eventos o tareas (recordatorios). El flujo base es el siguiente:

1. Pantalla de inicio: Al abrir la aplicación, se muestra una pantalla de bienvenida.
2. Vista Agenda: Presenta un listado de los eventos pendientes. Cada elemento puede ser accesado y modificado para cambiar su estado.
3. Crear evento (Flujo con Alarma):
   1. El usuario presiona el botón "Agregar".
   2. Se abre el formulario que incluye el switch "Alarma".
   3. Al presionar grabar y si el switch "Alarma" está activo:
      1. La aplicación solicita el permiso de Notificaciones (POST_NOTIFICATIONS).
   4. La aplicación guarda la tarea en el CSV.
   5. La aplicación lanza un Intent que abre la aplicación nativa de Calendario del dispositivo, precargando los campos del evento, delegando la gestión del recordatorio al sistema operativo.
4. Barra de Navegación: La barra de navegación inferior permite al usuario moverse fácilmente entre las vistas de "Agenda" y "Crear Evento".

Capturas de Pantalla

<p float="left">
  <img src="scrapbook/pantalla_inicial.png" alt="Pantalla inicial" width="150"/>  
  <img src="scrapbook/agregar_evento.png" alt="Agregar Actividad" width="150"/>
  <img src="scrapbook/advertencia_notificaciones.png" alt="Notificación" width="150"/>
  <img src="scrapbook/date_picker.png" alt="Date picker" width="150"/>
  <img src="scrapbook/lista_actividades.png" alt="Lista de actividades" width="150"/>
  <img src="scrapbook/permiso_notificaciones.png" alt="Notificaciones" width="150"/>
  <img src="scrapbook/toast_advertencia_notificaciones.png" alt="Advertencia" width="150"/>
</p>

## Guía de Ejecución del Proyecto

**Para ejecutar este proyecto en tu entorno de desarrollo, siga estos 'quick steps':**

1.**Clonar el Repo:** Clona el proyecto en su máquina local.

2.**Abrir en Android Studio:** Abra la carpeta del proyecto con Android Studio. El IDE detectará automáticamente la configuración de Gradle.

3.**Sincronizar Gradle:** Haz clic en el botón "Sync Now" si Android Studio te lo solicita. Esto descargará todas las dependencias necesarias.

4.**Ejecutar:** Conecta un dispositivo Android físico o inicia un emulador. Luego, haz clic en el botón "Run 'app'" (el ícono de la flecha verde) para desplegar la aplicación.

**Para ejecutar este proyecto en tu celular, sigue estos 'quick steps':**

1.**Copiar la APK:** Copia la aplicación (APK) en tu celular.

2.**Instalar:** Instala la aplicación, salta los avisos de advertencia, es normal si la aplicación no ha sido productivizada la plataforma de Android.

3.**Abrir la App:** Haz doble clic en el ícono "Agenda".

4.**Recorrer las opciones:** Cliquea en las opciones y podrás acceder al listado de eventos, editar cada evento, crear nuevos eventos, regresando a cualquier punto de la app.

## Instalación y Configuración

a. **Clonar el repositorio:**

```bash


https://github.com/jcordovaj/ae2_abpro1_AgendaV2.git


```

b. **Abrir el Proyecto en Android Studio:**

b.1. Abrir Android Studio.

b.2. En la pantalla de bienvenida, seleccionar **"Open an existing Android Studio project"** (Abrir un proyecto de Android Studio existente).

b.3. Navegar a la carpeta donde se clonó el repositorio y seleccionarla. Android Studio detectará automáticamente el proyecto de Gradle y comenzará a indexar los archivos.

c. **Sincronizar Gradle:**

c.1. Este es el paso más importante. Después de abrir el proyecto, Android Studio intentará sincronizar la configuración de Gradle. Esto significa que descargará todas las librerías, dependencias y plugins necesarios para construir la aplicación. Normalmente, una barra de progreso se mostrará en la parte inferior de la consola de Android Studio con un mensaje como **"Gradle Sync in progress"**.

c.2. Si no se inicia, o si el proceso falla, intente con el botón **"Sync Project with Gradle Files"** en la barra de herramientas. Es el icono con el **"elefante" de Gradle**. Eso forzará la sincronización.

c.3. Esperar que el proceso de sincronización termine. De haber errores, puede ser por problemas en la configuración de Android u otros conflictos, la aplicación debe descargar lo que requiera y poder ser ejecutada "AS-IS".

d. **Configurar el Dispositivo o Emulador:**

Para ejecutar la aplicación, se requiere un dispositivo Android, puedes usarse el emulador virtual o un dispositivo físico.

d.1. Emulador: En la barra de herramientas, haga click en el botón del "AVD Manager" (Android Virtual Device Manager), que es el icono de un teléfono móvil con el logo de Android. Desde ahí, puedes crear un nuevo emulador con la versión de Android que prefiera (Nota: Debe considerar que cada celular emulado, puede requerir más de 1GB de espacio en disco y recursos de memoria).

d.2. Dispositivo físico: Conecte su teléfono Android a la computadora con un cable USB (también puede ser por WI-FI). Asegúrese de que las **Opciones de desarrollador y la Depuración por USB** estén habilitadas en su dispositivo. Consulte a su fabricante para activar estas opciones.

e. **Ejecutar la aplicación:**

e.1. Seleccione el dispositivo o emulador deseado en la barra de herramientas del emulador.

e.2. Haga click en el botón "Run 'app'" (el triángulo verde en la parte superior, o vaya al menu "RUN") para iniciar la compilación y el despliegue de la aplicación, puede tardar algunos minutos, dependiendo de su computador.

e.3. Si todo ha sido configurado correctamente, la aplicación se instalará en el dispositivo y se iniciará automáticamente, mostrando la pantalla de inicio.

## Contribuciones (Things-To-Do)

Se puede contribuir reportando problemas o con nuevas ideas, por favor respetar el estilo de programación y no subir código basura. Puede utilizar: forking del repositorio, crear pull requests, etc. Toda contribución es bienvenida.

## Licencia

Proyecto con fines educativos, Licencia MIT
