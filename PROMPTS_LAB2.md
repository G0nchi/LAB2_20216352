# Prompts Usados En El Laboratorio 2

Modelo utilizado en todos los casos: GPT-5.3-Codex.

## 1. Base del proyecto
Prompt:
Implementa la base de mi app Android en Java usando view binding. Activa viewbinding en Gradle, mantén mainactivity como launcher y prepara el proyecto para agregar más activities.

Razon:
Necesitaba dejar lista la estructura técnica antes de construir las pantallas del laboratorio.

## 2. Pantalla inicial con validacion de internet
Prompt:
Crea una pantalla de bienvenida con codigo PUCP, nombre del alumno y botón ingresar. Al presionar el botón, valida que haya conectividad con connectivitymanager; si no hay internet muestra toast y no dejes avanzar

Razon:
El enunciado exige bloquear el acceso cuando no hay conexión.

## 3. CRUD
Prompt:
Implementa el CRUD completo de equipos en Java con Activities separadas: listado y formulario. Usa campos codigo, nombre, tipo (dropdown), estado, observaciones

Razon:
Este era el bloque principal del laboratorio y el de mayor puntaje

## 4. Confirmaciones y acciones contextuales
Prompt:
Agrega alertdialog de confirmación para crear, actualizar y eliminar. Ademas, implementa long press en el listado para abrir un contex action bar con acciones editar y eliminar

Razon:
El laboratorio pedía explícitamente dialog + context action bar para las operaciones

## 5. Filtros y ajustes visuales finales
Prompt:
Añade dos dropdowns de filtro por tipo y estado, con actualización automática del listado y botón refresh en la Toolbar para limpiar filtros. Finalmente ajusta la UI para respetar la zona segura superior y coloca el FAB abajo a la derecha

Razon:
Necesitaba completar los últimos requisitos funcionales y corregir la posición visual según la maqueta
