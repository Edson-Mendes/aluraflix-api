<h1 align="center">Alura Flix API</h1>

![Badge Concluído](https://img.shields.io/static/v1?label=Status&message=Concluído&color=green&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=red&style=for-the-badge&logo=java)
![Badge Spring](https://img.shields.io/static/v1?label=SpringBoot&message=v2.7.5&color=brightgreen&style=for-the-badge&logo=SpringBoot)
![Badge JUnit5](https://img.shields.io/static/v1?label=JUnit5&message=v5.8.2&color=green&style=for-the-badge&logo=junit5)
![Badge Postgresql](https://img.shields.io/static/v1?label=PostgreSQL&message=v14.4&color=blue&style=for-the-badge&logo=PostgreSQL)

## :book: Resumo do projeto

Alura Flix é uma plataforma de compartilhamento de vídeos. Projeto proposto pela Alura no Challenge Backend 5ª edição.

## :bulb: Funcionalidades

- `Autenticação`</br></br>
  Todos os recursos, exceto  **POST /auth/signup** e **GET /videos/free**, requerem clientes autenticados,
  o qual deve ser feita em todas as requisições via [basic auth](https://datatracker.ietf.org/doc/html/rfc7617),
  enviando o header _**Authorization: Basic username:password**_</br>

  - `Sign Up`: Cadastro de usuário através de um **POST /auth/signup** com os dados do usuário no corpo da requisição.
    Segue abaixo um exemplo do **corpo da requisição**.</br></br>

  ```json
    {
      "name" : "Lorem Ipsum",
      "email" : "lorem@email.com",
      "password" : "123456"
    }
  ```

- `Vídeos`
  - `Cadastrar`: Cadastro de vídeo através de um **POST /videos** com as informações de *title*, *description* e *url* do
    vídeo no corpo da requisição. Segue abaixo um exemplo do **corpo da requisição**.</br></br>

  ```json
    {
      "title": "vídeo xpto",
      "description": "descrição do vídeo xpto",
      "url": "http://www.xpto.com/duwehdiwehdhweuhd9487jh82"  
    }
  ```

  - `Buscar`: Busca paginada de vídeos através de um **GET /videos**. Opcional buscar por título com parâmetro *search* (e.g. **/videos?search=xpto**).
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

  - `Buscar por id`: Busca vídeo por id através de um **GET /videos/{ID}**, onde *{ID}* é o identificador do vídeo. 
  Segue abaixo um exemplo do **corpo da resposta**.</br></br>
  
  ```json
    {
      "id": 100,
      "title": "vídeo xpto",
      "description": "descrição do vídeo xpto",
      "url": "http://www.xpto.com/duwehdiwehdhweuhd9487jh82"
    }
  ```
  
  - `Buscar amostra`: Buscar últimos 5 vídeos adicionados através de um **GET /videos/free**, essa requisição _não_ requer autenticação.
    Segue abaixo um exemplo do **corpo da resposta**.</br></br>

  ```json
    [
      {
        "id": 12,
        "title": "Vídeo Comum 12",
        "description": "Vídeo comum 12",
        "url": "http://www.funnyvideos.com/d39167ge6gd6gf",
        "categoryId": 1
      },
      {
        "id": 11,
        "title": "Vídeo Comum 11",
        "description": "Vídeo Comum 11",
        "url": "http://www.xpto.com/f546h56h56h",
        "categoryId": 1
      },
      {
        "id": 10,
        "title": "Vídeo Comum 10",
        "description": "Vídeo comum 10",
        "url": "http://www.funnyvideos.com/n7j756unbgf",
        "categoryId": 1
      },
      {
        "id": 9,
        "title": "Vídeo Comum 9",
        "description": "Vídeo comum 9",
        "url": "http://www.funnyvideos.com/b3t24f3f",
        "categoryId": 1
      },
      {
        "id": 8,
        "title": "Vídeo Comum 8",
        "description": "Vídeo comum 8",
        "url": "http://www.funnyvideos.com/tts6gf",
        "categoryId": 1
      }
   ]
  ```

  - `Atualizar`: Atualizar vídeo através de um **PUT /videos/{ID}**, onde *{ID}* é o identificador do vídeo, 
  os novos dados do vídeo devem ser enviados no corpo da requisição. Segue abaixo um exemplo do **corpo da requisição**.</br></br>

  ```json
    {
      "title": "vídeo xpto atualizado",
      "description": "descrição do vídeo xpto atualizado",
      "url": "http://www.xpto.com/duwehdiwehdhweuhd9487jh82"  
    }
  ```

  - `Deletar`: Deletar vídeo através de um **DELETE /videos/{ID}**, onde *{ID}* é o identificador do vídeo.</br></br>

- `Categorias`
  - `Cadastrar`: Cadastro de categoria através de um **POST /categories** com as informações de *title* e *color* da
    categoria no corpo da requisição. Segue abaixo um exemplo do **corpo da requisição**.</br></br>

  ```json
    {
      "title": "Comédia xpto",
      "color": "f0aff0" 
    }
  ```

  - `Buscar`: Busca paginada de categorias através de um **GET /categories**. Segue abaixo um exemplo do **corpo da resposta**.</br></br>
  
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
  
  - `Buscar vídeos por categoria`: Busca de vídeos por categoria através de um **GET /categories/{ID}/videos**, 
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
  
  - `Buscar por id`: Busca categoria por id através de um **GET /categories/{ID}**, onde *{ID}* é o identificador da categoria. 
  Segue abaixo um exemplo do **corpo da resposta**.</br></br>

  ```json
    {
      "id": 55,
      "title": "Comédia xpto",
      "color": "f0aff0"  
    }
  ```

  - `Atualizar`: Atualizar categoria através de um **PUT /categories/{ID}**, onde *{ID}* é o identificador da categoria, 
  os novos dados da categoria devem ser enviados no corpo da requisição. Segue abaixo um exemplo do **corpo da requisição**.</br></br>

  ```json
    {
      "title": "Comédia XPTO",
      "color": "0f0ab4" 
    }
  ```

  - `Deletar`: Deletar categoria através de um **DELETE /categories/{ID}**, onde *{ID}* é o identificador da categoria.</br></br>

  
## :hammer_and_wrench: Deploy

Realizei o deploy da aplicação na AWS, você pode Testar/Brincar através do [Swagger UI](http://aluraflixapi-env.eba-79ddpr8r.us-east-1.elasticbeanstalk.com/swagger-ui.html).
Pode criar um usuário ou usar o username _**visitor@email.com**_ com password _**12345678**_.</br>
Se possível, me avise caso encontre algum bug ou tenha alguma sugestão.</br></br>

## :whale: Criar container docker da aplicação

Execute este aquivo [docker-compose.yml](https://github.com/Edson-Mendes/aluraflix-api/blob/main/docker-compose.yml). 
Que irá subir um container postgres e um container da aplicação Aluraflix.

  ```
    docker-compose -f docker-compose.yml up
  ```

Após subir os containers, acesse <http://localhost:22222/swagger-ui.html>.

## :toolbox: Tecnologias

- `Intellij`
- `Java 17`
- `Maven`
- `Spring Boot, Spring MVC, Spring Data JPA, Spring Security`
- `Docker`
- `PostgreSQL`
- `Flyway`
- `Lombok`
- `OpenAPI/Swagger`
- `Mockito`
- `JUnit5`
- `Testcontainers`
- `Testes de unidade`
- `Testes de integração`
- `AWS`

## :gear: Atualizações futuras
- [ ] HATEOAS para ajudar os clientes a consumirem a API sem a necessidade de conhecimento prévio.
- [ ] Configurar CORS.
- [x] Atualizar imagem docker do projeto.
- [x] Acesso a recursos apenas autenticado.
- [ ] Devolver mensagem "amigável" em caso de 409 (Conflict).
- [x] Realizar deploy da aplicação em algum serviço cloud.
- [ ] Criar funcionalidades de atualizar e deletar usuário.
- [ ] Adicionar OAuth2 (Apenas para estudar)
