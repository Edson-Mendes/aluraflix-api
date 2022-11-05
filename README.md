<h1 align="center">Alura Flix API</h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em+Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=red&style=for-the-badge&logo=java)
![Badge Spring](https://img.shields.io/static/v1?label=SpringBoot&message=v2.7.5&color=brightgreen&style=for-the-badge&logo=SpringBoot)
![Badge JUnit5](https://img.shields.io/static/v1?label=JUnit5&message=v5.8.2&color=green&style=for-the-badge&logo=junit5)
![Badge Postgresql](https://img.shields.io/static/v1?label=PostgreSQL&message=v14.4&color=blue&style=for-the-badge&logo=PostgreSQL)

## :book: Resumo do projeto

Alura Flix é uma plataforma de compartilhamento de vídeos. Projeto proposto pela Alura no Challenge Backend 5ª edição.

## :bulb: Funcionalidades

- `Vídeos`
  - `Cadastrar`: Cadastro de vídeo através de um POST **/videos** com as informações de *title*, *description* e *url* do
    vídeo no corpo da requisição. Segue abaixo um exemplo do **corpo da requisição**.</br></br>

  ```json
    {
      "title": "vídeo xpto",
      "description": "descrição do vídeo xpto",
      "url": "http://www.xpto.com/duwehdiwehdhweuhd9487jh82"  
    }
  ```

  - `Buscar`: Busca paginada de vídeos através de um GET **/videos**. Opcional buscar por título com parâmetro *search* (e.g. **/videos?search=xpto**).
  Segue abaixo um exemplo do **corpo da resposta**.</br></br>
  
  ```json
    {
      "content": [
        {
            "id": 100,
            "title": "vídeo xpto",
            "description": "descrição do vídeo xpto",
            "url": "http://www.xpto.com/duwehdiwehdhweuhd9487jh82"
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

  - `Buscar por id`: Busca vídeo por id através de um GET **/videos/{ID}**, onde *{ID}* é o identificador do vídeo. 
  Segue abaixo um exemplo do **corpo da resposta**.</br></br>
  
  ```json
    {
      "id": 100,
      "title": "vídeo xpto",
      "description": "descrição do vídeo xpto",
      "url": "http://www.xpto.com/duwehdiwehdhweuhd9487jh82"
    }
  ```

  - `Atualizar`: Atualizar vídeo através de um PUT **/videos/{ID}**, onde *{ID}* é o identificador do vídeo, 
  os novos dados do vídeo devem ser enviados no corpo da requisição. Segue abaixo um exemplo do **corpo da requisição**.</br></br>

  ```json
    {
      "title": "vídeo xpto atualizado",
      "description": "descrição do vídeo xpto atualizado",
      "url": "http://www.xpto.com/duwehdiwehdhweuhd9487jh82"  
    }
  ```

  - `Deletar`: Deletar vídeo através de um DELETE **/videos/{ID}**, onde *{ID}* é o identificador do vídeo.</br></br>

- `Categorias`
  - `Cadastrar`: Cadastro de categoria através de um POST **/categories** com as informações de *title* e *color* da
    categoria no corpo da requisição. Segue abaixo um exemplo do **corpo da requisição**.</br></br>

  ```json
    {
      "title": "Comédia xpto",
      "color": "f0aff0" 
    }
  ```

  - `Buscar`: Busca paginada de categorias através de um GET **/categories**. Segue abaixo um exemplo do **corpo da resposta**.</br></br>
  
  ```json
    {
      "content": [
        {
            "id": 55,
            "title": "Comédia xpto",
            "color": "f0aff0"
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
  
  - `Buscar vídeos por categoria`: Busca de vídeos por categoria através de um GET **/categories/{ID}/videos**, 
  onde *{ID}* é o identificador da categoria. Segue abaixo um exemplo do **corpo da resposta**.</br></br>

  ```json
    {
      "content": [
        {
            "id": 100,
            "title": "vídeo xpto",
            "description": "descrição do vídeo xpto",
            "url": "http://www.xpto.com/duwehdiwehdhweuhd9487jh82",
            "categoryId" : 55
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
  
  - `Buscar por id`: Busca categoria por id através de um GET **/categories/{ID}**, onde *{ID}* é o identificador da categoria. 
  Segue abaixo um exemplo do **corpo da resposta**.</br></br>

  ```json
    {
      "id": 55,
      "title": "Comédia xpto",
      "color": "f0aff0"  
    }
  ```

  - `Atualizar`: Atualizar categoria através de um PUT **/categories/{ID}**, onde *{ID}* é o identificador da categoria, 
  os novos dados da categoria devem ser enviados no corpo da requisição. Segue abaixo um exemplo do **corpo da requisição**.</br></br>

  ```json
    {
      "title": "Comédia XPTO",
      "color": "0f0ab4" 
    }
  ```

  - `Deletar`: Deletar categoria através de um DELETE **/categories/{ID}**, onde *{ID}* é o identificador da categoria.

## :card_file_box: Docker

- Container Postgres
  ```
    docker-compose -f stack.yml up -d
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
- `Mockito`
- `JUnit5`
- `Testcontainers`
- `Testes de Unidade`
- `Testes de integração`

## :gear: Atualizações futuras
- [x] CRUD de Categorias dos vídeos.
- [x] Relacionamento entre categorias e vídeos.
- [ ] Swagger/OpenApi para uma melhor documentação da api.
- [ ] HATEOAS para ajudar os clientes a consumirem a API sem a necessidade de conhecimento prévio.
- [x] Testes de Integração.
- [ ] Pensar no que fazer com Vídeos associados a uma Categoria deletada.
- [ ] Fazer os Controllers/Services interagirem com as Services através de interfaces.