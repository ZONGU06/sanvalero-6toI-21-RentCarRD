# AGENTS.md - RentCarRD Project Rules

## IA Programming Guidelines

### Architecture
- **Strict MVVM + Clean Architecture in 3 layers**
- UI (Compose) never calls DAO directly
- Communication flow: `Pantalla Compose → ViewModel (StateFlow) → Caso de Uso (domain) → Repositorio → DAO`
- Never bypass the layer restriction

### Calculations
- All price, mora, and discount calculations must be in `UseCase` classes within `domain/usecase/`
- Never perform business calculations directly in UI composables or ViewModels
- Use Cases must be pure business logic, independent of UI and data layers

### Interface
- All UI must be reactive using `StateFlow` from the `ViewModel`
- Use `Jetpack Compose` declarative composables
- UI must respond to state changes automatically via Flow

### Libraries
- **No external libraries** beyond the approved stack:
  - Kotlin
  - Jetpack Compose + Material 3
  - Room (SQLite)
  - Hilt (Dependency Injection)
  - Navigation Compose
  - Android PdfDocument (for reports)
  - Canvas native de Compose (for dashboard graphs)

### Authentication
- Passwords must be stored encrypted using hashing (SHA-256 / BCrypt)
- During database initialization (*database seeding*), the initial admin account must be created without duplicating it on subsequent openings

### mandatory screens and features
- Authentication: Login, Register
- Client: Catalog search, Rental requests, Maintenance requests, My Contracts section
- Operator: Daily deliveries/returns management, Diagnostics, Budgets
- Admin: Full dashboard with Canvas charts, CRUD vehicles/clients/operators/rates, Delay control, PDF/CSV reports, DB backup

### Reports
- All PDFs must include header with "RentCarRD" name, logo, emission date, and page numbering
- PDFs saved in Downloads folder with `Intent.ACTION_SEND` sharing option
- Export to CSV format also required
- Periodic reports (between two dates) with totals and subtotals

### Financial Calculations
- **Rental Calculation:**
  - Billable Days = max(1, ceil((End Date - Start Date) / 24 hours))
  - Base Price = Billable Days × Daily Rate
- **Overbooking Prevention:** Cannot reserve/rent if requested date range overlaps with existing active contract for that vehicle
- **Delay Penalties (Clients):** If actual return date exceeds agreed date, late fees apply based on centralizable rules from administration
- **Delay Penalties (Maintenance):** If actual completion date exceeds promised date, automatic discount on approved budget as penalty to company

### Database
- Room entities with foreign key relationships
- Logical deletions (soft delete) for preserving history
- Initial seeding with admin demo account (not duplicated on subsequent openings)

### Navigation
- Navigation Compose for all screen transitions
- Role-based access control (different screens per role: CLIENT, OPERATOR, ADMIN)