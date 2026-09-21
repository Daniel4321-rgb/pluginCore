# PluginCore

A modular Java system-monitoring tool for Linux using a plugin-based architecture.

## Features

- Process monitoring
- Service management
- Network monitoring
- Disk monitoring
- Modular OOP architecture

## Requirements

- Linux
- Java 21+
- OpenRC

## Run

```bash
javac Main.java core/*.java models/*.java plugins/*.java utils/*.java
java Main
