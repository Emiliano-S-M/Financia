# 🏗️ Arquitectura del Sistema

## 📌 Visión general

El sistema **Financia** está diseñado como una aplicación de escritorio basada en una arquitectura por capas, con el objetivo de garantizar mantenibilidad, escalabilidad y claridad en la organización del código.

Se implementa una separación de responsabilidades que permite desacoplar la lógica de negocio de la interfaz gráfica, facilitando futuras mejoras y refactorizaciones.

---

## 🧱 Estructura general

La aplicación está organizada en tres capas principales:

- Controlador
- Servicio
- Modelo

---

## 🔄 Flujo de la aplicación

1. El usuario interactúa con la interfaz gráfica (JavaFX).
2. El controlador captura el evento.
3. El controlador delega la operación al servicio.
4. El servicio ejecuta la lógica de negocio.
5. El modelo representa y almacena los datos.
6. Se actualiza la interfaz con los nuevos valores.

---


## 🧩 Capas del sistema

### 🎮 Controladores (Control)

Responsables de gestionar la interacción con la interfaz gráfica.

**Responsabilidades:**
- Manejo de eventos (botones, formularios)
- Validación básica de datos de entrada
- Comunicación con la capa de servicios

**Ejemplo:**
- Registro de movimientos
- Eliminación de registros
- Actualización de tablas

---

### ⚙️ Servicios (Servicio)

Contienen la lógica de negocio del sistema.

**Responsabilidades:**
- Procesamiento de datos
- Aplicación de reglas de negocio
- Coordinación entre componentes

**Ventajas:**
- Reutilización de lógica
- Facilidad de pruebas
- Bajo acoplamiento

---

### 🧾 Modelos (Model)

Representan las entidades del sistema.

**Responsabilidades:**
- Definición de estructuras de datos
- Representación de movimientos financieros
- Encapsulación de atributos

**Ejemplo:**
- Movimiento (tipo, monto, categoría, fecha)

---

## 🧠 Decisiones de arquitectura

Durante el desarrollo se tomaron las siguientes decisiones clave:

- Se optó por una arquitectura en capas para evitar acoplamiento entre interfaz y lógica de negocio. Aunque aún faltan por pulir varios puntos, ya que esto es un MVP se planea para un siguiente version hacer un desacople más claro.
- Se priorizó una implementación funcional inicial antes de aplicar patrones más complejos.
- Se diseñó el sistema pensando en una futura migración a un patrón MVC completo.
- Se procuró mantener la lógica de negocio fuera de los controladores para mejorar mantenibilidad.

---

## 🚀 Evolución de la arquitectura

### 🔹 Versión 1.0.0
- Implementación inicial funcional
- Registro y visualización de movimientos
- Cálculo de balance en tiempo real
- Lógica parcialmente acoplada a controladores

### 🔹 Versión 1.0.1
- Refactorización ligera de controladores
- Separación inicial de responsabilidades
- Creación de utilitarios básicos (formato de moneda, validaciones, saludo dinámico)

### 🔹 Versión 1.1.0
- Refactorización más profunda de controladores
- Introducción de interfaces de servicio (ICategoriaServicio, IMovimientoServicio)
- Modularización de servicios y repositorios
- Documentación inicial en clases principales

### 🔹 Versión 1.1.1
- Documentación completa en controladores, modelos y repositorios
- Migración de dependencias a interfaces para mayor desacoplamiento
- Homogeneización de estilo con JavaDoc
- Confirmación de separación de responsabilidades entre capas
- Versión estable sin cambios funcionales

### 🔹 Versión 1.2.0 (Pre-release)
- Implementación de filtros dinámicos en la tabla de movimientos:
  - Por tipo y categoría 
  - Búsqueda libre con campo de texto 
  - Filtros de fecha flexibles con tres ComboBox (Año, Mes, Día)
- Nuevos componentes visuales:
  - Botones de eliminación con ícono por fila 
  - Colores dinámicos en columna de monto según tipo de movimiento
- Nuevas utilidades en carpeta Utils:
  - `FiltroFechaUtil` para inicialización y lógica de filtros de fecha 
  - `TablaColorUtil` para reglas de color en columnas

- Modularización de lógica de filtros para reutilización en múltiples controladores 
- Generación de datos de prueba masivos para validación de filtros y balance 
- Versión preliminar enfocada en funcionalidad, pendiente de refactorización y optimización

---

## 🔮 Escalabilidad futura

El sistema está preparado para evolucionar hacía:

- Implementación completa de MVC o MVVM
- Persistencia más robusta (JPA / Hibernate)
- Modularización del sistema
- Manejo de datos mas complejos

---

## 📌 Conclusión

La arquitectura actual proporciona una base sólida para el crecimiento del sistema, permitiendo iterar sobre el diseño sin comprometer la estabilidad ni la claridad del código.

El enfoque adoptado prioriza la evolución progresiva, evitando sobre ingeniería en etapas tempranas mientras se mantiene una dirección clara hacía buenas prácticas de desarrollo de software.