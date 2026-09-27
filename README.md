

# DemoTabViewer

## About demoTabViewer

<img src="https://storage.imgbly.com/imgbly/axyhnneeYc.png" style="max-width: 100%; max-height: 100vh; width: auto; margin: auto;" alt="">

DemoTabViewer is web platform that can render, store and play Guitar Pro tabulatures in the browser. Built using libraries from the open source [TuxGuitar](https://github.com/pterodactylus42/tuxguitar-2.0beta)
project for reading [Guitar Pro](https://www.guitar-pro.com/) files, [VexFlow](https://www.vexflow.com/) libraries for tablature rendering, Spring Boot for implementing the server part, [Postgresql](https://www.postgresql.org/) for data storage and [MinIo](https://min.io/) for storing tab files.

- [Guitar Pro](https://www.guitar-pro.com/) is the de facto standard for storing guitar tab music.<br>
- [VexFlow](https://www.vexflow.com/) is widely used for rendering sheet music. It features an extensive library of musical elements, but each measure and symbol has to be created and positioned by hand in Javascript.<br>
- [TuxGuitar](https://github.com/pterodactylus42/tuxguitar-2.0beta) is a free and open-source tablature editor, which includes features such as tablature editing, score editing, and import and export of Guitar Pro gp3, gp4, and gp5 files.<br>

DemoTabViewer offers an open source solution for digital guitar music viewing, storing and rendering.

## What I built and what comes from third parties

The application code is under `src/main/java/ru/fireg45/demotabviewer` and `front-end/src`. It includes the Spring Boot API for users and tablatures, search and favourites, PostgreSQL persistence, MinIO file storage, and the React viewer and editor built with VexFlow.

The bundled code under `src/main/java/org/herac/tuxguitar` comes from the open-source TuxGuitar project and is used to read Guitar Pro files. VexFlow is also a third-party library. Neither library is presented here as original application code.

## Key Features

* Displays Guitar pro tabs in a browser 
* Uses [Vexflow](https://www.vexflow.com/) for rendering and layout
* Parses most Guitar Pro note effects (Band, Natural/Artificial Harmonics, Palm Mute etc)
* Written with React.js and Vexflow

## Limitations
* Supports only Guitar Pro files below v.5
* Slides / hammer-ons between bars are not supported
* Web version on mobile doesn't renders properly

## Build

Install Docker Engine with Docker Compose. From the repository root run:

```bash
docker compose up --build -d
```

To stop the containers:

```bash
docker compose down
```
