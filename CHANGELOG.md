# 📜 Historial de versiones

## V1.2.0 (Pre-release)
- Implementación de nuevos filtros dinámicos:
  - Filtros por tipo y categoría en la tabla de movimientos
  - Filtro de búsqueda libre (BuscarTextFieldFiltro)
  - Filtros de fecha con tres ComboBox (Año, Mes, Día) para combinaciones flexibles
- Nuevos componentes en la interfaz:
  - Botones de eliminación con ícono en cada fila
  - Colores dinámicos en columna de monto según tipo de movimiento
- Nuevas utilidades en carpeta Utils:
  - FiltroFechaUtil (inicialización y lógica de filtros de fecha)
  - TablaColorUtil (aplicación de reglas de color en columnas)
- Modularización de lógica de filtros para reutilización en múltiples controladores
- Datos de prueba masivos generados para validación de filtros y balance
- Versión preliminar enfocada en funcionalidad, pendiente de refactorización y optimización


## V1.1.1
- Documentación completa en controladores, modelos, repositorios y clases de presentación
- Migración de dependencias a interfaces (IMovimientoServicio) para mejorar desacoplamiento
- Homogeneización del estilo de comentarios con JavaDoc en toda la aplicación
- Confirmación de separación de responsabilidades entre capas (Controlador, Servicio, Repositorio, Modelo)
- Refactorización ligera para depender de contratos en lugar de implementaciones concretas
- Versión estable sin cambios funcionales, enfocada en claridad y arquitectura

## V1.1.0
- Refactorización de controladores para mejorar separación de responsabilidades
- Creación de interfaces de servicio (ICategoriaServicio) para mayor mantenibilidad
- Nuevos utilitarios en Utils:
  - FormatoMonedaUtil (formato consistente de valores monetarios)
  - SaludoUtil (mensajes dinámicos de bienvenida)
  - ValidacionUtil (validaciones reutilizables de campos)
  - Mejora en servicios (MovimientoServicio, CategoriaServicio) con arquitectura modular
  - Documentación inicial en clases principales y servicios

## V1.0.0
- Implementación funcional del sistema
- Registro y visualización de movimientos
- Cálculo de balance en tiempo real
