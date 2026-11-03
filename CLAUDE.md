# CLAUDE.md - RentCarRD Project Instructions

## Project Overview
RentCarRD is a Dominican Republic vehicle rental and maintenance management mobile application built with Kotlin and Jetpack Compose.

## Development Commands
- Build: `./gradlew assembleDebug`
- Install: `./gradlew installDebug`
- Clean: `./gradlew clean`

## Project Structure
Follow the mandatory structure defined in `contexto_inicial.md`:
- `app/src/main/java/com/sanvalero/rentcarrd/`
  - `data/` - Room DB, DAOs, Entities, Repository implementations
  - `domain/` - Business models, Use Case interfaces, domain logic
  - `ui/` - Jetpack Compose screens, ViewModels, Components, Theme
  - `di/` - Hilt modules for dependency injection
  - `util/` - Formatters (RD$, dates), Validators

## Key Directories
- `docs/` - Project documentation (requirements, architecture, data model, use cases, user manual)
- `docs/prompts/` - IA interaction log (bitacora-ia.md)
- `.github/` - GitHub Actions and configurations
- `.vscode/` - VS Code settings and tasks

## Technical Requirements
- Language: Kotlin
- UI: Jetpack Compose + Material 3
- Default dark theme: `#000000`/`#121212` with red accent `#D32F2F` and contrast white
- Optional light theme support
- Local persistence: Room (SQLite)
- Architecture: MVVM + Clean Architecture 3 layers
- Dependency injection: Hilt
- Asynchrony: Coroutines and Flow
- Navigation: Navigation Compose
- Reporting: Android PdfDocument + CSV export
- Dashboard graphs: Native Compose Canvas

## Financial Format
- Currency: Dominican Pesos (RD$)
- Date format: dd/MM/yyyy

## Roles and Permissions
1. **CLIENT**: Catalog search, rental requests, maintenance requests, "Mis Contratos" section
2. **OPERATOR**: Daily deliveries/returns, diagnostics, budgets, status updates
3. **ADMIN**: Full dashboard with charts, CRUD vehicles/clients/operators/rates, delay control, PDF/CSV reports, DB backup

## Security
- Passwords encrypted with BCrypt or SHA-256
- Initial admin account created during database seeding
- Admin account must not be duplicated on subsequent openings

## Output Files
- PDF reports with RentCarRD header, logo, date, and page numbering
- CSV exports for period reports
- Database backup (.db) export/import