This is a sample project that runs [JDBI](https://jdbi.org/) on [Quarkus](https://quarkus.io/) native image.

## Prerequisites:
Install [Docker](https://docs.docker.com/get-docker/)

---

- To build the native image:
<!-- -->
    ./gradlew buildRegularNativeImage

- Before running the native image, run postgres container:
<!-- -->
    docker run -it --rm -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=fouad postgres:14.4

- To run the native image:
<!-- -->
    docker run -it --rm -p 2030:2030 -v {PATH_TO_CONFIG_FILE}:/work/config io.fouad/jdbi-quarkus-native:0.1

replace `{PATH_TO_CONFIG_FILE}` with the absolute path, e.g. `C:/Users/fouad/IdeaProjects/jdbi-quarkus-native/sample-prod-configs`

    docker run -it --rm -p 2030:2030 -v C:/Users/fouad/IdeaProjects/jdbi-quarkus-native/sample-prod-configs:/work/config io.fouad/jdbi-quarkus-native:0.1

output:

    __  ____  __  _____   ___  __ ____  ______ 
    --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
    -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \
    --\___\_\____/_/ |_/_/|_/_/|_|\____/___/
    2022-07-26 11:54:13,779 [] INFO  [io.quarkus] (main) jdbi-quarkus-native 0.1 native (powered by Quarkus 2.10.3.Final) started in 0.128s. Listening on: http://0.0.0.0:2030
    2022-07-26 11:54:13,779 [] INFO  [io.quarkus] (main) Profile prod activated.
    2022-07-26 11:54:13,779 [] INFO  [io.quarkus] (main) Installed features: [agroal, cdi, jdbc-postgresql, narayana-jta, resteasy, smallrye-context-propagation, vertx]

- Access this url to add a new user:
<!-- -->
    http://localhost:2030/users/add

output:

    User{id=2, name='Amelia'}

- Access this url to list all users:
<!-- -->
    http://localhost:2030/users/list

output:

    [User{id=2, name='Amelia'}, User{id=1, name='Harper'}]