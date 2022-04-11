# Generic Toolkit

## Nexus

``` sh
$ docker run -d -p 9081:8081 --name c-nexus sonatype/nexus3
$ docker run -d -p 9081:8081 --name c-nexus -v $PWD/nexus-data:/nexus-data sonatype/nexus3
```

## Build

> [Sample Index](https://docs.gradle.org/current/samples/index.html)
> [Building Java Libraries Sample](https://docs.gradle.org/current/samples/sample_building_java_libraries.html)

### Gradle

- `$USER_HOME/.gradle/gradle.properties`
```groovy
nexusUrl='http://localhost:9081/repository/maven-snapshots/'
nexusUsername=admin
nexusPassword=hanzo
```

- `$USER_HOME/.gradle/init.gradle`
```groovy
allprojects {
    repositories {
        maven {
            name 'Local-Nexus'
            url 'http://localhost:9081/repository/maven-public/'
            allowInsecureProtocol(true)
        }
        mavenLocal()
    }
}
```

- `build.gradle`
```groovy
publishing {
    repositories {
        maven {
//            url = uri("${buildDir}/publishing-repository")
//            url = layout.buildDirectory.dir('publishing-repository')
//            url = uri('http://localhost:9081/repository/maven-snapshots/')
//            url = 'http://localhost:9081/repository/maven-snapshots/'
            url = nexusUrl
            allowInsecureProtocol = true
            credentials {
                username nexusUsername
                password nexusPassword
            }
        }
    }
}
```
