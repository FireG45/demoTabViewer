

# DemoTabViewer
[![Build](https://github.com/FireG45/demoTabViewer/actions/workflows/docker-image.yml/badge.svg)](https://github.com/FireG45/demoTabViewer/actions/workflows/docker-image.yml)
## About demoTabViewer

<img src="https://imgbly.com/ib/Ce1qh9chQO.png" style="max-width: 100%; max-height: 100vh; width: auto; margin: auto;" alt="">

DemoTabViewer is web platform that can render, store and play Guitar Pro tabulatures in the browser. Built using libraries from the open source [TuxGuitar](https://github.com/pterodactylus42/tuxguitar-2.0beta)
project for reading [Guitar Pro](https://www.guitar-pro.com/) files, [VexFlow](https://www.vexflow.com/) libraries for tablature rendering, Spring Boot for implementing the server part, [Postgresql](https://www.postgresql.org/) for data storage and [MinIo](https://min.io/) for storing tab files.

- [Guitar Pro](https://www.guitar-pro.com/) is the de facto standard for storing guitar tab music.<br>
- [VexFlow](https://www.vexflow.com/) is widely used for rendering sheet music. It features an extensive library of musical elements, but each measure and symbol has to be created and positioned by hand in Javascript.<br>
- [TuxGuitar](https://github.com/pterodactylus42/tuxguitar-2.0beta) is a free and open-source tablature editor, which includes features such as tablature editing, score editing, and import and export of Guitar Pro gp3, gp4, and gp5 files.<br>

DemoTabViewer offers an open source solution for digital guitar music viewing, storing and rendering.

## Key Features

* Displays MusicXML sheet music in a browser 
* *Soon: Tabulature audio playback (work in progress)*
* Uses [Vexflow](https://www.vexflow.com/) for rendering and layout
* Parses most Guitar Pro note effects (Band, Natural/Artificial Harmonics, Palm Mute etc)
* *Soon: Allows modification of the displayed score, like changing note effects and durations (work in progress)*
* Written with React.js and Vexflow

<p align="left">
  <img title="OSMD in the Browser"  src="https://imgbly.com/ib/ujQ3TKRKmx.png" alt="ujQ3TKRKmx">
</p>

## Limitations
* Supports only Guitar Pro files below v.5
* Slides / hammer-ons between bars are not supported
* Web version on mobile doesn't renders properly

Also, **demoTabViewer is a renderer, not an interactive music editor. (work in progress)**

## Build
* To build the project you must have Docker Engine installed
* You can build and run all containers with this command:
 ```docker compose up --build -d```
* Or build all containers separately:
  * backend: ```docker build -t server .```
  * frontend: ```docker build -t client font-end```
  * postgres: ```docker run --name postgres -p 5432:5432 -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=tab_viewer_db -e PGDATA=/var/lib/postgresql/data/pgdata -d -v "$(pwd)":/var/lib/postgresql/data ```
  * minio: ```docker run -p 9000:9000 p 9001:9001 --name minio -v ~/minio/data:/data -e "MINIO_ROOT_USER=minioadmin" -e "MINIO_ROOT_PASSWORD=minioadmin" quay.io/minio/minio server /data --console-address ":9001"```
