# Generic Toolkit

## Nexus

``` sh
$ docker run -d -p 9081:8081 --name c-nexus sonatype/nexus3
$ docker run -d -p 9081:8081 --name c-nexus -v $PWD/nexus-data:/nexus-data sonatype/nexus3
```

## Build

> [Sample Index](https://docs.gradle.org/current/samples/index.html)
> [Building Java Libraries Sample](https://docs.gradle.org/current/samples/sample_building_java_libraries.html)
