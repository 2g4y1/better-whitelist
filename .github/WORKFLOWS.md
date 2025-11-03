# GitHub Actions Workflows

Dieses Projekt verwendet GitHub Actions für automatisches Build, Testing und Release-Management.

## Workflows

### 🔨 Build and Test (`build.yml`)
**Trigger:** Push oder Pull Request auf `main` oder `develop` Branch

**Was wird gemacht:**
- Checkout des Codes
- Setup von JDK 21
- Maven Build (`mvn clean package`)
- Tests ausführen
- Artifact Upload (7 Tage verfügbar)

### 🔍 Lint Code (`lint.yml`)
**Trigger:** Push oder Pull Request auf `main` oder `develop` Branch

**Was wird gemacht:**
- Code-Kompilierung mit Lint-Checks
- Überprüfung auf Compiler-Warnings
- Code-Qualitätsprüfung

### 🚀 Release (`release.yml`)
**Trigger:** 
- Tag-Push im Format `v*.*.*` (z.B. `v1.0.0`)
- Manuell über GitHub Actions UI

**Was wird gemacht:**
- Build der finalen JAR-Datei
- Erstellen eines GitHub Releases
- Upload der JAR-Datei zum Release
- Automatische Release Notes

**Manuelles Release erstellen:**
1. Gehe zu "Actions" → "Release"
2. Klicke auf "Run workflow"
3. Gib die Version ein (z.B. `v1.0.1`)
4. Klicke auf "Run workflow"

### 🤖 Auto Release (`auto-release.yml`)
**Trigger:** Push auf `main` Branch (wenn `src/**` oder `pom.xml` geändert wurde)

**Was wird gemacht:**
- Automatisches Build bei jedem Commit
- Erstellt Pre-Release mit automatischer Versionierung
- Format: `v1.0.0-build.<commit-count>`
- Enthält Build-Datum und Commit-Message

**Hinweis:** Dies sind Pre-Releases, keine stabilen Versionen!

## Wie man ein stabiles Release erstellt

### Methode 1: Git Tag (empfohlen)
```bash
# Tag erstellen
git tag v1.0.0

# Tag pushen
git push origin v1.0.0
```

### Methode 2: Manuell über GitHub UI
1. Gehe zu "Actions" Tab
2. Wähle "Release" Workflow
3. Klicke "Run workflow"
4. Gib Version ein und starte

## Berechtigungen

Die Workflows benötigen folgende Berechtigungen:
- **contents: write** - Für das Erstellen von Releases (bereits in Workflows konfiguriert)

Diese sind standardmäßig in GitHub verfügbar. Keine zusätzlichen Secrets erforderlich!

## Status Badges

Füge diese zu deiner README.md hinzu:

```markdown
![Build Status](https://github.com/2g4y1/better-whitelist/actions/workflows/build.yml/badge.svg)
![Lint Status](https://github.com/2g4y1/better-whitelist/actions/workflows/lint.yml/badge.svg)
```

## Tipps

- **Auto-Releases** sind für Entwicklungs-Snapshots gedacht
- **Manuelle Releases** für stabile Versionen verwenden
- Tags sollten dem Format `v1.0.0` folgen (Semantic Versioning)
- Alle Workflows cachen Maven Dependencies für schnellere Builds
