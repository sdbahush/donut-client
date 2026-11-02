# Donut Client (Fabric 1.21.11)

A client-side Fabric utility mod with a dark click GUI.

## Included
- Right Shift opens the GUI.
- Coordinates HUD (toggle in HUD tab).
- Crosshair target-name HUD (toggle in HUD tab).
- Sprint Assist (toggle in Movement tab).
- Waypoint capture and local persistence (Waypoints tab).
- GUI tabs for HUD, Movement, Combat information, and Waypoints.

## Intentionally not included
This starter does not include server-data extraction, hidden/unloaded-chunk stash scanning, x-ray/ESP, automated attacks, aim assistance, autoclicking, packet manipulation, or server-side movement bypasses. It only displays information already available to the local client and saves coordinates the player explicitly records. Sprint Assist is a key-hold convenience and does not grant server-authorized flight or speed.

## Target toolchain
- Minecraft 1.21.11
- Java 21
- Fabric Loader 0.18.1
- Fabric Loom 1.14.x
- Fabric API 0.141.4+1.21.11

## Build
Run `gradle build` from this folder with Gradle installed. The remapped mod JAR is produced under `build/libs/`.

## Install
Copy `donut-client-1.0.0.jar` into your Minecraft `mods` folder alongside Fabric API, then launch the Fabric 1.21.11 profile.

GitHub Actions builds the project on pushes, pull requests, and manual dispatch, then uploads the remapped JAR as the `donut-client-fabric-1.21.11` artifact.
