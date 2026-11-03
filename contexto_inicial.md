# Proyecto: RentCarRD — Aplicación Móvil Empresarial

## 1. Contexto General del Proyecto y Asignación
* **Nombre de la Aplicación:** RentCarRD
* **Negocio:** Empresa dominicana de alquiler de vehículos propios y servicio de mantenimiento/reparación de vehículos particulares de clientes.
* **Estudiante / Desarrollador:** Bryan José Mieses Román (Número de lista: 21)
* **Curso / Centro:** 6to I Informática, Politécnico San Valero
* **Docente / Asesor:** Carlos Escalante
* **Nombre del Repositorio Oficial en GitHub:** `sanvalero-6toI-21-rentcarrd`
* **Idioma de Interfaz:** Español (Textos, diálogos, etiquetas)
* **Formato Monetario y Fechas:** Pesos Dominicanos (`RD$`), Fechas `dd/MM/yyyy`

---

## 2. Pila Tecnológica y Arquitectura Obligatoria
Toda la aplicación debe desarrollarse bajo las especificaciones técnicas estrictas de la asignación:

* **Lenguaje:** Kotlin
* **Interfaz Gráfica:** Jetpack Compose + Material 3 (Tema Oscuro por defecto `#000000`/`#121212` con acento Rojo `#D32F2F` y Blanco de contraste, con opción a Tema Claro)
* **Persistencia Local:** Room (SQLite)
* **Arquitectura:** MVVM + Clean Architecture en 3 capas estrictas (`ui/`, `domain/`, `data/`).
  * *Regla de Capas:* La interfaz Compose nunca llama al DAO directamente; la comunicación es exclusivamente `Pantalla Compose → ViewModel (StateFlow) → Caso de Uso (domain) → Repositorio → DAO`.
* **Inyección de Dependencias:** Hilt
* **Asincronía:** Corrutinas y Flow
* **Navegación:** Navigation Compose
* **Reportes:** `PdfDocument` de Android (PDF) + Exportación CSV
* **Gráficos:** Canvas nativo de Compose en el Dashboard
* **Editor y Compilación:** Visual Studio Code, compilando mediante Gradle CLI (`./gradlew assembleDebug`, `./gradlew installDebug`)

---

## 3. Estructura Obligatoria del Proyecto
El código y la documentación deben organizarse de la siguiente manera:

```text
sanvalero-6toI-21-rentcarrd/
├── AGENTS.md                  # Reglas del proyecto para agentes de IA
├── CLAUDE.md                  # Instrucciones para Claude
├── .github/
├── .vscode/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── res/               # Íconos, colores, recursos visuales
│       └── java/com/sanvalero/rentcarrd/
│           ├── RentCarRDApp.kt
│           ├── MainActivity.kt
│           ├── data/          # Room DB, DAOs, Entidades, Impl de Repositorios
│           ├── domain/        # Modelos de negocio, Interfaces, Casos de uso
│           ├── ui/            # Pantallas Compose, ViewModels, Componentes, Tema
│           ├── di/            # Módulos de Hilt
│           └── util/          # Formatters (RD$, fechas), Validadores
├── docs/
│   ├── 01-requisitos.md
│   ├── 02-arquitectura.md
│   ├── 03-modelo-datos.md
│   ├── 04-casos-uso.md
│   ├── 05-manual-usuario.md
│   └── prompts/
│       └── bitacora-ia.md     # Registro obligatorio de interacción con IA
├── README.md
└── build.gradle.kts
```

---

## 4. Roles, Permisos y Autenticación
La aplicación soporta 3 roles de usuario:

1. **Cliente:**
   * Registro e inicio de sesión local.
   * Consulta de catálogo de vehículos con filtros.
   * Solicitud de alquileres de flota y solicitudes de mantenimiento para sus vehículos particulares.
   * Acceso a la sección "Mis Contratos" (alquileres y mantenimientos) e historial propio.
2. **Operador:**
   * Gestión operativa diaria: Registro de entregas y devoluciones de alquileres.
   * Gestión de taller: Carga de diagnósticos, elaboración de presupuestos (mano de obra/repuestos), actualización de estados y fechas estimadas de entrega.
3. **Administrador:**
   * Acceso completo al Dashboard gerencial con gráficos de Canvas e indicadores en tiempo real.
   * CRUD completo de vehículos, clientes, operadores y tarifas.
   * Control de retrasos y penalizaciones.
   * Generación y exportación de Reportes PDF/CSV.
   * Exportación e importación de copias de seguridad de la base de datos (SQLite `.db`).

