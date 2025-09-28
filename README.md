# Sistema de Gestión de Becas (GUI)

Este proyecto es un sistema automatizado de gestión de becas estudiantiles, desarrollado como parte de la asignatura de Programación Avanzada en la Pontificia Universidad Católica de Valparaíso. Con una interfaz gráfica de usuario (GUI) desarrollada en Java Swing, permite administrar estudiantes, becas y solicitudes de forma visual e intuitiva, facilitando el proceso completo de asignación de becas.

## Descripción

El sistema de gestión de becas ofrece una administración completa de estudiantes, becas disponibles y solicitudes de becas, manteniendo las operaciones CRUD (Crear, Leer, Actualizar y Eliminar) directamente desde una interfaz gráfica amigable. La aplicación gestiona el ciclo completo de solicitudes desde su creación hasta su aprobación o rechazo, almacenando los datos de forma persistente en archivos CSV y generando reportes detallados en múltiples formatos.

## Funcionalidades

### Gestión de Estudiantes con interfaz gráfica:
- Agregar, modificar y eliminar estudiantes de manera intuitiva a través de ventanas de diálogo
- Visualizar listas detalladas de estudiantes con toda la información relevante
- Validación de datos para evitar duplicados por ID
- Búsqueda rápida de estudiantes por ID

### Administración de Becas:
- Crear y gestionar diferentes tipos de becas con criterios específicos
- Editar criterios de becas existentes
- Eliminar becas del sistema con validaciones de seguridad
- Catálogo inicial con 8 tipos de becas predefinidas

### Sistema de Solicitudes:
- Creación de solicitudes vinculando estudiantes con becas específicas
- Gestión del estado de solicitudes (Pendiente, Aprobada, Rechazada)
- Prevención de solicitudes duplicadas por estudiante
- Búsqueda y eliminación de solicitudes específicas
- Filtrado de solicitudes por estado

### Herramientas de Análisis y Reportes:
- Generación de reportes completos en formato TXT con encoding UTF-8
- Exportación de datos a archivos CSV para análisis externo
- Reportes estadísticos por estado de solicitudes
- Listado de estudiantes con sus solicitudes activas
- Filtrado de estudiantes por número mínimo de solicitudes

### Persistencia de Datos:
- Los datos se almacenan de manera persistente en archivo CSV (data.csv)
- Carga automática de datos al iniciar la aplicación
- Guardado automático al cerrar con confirmación del usuario
- Recuperación de datos entre sesiones

## Estructura del Proyecto

El proyecto está compuesto por varias clases que organizan la lógica de la aplicación y la interacción con la GUI:

### Interfaz Gráfica:
- **MenuGUI**: Ventana principal con paneles organizados por funcionalidad (Estudiantes, Solicitudes, Becas, Sistema)
- **TextAreaOutputStream**: Clase utilitaria para redirigir la salida del sistema a la interfaz gráfica
- Uso de JFrame, JDialog y componentes Swing para una experiencia de usuario fluida

### Clases del Modelo de Datos:
- **Persona**: Clase base con atributos comunes (nombre, ID)
- **Estudiante**: Extiende Persona, modela a los candidatos con sus solicitudes asociadas
- **Beca**: Modela las becas disponibles con tipo y criterios específicos  
- **SolicitudBeca**: Extiende Registro, representa las solicitudes con estado y relaciones
- **Registro**: Clase base para elementos que pueden generar resúmenes

### Gestión Centralizada:
- **SistemaDeGestion**: Clase principal que administra todas las operaciones del sistema
- Manejo de colecciones con HashMap para estudiantes y ArrayList para becas y solicitudes
- Implementación de operaciones CRUD para todas las entidades
- Manejo de excepciones personalizadas

### Excepciones Personalizadas:
- **EstudianteNotFoundException**: Para casos donde no se encuentra un estudiante
- **BecaNotFoundException**: Para casos donde no se encuentra una beca específica

## Almacenamiento y Persistencia

### Almacenamiento en CSV:
Los datos de estudiantes, becas y solicitudes se almacenan en un archivo CSV unificado (data.csv) con formato estructurado:
- Líneas tipo "ESTUDIANTE;nombre;id"  
- Líneas tipo "BECA;tipo;criterio"
- Líneas tipo "SOL;idEstudiante;tipoBeca;estado"

