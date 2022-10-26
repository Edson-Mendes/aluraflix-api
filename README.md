<h1 align="center">Alura Flix API</h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em+Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=red&style=for-the-badge&logo=java)
![Badge Spring](https://img.shields.io/static/v1?label=SpringBoot&message=v2.7.5&color=brightgreen&style=for-the-badge&logo=SpringBoot)
![Badge JUnit5](https://img.shields.io/static/v1?label=JUnit5&message=v5.8.2&color=green&style=for-the-badge&logo=junit5)
![Badge JUnit5](https://img.shields.io/static/v1?label=PostgreSQL&message=v14.4&color=blue&style=for-the-badge&logo=PostgreSQL)

## :book: Resumo do projeto

Alura Flix é uma plataforma de compartilhamento de vídeos. Projeto proposto pela Alura no Challenge Backend 5ª edição.

## :bulb: Funcionalidades

- `Vídeos`
  - `cadastrar`: Cadastro de vídeo através de um POST para **/videos** com as informações de title, description e url do
    vídeo no corpo da requisição.
  
  ```json
    {
      "id": 100,
      "title": "vídeo xpto",
      "description": "descrição do vídeo xpto",
      "url": "http://www.xptovideos.com/duwehdiwehdhweuhd9487jh82"  
    }
  ```

  - `Buscar`: Busca paginada de videos através de um GET para **/videos**.
  
  ```json
    {
      "content": [
        {
            "id": 100,
            "title": "vídeo xpto",
            "description": "descrição do vídeo xpto",
            "url": "http://www.xptovideos.com/duwehdiwehdhweuhd9487jh82"
        }
      ],
      "pageable": {
        "sort": {
          "empty": false,
          "unsorted": false,
          "sorted": true
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 10,
        "paged": true,
        "unpaged": false
      },
      "last": true,
      "totalPages": 1,
      "totalElements": 1,
      "size": 10,
      "number": 0,
      "sort": {
        "empty": false,
        "unsorted": false,
        "sorted": true
      },
      "first": true,
      "numberOfElements": 1,
      "empty": false
    }
  ```

  - `Buscar por id`: Busca vídeo por id através de um GET para **/videos/{ID}**, onde *{ID}* é o identificador do vídeo.
  
  ```json
    {
      "id": 100,
      "title": "vídeo xpto",
      "description": "descrição do vídeo xpto",
      "url": "http://www.xptovideos.com/duwehdiwehdhweuhd9487jh82"
    }
  ```

  - `Atualizar`: Atualizar vídeo através de um PUT para **/videos/{ID}**, onde *{ID}* é o identificador do vídeo, 
  os novos dados do vídeo devem ser enviados no corpo da requisição.

  ```json
    {
      "title": "vídeo xpto atualizado",
      "description": "descrição do vídeo xpto atualizado",
      "url": "http://www.xptovideos.com/duwehdiwehdhweuhd9487jh82"  
    }
  ```

  - `Deletar`: Deletar vídeo através de um DELETE para **/videos/{ID}**, onde *{ID}* é o identificador da vídeo.

## :card_file_box: Docker
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