*Seguridad de Autenticación:* Las contraseñas deben guardarse cifradas mediante hashing (SHA-256 / BCrypt). En la inicialización (*database seeding*), debe crearse la cuenta administrativa inicial de demostración sin duplicarla en posteriores aperturas.

---

## 5. Modelo de Datos Mínimo (Room)
Se deben definir las siguientes entidades interrelacionadas con claves foráneas e integridad referencial (bajas lógicas para preservar el historial):

1. **`UserEntity` (`users`):** `id`, `fullName`, `email`, `passwordHash`, `phone`, `identificationCard` (Cédula/RNC), `role` (`CLIENT`, `OPERATOR`, `ADMIN`).
2. **`VehicleEntity` (`vehicles`):** `id`, `brand`, `model`, `year`, `color`, `category`, `transmission`, `passengers`, `fuelType`, `dailyRate`, `status` (`AVAILABLE`, `RENTED`, `MAINTENANCE`, `INACTIVE`), `imageUri`.
3. **`ContractEntity` (`contracts`):** `id`, `contractNumber`, `contractType` (`RENTAL`, `MAINTENANCE`), `clientId`, `vehicleId` (nullable), `startDate`, `expectedEndDate`, `actualEndDate`, `baseAmount`, `extraCharges`, `discounts`, `totalAmount`, `status`, `isDeleted`.
4. **`MaintenanceServiceEntity` (`maintenance_services`):** `id`, `contractId`, `vehicleBrand`, `vehicleModel`, `vehicleYear`, `vehicleColor`, `licensePlate`, `mileage`, `issueDescription`, `serviceType`, `laborCost`, `partsCost`, `promisedCompletionDate`, `actualCompletionDate`, `maintenanceStatus`.
5. **`PaymentEntity` (`payments`):** `id`, `contractId`, `amount`, `paymentDate`, `paymentMethod`, `status`.
6. **`SystemConfigEntity` (`system_configs`):** `key`, `value` (Tarifas de mora, porcentajes de descuento).

---

## 6. Reglas de Negocio Clave

* **Cálculo de Alquiler:**
  $$\text{Días Facturables} = \max\left(1, \lceil \frac{\text{Fecha Fin} - \text{Fecha Inicio}}{24 \text{ horas}} \rceil\right)$$
  $$\text{Precio Base} = \text{Días Facturables} \times \text{Tarifa Diaria}$$
* **Prevención de Overbooking:** No se pueden reservar ni alquilar vehículos si el rango de fechas solicitado $[T_{inicio}, T_{fin}]$ se solapa con un contrato activo o verificado existente para ese vehículo.
* **Control de Retrasos en Alquileres (Cliente):** Si la fecha real de devolución supera la acordada, se aplican recargos basados en reglas centralizadas parametrizables desde la administración.
* **Control de Retrasos en Mantenimiento (Taller):** Si la fecha real de finalización en taller supera la fecha prometida al cliente, el sistema calcula automáticamente un descuento sobre el presupuesto aprobado como penalización a la empresa.

---

## 7. Reportes Obligatorios

1. **Listado General:** Todos los registros de la entidad principal según filtros aplicados (PDF).
2. **Reporte por Período:** Movimientos de alquileres y mantenimientos entre dos fechas con totales y subtotales (PDF y CSV).
3. **Resumen Gerencial:** Indicadores gerenciales, ranking Top 5 de vehículos más alquilados y servicios más solicitados, junto a gráficos de rendimiento (PDF).

*Requisito de formato PDF:* Todos los archivos PDF deben incluir el encabezado con el nombre **RentCarRD**, su logo, la fecha de emisión y numeración de página, guardándose en la carpeta de Descargas con opción de compartir por `Intent.ACTION_SEND`.

---

## 8. Guía para la IA Programadora
* **No omitir arquitectura:** Generar código respetando strictly MVVM + Clean Architecture en 3 capas.
* **Cálculos aislados:** Los cálculos de precios, moras y descuentos deben ir en clases `UseCase` dentro del paquete `domain/usecase/`.
* **Interfaz reactiva:** Toda la UI debe ser reactiva usando `StateFlow` desde el `ViewModel` y componibles `Jetpack Compose`.
* **No inventar librerías externas:** Utilizar únicamente la pila técnica aprobada (Room, Hilt, Navigation Compose, Compose Canvas, Android PdfDocument).