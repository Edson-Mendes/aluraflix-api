<h1 align="center">Alura Flix API</h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em+Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=red&style=for-the-badge&logo=java)
![Badge Spring](https://img.shields.io/static/v1?label=Spring&message=v2.7.5&color=brightgreen&style=for-the-badge&logo=spring)

## :book: Resumo do projeto

Alura Flix é uma plataforma de compartilhamento de vídeos. Projeto proposto pela Alura no Challenge Backend 5ª edição.

## Docker
- Volume do banco de dados
  ```
    docker volume create aluraflix-volume
  ```
- Container Postgres
  ```
    docker run -d -p 5432:5432 --name aluraflix-db -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=aluraflix-db -v aluraflix-volume:/var/lib/postgresql/data postgres
  ```

## :toolbox: Tecnologias

- `Intellij`
- `Java 17`
- `Maven`
- `Spring Boot, Spring MVC, Spring Data JPA`
- `Docker`
- `PostgreSQL`
- `Flyway`
- `Lombok`