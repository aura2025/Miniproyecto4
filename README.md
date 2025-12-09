# 🐉 Sistema de Combate RPG - Dragon Quest VIII

  

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)

![MVC](https://img.shields.io/badge/Architecture-MVC-blue?style=flat-square)

![Status](https://img.shields.io/badge/Status-Complete-success?style=flat-square)

  

Sistema de combate por turnos inspirado en Dragon Quest VIII, desarrollado en Java utilizando el patrón de diseño Modelo-Vista-Controlador (MVC) y estructuras de datos avanzadas.

  

---
## Integrantes del Grupo
-   Valentina Valencia - 202459626
-  Aura Maria Peláez - 202459422
-   Juan Felipe Aristizabal - 202459364



  

## 📋 Tabla de Contenidos

  

- [Características](#-características)

- [Estructuras de Datos](#-estructuras-de-datos-implementadas)

- [Arquitectura](#-arquitectura-mvc)

- [Requisitos](#-requisitos)

- [Instalación](#-instalación)

- [Uso](#-uso)

- [Sistemas Implementados](#-sistemas-implementados)

- [Documentación Técnica](#-documentación-técnica)



  

---

  

## Características

  

### Principales

- ✅ **Sistema de combate por turnos** basado en velocidad de personajes

- ✅ **4 héroes jugables** con habilidades únicas y armas especiales

- ✅ **4 tipos de monstruos** con diferentes comportamientos (Agresivo, Defensivo, Balanceado)

- ✅ **9 ítems del juego original** Dragon Quest VIII

- ✅ **Inventario individual** por héroe (5 espacios cada uno)

- ✅ **Sistema de poderes especiales** organizados en árbol binario

- ✅ **Sistema de buffs/debuffs** con duración en turnos

- ✅ **Cola FIFO** para gestión de turnos en el gremio

- ✅ **Sistema Deshacer/Rehacer** usando pilas (Stack)

- ✅ **Guardado/Carga de partidas** con serialización completa

- ✅ **Historial de batallas** persistente en archivos

  

### 🎨 Interfaces

-  **Vista Terminal**: Interfaz de texto en consola

-  **Vista GUI**: Interfaz gráfica con Swing

- Cambio dinámico entre vistas sin recompilar

  

---

  

## Estructuras de Datos Implementadas

  

### 1. **HashMap<Item, Integer>** - Inventario Individual

```java

private  Map<Item, Integer> items;

```

-  **Uso**: Gestión de ítems por héroe (clave: ítem, valor: cantidad)

-  **Complejidad**: O(1) para agregar, usar y verificar ítems

-  **Justificación**: Requisito del proyecto de estructura clave-valor con búsqueda eficiente

  

### 2. **Vector** - Listas de Entidades

```java

private  List<PersonajeJugable> heroes;

private  List<Monstruo> monstruos;

```

-  **Uso**: Almacenamiento thread-safe de héroes y monstruos

-  **Por qué Vector**: Sincronización automática para operaciones concurrentes

  

### 3. **Stack** - Sistema Deshacer/Rehacer

```java

private  Stack<EstadoCombate> pilaDeshacer;

private  Stack<EstadoCombate> pilaRehacer;

```

-  **Uso**: Historial de acciones LIFO (Last In, First Out)

-  **Límite**: 10 acciones almacenadas

  

### 4. **Queue (LinkedList)** - Cola del Gremio

```java

private  Queue<PersonajeJugable> colaGremio;

```

-  **Uso**: Sistema de atención FIFO (First In, First Out)

-  **Aplicación**: Turnos de aventureros en el gremio

  

### 5. **Árbol Binario** - Poderes Especiales

```java

ArbolPoderesEspeciales con NodoPoderEspecial

```

-  **Estructura**:

```

Poderes Especiales (raíz)

/ \

Envenenar Debilitar

/ \ / \

Aturdir Congelar (null) (null)

```

-  **Uso**: Organización jerárquica de poderes sin condicionales múltiples

-  **Recorridos**: InOrden y PreOrden implementados

  

### 6. **Enum** - Tipos Estructurados

```java

public  enum  Item { HIERBA, MEGA_HIERBA, RECUP_MP, ... }

```

-  **Ventaja**: Type-safety, validación en tiempo de compilación

-  **Uso**: Ítems, Estados, Armas, Poderes Especiales

  

---

  

## Arquitectura MVC

  

```

┌─────────────────────────────────────────────────────────┐

│ VISTA │

│ ┌─────────────────┐ ┌─────────────────┐ │

│ │ VistaTerminal │ │ VistaGUI │ │

│ └─────────────────┘ └─────────────────┘ │

└─────────────────────────────────────────────────────────┘

│

▼

┌─────────────────────────────────────────────────────────┐

│ CONTROLADOR │

│ ┌───────────────────────────────────────────────┐ │

│ │ • Gestión de combate │ │

│ │ • Turnos de héroes y monstruos │ │

│ │ • Sistema Deshacer/Rehacer │ │

│ │ • Guardado/Carga de partidas │ │

│ │ • Gestión de inventarios │ │

│ └───────────────────────────────────────────────┘ │

└─────────────────────────────────────────────────────────┘

│

▼

┌─────────────────────────────────────────────────────────┐

│ MODELO │

│ ┌─────────────────┐ ┌──────────────────────┐ │

│ │ PersonajeJugable│ │ InventarioIndividual │ │

│ │ • HP/MP │ │ • 5 espacios │ │

│ │ • Arma │ │ • HashMap<Item,Int> │ │

│ │ • Poder Especial│ └──────────────────────┘ │

│ └─────────────────┘ │

│ ┌─────────────────┐ ┌──────────────────────┐ │

│ │ Monstruo │ │ ArbolPoderesEspeciales│ │

│ │ • Tipo │ │ • Árbol binario │ │

│ │ • Comportamiento│ │ • Búsqueda O(1) │ │

│ └─────────────────┘ └──────────────────────┘ │

│ │

│ ┌─────────────────┐ ┌──────────────────────┐ │

│ │ GestorDeshacer │ │ GestorColaGremio │ │

│ │ • Stack × 2 │ │ • Queue FIFO │ │

│ └─────────────────┘ └──────────────────────┘ │

└─────────────────────────────────────────────────────────┘

```

  

---

  

## 💻 Requisitos

  

### Software

-  **Java**: JDK 11 o superior

-  **IDE** (opcional): Eclipse, IntelliJ IDEA, VS Code

  

### Dependencias

- Java Swing (incluido en JDK)

- No requiere librerías externas

  

---

  

## Instalación

  

### 1. Clonar el repositorio

```bash

git  clone  https://github.com/tu-usuario/sistema-combate-rpg.git

cd  sistema-combate-rpg

```

  

### 2. Compilar el proyecto

```bash

javac  -d  bin  src/**/*.java

```

  

### 3. Ejecutar

```bash

java  -cp  bin  App

```

  

---

  

## Uso

  

### Iniciar el Juego

  

Al ejecutar, verás el menú principal:

  

```

╔════════════════════════════════════════╗

║ MENÚ PRINCIPAL DEL GREMIO ║

╠════════════════════════════════════════╣

║ 1) Iniciar Batalla ║

║ 2) Atención en el Gremio ║

║ 3) Salir ║

╚════════════════════════════════════════╝

```

  

### Durante el Combate

  

En tu turno, tienes las siguientes opciones:

  

```

╔══════════════════════════════════════════════╗

║ ACCIONES - Heroe (HP 500/500) ║

╠══════════════════════════════════════════════╣

║ 1) Atacar ║

║ 2) Poder especial ║

║ 3) Defender ║

║ 4) Usar ítem ║

║ 5) Guardar partida ║

║ 6) Cargar partida ║

║ 7) DESHACER última acción ║

║ 8) REHACER acción deshecha ║

║ 9) Ver historial de acciones ║

║ 10) Gestionar inventario ║

╚══════════════════════════════════════════════╝

```

  

### Gestión de Inventario (Opción 10)

  

```

=== GESTIÓN DE INVENTARIO: Heroe ===

  

Heroe - Inventario (5/5):

• Hierba curativa x2

• Mega Hierba x1

• Recuperador de MP x1

• Antídoto x1

• Poción de fuerza x1

  

1. Tomar ítem del depósito común

2. Transferir ítem a otro héroe

3. Ver depósito común

4. Volver

```

  

---

  

## Sistemas Implementados

  

### 1. Sistema de Combate por Turnos

- Orden determinado por **velocidad** de personajes

- Turnos alternados héroes vs monstruos

- Estados alterados (Envenenado, Aturdido, Congelado, Debilitado)

  

### 2. Sistema de Inventario Individual

-  **5 espacios por héroe** (requisito del proyecto)

-  **9 ítems diferentes** del juego original:

- Hierba Curativa (50 HP)

- Mega Hierba (200 HP)

- Recuperador de MP (30 MP)

- Antídoto (cura envenenamiento)

- Estimulante (cura parálisis/congelamiento)

- Panacea (cura todos los estados)

- Poción de Fuerza (+20 ATK, 3 turnos)

- Poción de Defensa (+20 DEF, 3 turnos)

- Poción de Velocidad (+15 VEL, 3 turnos)

  

### 3. Árbol de Poderes Especiales

Organización jerárquica sin condicionales:

-  **ENVENENAR**: 50 MP, 3 turnos, daño por turno

-  **ATURDIR**: 50 MP, 1 turno, pierde turno

-  **CONGELAR**: 50 MP, 1 turno, pierde turno

-  **DEBILITAR**: 50 MP, 3 turnos, -10 ATK

  

### 4. Sistema Deshacer/Rehacer

-  **Stack de 10 acciones** máximo

- Restaura:

- HP/MP de todos los personajes

- Buffs activos

- Inventarios individuales

- Estado del combate

  

### 5. Sistema de Guardado/Carga

-  **Serialización en texto plano**

- Guarda:

- Estados de héroes y monstruos

- Inventario compartido

- Inventarios individuales (cada héroe)

- Orden de turnos

- Historial de batalla

- Ubicación: `partidas/partida_YYYYMMDD_HHMMSS.txt`

  

### 6. Cola del Gremio (FIFO)

- Sistema de atención de aventureros

- Primer héroe en llegar = primero en ser atendido

- Independiente del sistema de combate

  

---

  

## 📊 Personajes

  

### Héroes Jugables

  

| Héroe | HP | MP | ATK | DEF | VEL | Arma | Poder Especial | Inventario Inicial |

|-------|----|----|-----|-----|-----|------|----------------|--------------------|

| **Heroe** | 500 | 200 | 80 | 50 | 90 | Espada | Envenenar | Hierba x2, Mega Hierba, Recup MP, Antídoto, Poción Fuerza |

| **Jessica** | 400 | 250 | 90 | 40 | 70 | Bastón | Congelar | Recup MP x2, Hierba, Panacea, Estimulante, Poción Defensa |

| **Angelo** | 450 | 180 | 85 | 45 | 60 | Arco | Debilitar | Hierba x2, Poción Velocidad, Recup MP, Antídoto, Estimulante |

| **Yangus** | 600 | 150 | 70 | 70 | 50 | Hacha | Aturdir | Mega Hierba x2, Hierba, Poción Defensa, Estimulante, Poción Fuerza |

  

### Monstruos

  

| Monstruo | HP | ATK | DEF | VEL | Tipo | Comportamiento |

|----------|----|----|-----|-----|------|----------------|

| **Slime Gigante** | 300 | 60 | 40 | 85 | Agresivo | Ataque +20%, raramente defiende |

| **Golem de Piedra** | 350 | 55 | 60 | 65 | Defensivo | Reduce DEF enemigo, defiende frecuentemente |

| **Caballero Oscuro** | 280 | 65 | 45 | 55 | Balanceado | Reduce VEL enemigo |

| **Dragón Menor** | 400 | 75 | 50 | 45 | Defensivo | Tanque con alta defensa |

  

---

  

##  Estructura del Proyecto

  

```

Miniproyecto3Poe/

├── src/

│ ├── App.java # Punto de entrada

│ ├── Modelo/

│ │ ├── Personaje.java # Interface

│ │ ├── PersonajeJugable.java # Clase héroe

│ │ ├── Monstruo.java # Clase monstruo

│ │ ├── InventarioIndividual.java # Inventario 5 espacios

│ │ ├── Inventario.java # Depósito común

│ │ ├── Item.java # Enum ítems

│ │ ├── ArbolPoderesEspeciales.java # Árbol binario

│ │ ├── NodoPoderEspecial.java # Nodo del árbol

│ │ ├── GestorPoderesEspeciales.java # Singleton gestor

│ │ ├── GestorDeshacerRehacer.java # Stack deshacer/rehacer

│ │ ├── GestorColaGremio.java # Queue FIFO

│ │ ├── EstadoCombate.java # Snapshot para deshacer

│ │ ├── HistorialBatallas.java # Queue eventos

│ │ ├── Partida.java # Serialización

│ │ ├── Estado.java # Enum estados

│ │ ├── Arma.java # Enum armas

│ │ ├── PoderEspecial.java # Enum poderes

│ │ ├── TipoMonstruo.java # Enum tipos

│ │ ├── Buff.java # Buffs temporales

│ │ └── exceptions/ # Excepciones custom

│ ├── Vista/

│ │ ├── Vista.java # Interface vista

│ │ ├── VistaTerminal.java # Vista consola

│ │ └── VistaGUI.java # Vista Swing

│ └── Controlador/

│ └── Controlador.java # Lógica de juego

├── bin/ # Archivos compilados

├── partidas/ # Partidas guardadas

├── historiales/ # Historiales de batalla

└── README.md

```

  

---

  

## 📖 Documentación Técnica

  

### Complejidad Temporal

  

| Operación | Estructura | Complejidad | Ubicación |

|-----------|-----------|-------------|-----------|

| Verificar ítem | HashMap | O(1) | InventarioIndividual |

| Agregar ítem | HashMap | O(1) | InventarioIndividual |

| Usar ítem | HashMap | O(1) | InventarioIndividual |

| Buscar poder | Árbol Binario | O(n) ≈ O(1) | 5 nodos total |

| Deshacer acción | Stack | O(1) | GestorDeshacerRehacer |

| Atender gremio | Queue | O(1) | GestorColaGremio |

  

### Patrones de Diseño

  

#### 1. **Modelo-Vista-Controlador (MVC)**

-  **Modelo**: Lógica de negocio y datos

-  **Vista**: Presentación (Terminal/GUI)

-  **Controlador**: Coordinación entre Modelo y Vista

  

#### 2. **Singleton**

```java

GestorPoderesEspeciales.obtenerInstancia()

```

- Una única instancia del gestor de poderes

  

#### 3. **Strategy**

- Diferentes comportamientos de monstruos (Agresivo, Defensivo, Balanceado)

  

#### 4. **Memento**

-  `EstadoCombate` guarda snapshots para deshacer/rehacer

  

---

  

## 🧪 Ejemplos de Uso

  

### Ejemplo 1: Usar un Ítem en Combate

```

Turno #5 - Heroe

1) Atacar

4) Usar ítem

  

→ Selecciona: 4

→ Inventario: Hierba x2, Mega Hierba x1

→ Selecciona: Hierba curativa

→ Objetivo: Jessica (HP: 250/400)

  

✓ Heroe usa Hierba curativa en Jessica

✓ Jessica recupera 50 HP. HP actual: 300/400

```

  

### Ejemplo 2: Transferir Ítem entre Héroes

```

Turno #3 - Angelo

10) Gestionar inventario

  

→ 2. Transferir ítem a otro héroe

→ Tu inventario: Poción de Velocidad x1

→ Destino: Yangus (Espacios: 2)

  

✓ Angelo transfirió Poción de Velocidad a Yangus

```

  

### Ejemplo 3: Deshacer una Acción

```

Turno #7 - Jessica

(Usa Mega Hierba en Heroe)

  

7) DESHACER última acción

  

✓ Acción deshecha: Jessica Va a usar un ítem

✓ Mega Hierba restaurada al inventario

✓ HP de Heroe restaurado a valor previo

```

  



  

---

  

##  Notas del Proyecto

  

### Cumplimiento de Requisitos Académicos

  

✅ **Estructura clave-valor**: HashMap<Item,  Integer>

✅ **Mínimo 5 objetos diferentes**: Cada héroe tiene 5 tipos

✅ **Objetos del juego original**: 9 ítems de Dragon Quest VIII

✅ **Estructuras de datos nuevas**: HashMap, Vector, Stack, Queue, Árbol Binario

✅ **Justificación técnica**: Documentación completa del por qué de cada estructura

  

### Características Técnicas Destacadas

  

-  **Thread-Safety**: Uso de Vector para operaciones sincronizadas

-  **Type-Safety**: Uso extensivo de Enums

-  **Memento Pattern**: Sistema completo de deshacer/rehacer

-  **Serialización**: Guardado/carga de estado completo del juego

-  **Árbol Binario**: Organización jerárquica sin condicionales múltiples

  

---
  

## 📄 Licencia

  

Este proyecto es de carácter académico y se proporciona tal cual con fines educativos.

  

---

  

##  Agradecimientos

  

- Inspirado en **Dragon Quest VIII: Journey of the Cursed King**

- Diseño de personajes e ítems basados en el juego original

- Sistema de combate por turnos tradicional de JRPGs

  

---

  

## 📚 Referencias

  

- [Dragon Quest VIII Wiki](https://dragonquest.fandom.com/wiki/Dragon_Quest_VIII)

- [Java Collections Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/)

- [MVC Pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)

- [Data Structures in Java](https://docs.oracle.com/javase/tutorial/collections/)

  

---

  

<div  align="center">

  

**⚔️ ¡Que comience la batalla! ⚔️**

  

</div>