### Reportes en múltiples formatos:
- **Reporte TXT**: Archivo detallado con información completa de todos los elementos del sistema
- **Reporte CSV**: Exportación estructurada para análisis en herramientas externas
- **Reportes estadísticos**: Conteos y análisis de solicitudes por estado

## Diseño Conceptual

### Uso de Colecciones Eficientes:
- **HashMap<String, Estudiante>**: Acceso rápido a estudiantes por ID (clave)
- **List<Beca>**: Gestión ordenada de becas disponibles
- **List<SolicitudBeca>**: Manejo de solicitudes con relaciones bidireccionales

### Programación Orientada a Objetos:
- **Herencia**: Estudiante extiende Persona, SolicitudBeca extiende Registro
- **Polimorfismo**: Sobrescritura de métodos toString() y descripcion()
- **Encapsulamiento**: Atributos privados con métodos getter/setter
- **Sobrecarga**: Múltiples versiones de métodos como crearSolicitud() y mostrarSolicitudes()

### Interfaz gráfica con Swing:
- Layout organizado con BorderLayout y BoxLayout
- Paneles temáticos con TitledBorder para mejor organización
- Redireccionamiento de salida del sistema a JTextArea
- Confirmaciones de usuario para operaciones críticas

## Características de Datos Iniciales

El sistema se inicializa con 8 tipos de becas predefinidas:
1. **Beca Académica** - Promedio superior a 6.0
2. **Beca Deportiva** - Excelente rendimiento deportivo  
3. **Beca Cultural** - Participación en actividades culturales
4. **Beca Socioeconómica** - Situación socioeconómica vulnerable
5. **Beca Liderazgo** - Destacado liderazgo estudiantil
6. **Beca Innovación** - Proyectos de investigación o innovación
7. **Beca Voluntariado** - Horas certificadas de servicio comunitario
8. **Beca Excelencia** - Rendimiento académico sobresaliente en toda la carrera

## Requisitos

- **Java 8 o superior**
- Sistema operativo compatible con Swing (Windows, macOS, o Linux con entorno de escritorio)

## Instrucciones de Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone <url-del-repositorio>
   ```

2. **Navegar al directorio del proyecto:**
   ```bash
   cd <nombre-del-directorio>
   ```

3. **Compilar el proyecto:**
   ```bash
   javac *.java
   ```

4. **Ejecutar la aplicación:**
   ```bash
   java Menu
   ```

## Buenas Prácticas Implementadas

### Diseño de Software:
- **Separación de responsabilidades**: Lógica de negocio separada de la interfaz gráfica
- **Manejo de errores**: Excepciones personalizadas y validaciones en cada operación
- **Experiencia de usuario**: Confirmaciones para operaciones destructivas
- **Código limpio**: Métodos helper para reducir duplicación de código

### Validación de Datos:
- Verificación de campos vacíos antes de procesar
- Prevención de datos duplicados (IDs de estudiantes, tipos de becas)
- Validación de existencia antes de modificaciones o eliminaciones
- Manejo robusto de entrada de usuario con trim() y validaciones

### Control de Versiones:
- Estructura modular preparada para versionado con Git
- Documentación detallada de funcionalidades
- Código comentado para facilitar mantenimiento

## Futuras Mejoras Propuestas

### Funcionalidades Avanzadas:
- **Sistema de matching automático**: Algoritmo para emparejar estudiantes con becas según criterios
- **Gestión de competencias**: Sistema para evaluar habilidades específicas de estudiantes
- **Notificaciones**: Sistema de alertas para deadlines y actualizaciones de estado
- **Dashboard analítico**: Gráficos y métricas del rendimiento del sistema

### Mejoras Técnicas:
- **Base de datos**: Migración de CSV a base de datos relacional (H2, SQLite)
- **API REST**: Servicios web para integración con otros sistemas
- **Autenticación**: Sistema de usuarios y roles (administrador, estudiante)
- **Backup automático**: Respaldos periódicos de datos críticos

### Interfaz de Usuario:
- **Diseño responsivo**: Adaptación a diferentes resoluciones de pantalla
- **Temas visuales**: Modo oscuro y personalización de colores
- **Tablas interactivas**: Ordenamiento y filtrado avanzado en visualizaciones
- **Asistentes paso a paso**: Guías para procesos complejos

---

**Pontificia Universidad Católica de Valparaíso**  
**Facultad de Ingeniería - Escuela de Ingeniería Informática**

### Integrantes del proyecto:
- **Javier Poblete**
- **Joaquin Perez**  
- **Gonzalo Soto**
