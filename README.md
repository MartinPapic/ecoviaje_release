🏞️ EcoViaje – Aplicación Móvil de Turismo Sostenible 🌱 Descripción General

EcoViaje es una aplicación móvil multiplataforma desarrollada en Kotlin, utilizando Jetpack Compose y el patrón arquitectónico MVVM. Su propósito es fomentar el turismo sostenible mediante experiencias ecológicas, responsables y accesibles a través de una plataforma digital moderna y funcional.

La aplicación permite planificar viajes, realizar reservas y compartir experiencias sostenibles, ofreciendo una interfaz visual atractiva y fluida, basada en los principios de Material Design 3.

🎯 Objetivo General

Desarrollar una aplicación móvil que permita a los usuarios planificar y reservar experiencias turísticas sostenibles, incorporando una arquitectura escalable, funcionalidades modernas y una interfaz visual intuitiva.

⚙️ Objetivos Específicos

Diseñar interfaces adaptativas con Jetpack Compose aplicando principios de usabilidad, jerarquía visual y retroalimentación clara.

Implementar validaciones robustas con ViewModel y StateFlow, garantizando precisión y respuestas inmediatas en la interfaz.

Aplicar el patrón arquitectónico MVVM, asegurando separación entre la lógica de negocio y la vista.

Gestionar la navegación mediante NavHost y NavController, garantizando flujos coherentes entre pantallas.

Integrar persistencia local con DataStore y Room Database para asegurar almacenamiento confiable de datos.

Incorporar recursos nativos del dispositivo (GPS, cámara, galería) para potenciar la experiencia del usuario.

Validar integraciones externas y pagos, garantizando un flujo seguro y eficiente antes de la publicación.

Organizar el desarrollo colaborativo con GitHub y Trello, priorizando calidad, trazabilidad y eficiencia.

🧩 Arquitectura y Tecnologías Componente Tecnología / Herramienta Propósito Lenguaje Kotlin Desarrollo nativo para Android Framework UI Jetpack Compose Diseño moderno, declarativo y adaptativo Patrón Arquitectónico MVVM Separación de lógica y vista Gestión de Estado ViewModel + StateFlow + asState() Control reactivo de datos y validaciones Navegación NavHost + NavController Flujo funcional entre pantallas Almacenamiento Local DataStore Guardado de preferencias y configuraciones Persistencia de Datos Room Database Gestión estructurada de datos locales Control de Versiones Git + GitHub Colaboración y control del código Gestión de Proyecto Trello Planificación y seguimiento ágil Diseño Visual Material Design 3 Coherencia, accesibilidad y estética Distribución Google Play Console Publicación y distribución oficial 👥 Historias de Usuario (Resumen)

Como usuario, deseo una interfaz moderna, adaptable y fluida.

Como desarrollador, quiero mantener un código limpio y escalable con Kotlin y MVVM.

Como usuario, quiero guardar mis configuraciones y acceder a datos sin conexión.

Como equipo de desarrollo, queremos usar GitHub y Trello para trabajar de forma organizada.

Como gestor de proyecto, deseo visualizar y supervisar el avance mediante tableros colaborativos.

📱 Flujo de Pantallas

Login / Registro – Validación de credenciales y autenticación con backend.

Pantalla Principal – Punto central para navegar entre funcionalidades.

Planificación de Viajes – Búsqueda, fechas y filtros sostenibles.

Reservas – Confirmación de actividades y pagos validados.

Intercambio de Experiencias – Publicación de fotos, comentarios y vivencias.

Configuración – Preferencias del usuario guardadas con DataStore.

🧠 Lógica de Negocio

El ViewModel actúa como núcleo de la aplicación, gestionando:

DataStore: preferencias locales, tokens, banderas de sesión.

Room Database: almacenamiento estructurado de viajes y reservas.

Supabase: autenticación, base de datos y reglas de seguridad.

Todo el flujo garantiza un manejo coherente de estado, validaciones instantáneas y persistencia entre sesiones.

🧪 Requerimientos Funcionales y Criterios de Aceptación

UI construida 100% con Jetpack Compose, adaptativa y responsiva.

Ningún archivo Java presente en el proyecto.

MVVM correctamente implementado, sin lógica de negocio en la vista.

Sin pérdida de estado en navegación.

Persistencia validada con Room y DataStore.

Repositorio Git estructurado con ramas, commits descriptivos y pull requests.

Interfaz visual coherente con Material Design 3.

Publicación exitosa en Google Play Console.

🗂️ Gestión del Proyecto

Control de versiones: Git + GitHub

Organización de tareas: Trello

Colaboración: revisiones semanales y reportes periódicos

Estandarización: commits claros, ramas por funcionalidad, revisión cruzada de código

🚀 Resultados Esperados

Aplicación estable, escalable y fácil de mantener.

Experiencia de usuario fluida con navegación sin errores.

Validaciones precisas y feedback inmediato en la interfaz.

Integración total con almacenamiento local y backend.

Publicación en Google Play, impulsando el turismo sostenible mediante tecnología.

🛠️ Configuración y Ejecución (Para Desarrolladores)

1. **Requisitos:**
   - Android Studio Ladybug o superior.
   - JDK 11 o superior.

2. **Clonar y Abrir:**
   - Clonar este repositorio.
   - Abrir en Android Studio y esperar la sincronización de Gradle.

3. **Configuración de Backend (Supabase):**
   - El proyecto utiliza **Supabase** para autenticación y base de datos.
   - **IMPORTANTE:** Las credenciales se gestionan de forma segura y NO están en el código fuente.
   - Debes agregar tus claves en el archivo `local.properties` (en la raíz del proyecto):
     ```properties
     SUPABASE_URL=https://tu-proyecto.supabase.co
     SUPABASE_KEY=tu-anon-key-jwt
     ```
   - El proyecto leerá estas variables automáticamente al compilar.

4. **Ejecutar (Debug):**
   - Seleccionar la configuración `app` y ejecutar en un emulador o dispositivo físico.
   - La app se conectará automáticamente a la instancia de Supabase configurada.

5. **Generar Release (Opcional):**
   - Se requiere el archivo `release-key.jks` y `keystore.properties` (no incluidos en el repositorio por seguridad).
   - Consultar `deployment_guide.md` para más detalles.

🏁 Conclusión

EcoViaje representa la unión entre tecnología, sostenibilidad y diseño. Mediante el uso de Kotlin, Jetpack Compose y MVVM, el proyecto sienta las bases para un desarrollo moderno, colaborativo y escalable, al servicio de un propósito mayor: fomentar el turismo responsable y consciente mediante innovación digital.