Projeto - Campanha Sócio Torcedor CRUD 
-
Esta aplicação foi construída utilizando Spring Boot Framework, Spring Data e Spring MVC. Utilizei também banco de dados H2.
A aplicação está separada por camadas: 

- *model*: as entidades de banco de dados e projeções de output estão aqui;
- *repository*: a camada de acesso ao banco de dados;
- *service*: as regras de negócio que são feitas com esses dados;
- *controller*: a exposição dos serviços e construção de outputs.

Temos então 3 entidades: Campanha, Cliente e Time (utilizada como referência).

Automaticamente são carregados os times de referência através do arquivo data.sql.


Compilar
-
    mvn compile

Rodar integration e system tests automatizados
-
    mvn test

Rodar a aplicação 
-
    mvn spring-boot:run

Uso
-
Esta aplicação roda em `http://localhost:8080` por default; favor utilizar alguma aplicação do tipo `curl` ou `ARC` para executar as chamadas nas APIs REST.

1.Criar Campanha
-

Para criar uma campanha, use o endpoint `/api/campanha` com o método HTTP POST e um JSON no request, como no exemplo abaixo:

`POST http://localhost:8080/api/campanha`

Caso a campanha tenha períodos conflitantes (como descrito no enunciado do desafio) construí a seguinte solução: efetua uma busca no banco de dados que retorne todas as campanhas com data de início da vigência conflitantes; separa-se desse resultado todas com a data fim da vigência conflitante; adiciona um dia na data fim de vigência delas; utilizei uma estrutura de Set para verificar as que ainda tinham datas conflitantes mas agora utilizando todo o resultado da busca inicial, como o Set não aceita valores (de data fim de vigência) repetidos, vou adicionando um dia até que a data fim não colida com nenhuma outra; persisto os objetos no banco.

Body

    {
      "nome": "Campanha 1",
      "time" : {"id": 1, "nome": "Atlético de Botucatu"},
      "dtInicio": "2017-01-16T18:25:43Z",
      "dtFim": "2017-08-16T18:25:43Z"
    }

Output

O output do serviço será a campanha carregada agora com o ID da base de dados:

    {
        "id": 10,
        "nome": "Campanha 1",
        "time": {
            "id": 1,
            "nome": "Atlético de Botucatu"
         },
        "dtInicio": "2017-01-16T18:25:43Z",
        "dtFim": "2017-08-16T18:25:43Z"
    }

2.Listar Campanha pelo ID
-

Para listar uma campanha, use o endpoint `/api/campanha/{id}` com o método HTTP GET substituindo o {id} pelo ID desejado, como no exemplo abaixo:

`GET http://localhost:8080/api/campanha/10`

Output

    {
        "id": 10,
        "nome": "Campanha 1",
        "time": {
            "id": 1,
            "nome": "Atlético de Botucatu"
         },
        "dtInicio": "2017-01-16T18:25:43Z",
        "dtFim": "2017-08-16T18:25:43Z"
    }
    
3.Listar todas as campanhas
-

Para listar todas as campanhas, use o endpoint `/api/campanha/all` com o método HTTP GET, como no exemplo abaixo:

`GET http://localhost:8080/api/campanha/all`

Output
```
[
  {
    "id": 10,
    "nome": "Campanha 1",
    "time": {
      "id": 1,
      "nome": "Atlético de Botucatu"
    },
    "dtInicio": "2017-01-16T18:25:43Z",
    "dtFim": "2017-08-16T18:25:43Z"
  },
  {
    "id": 11,
    "nome": "Campanha 1",
    "time": {
      "id": 1,
      "nome": "Atlético de Botucatu"
    },
    "dtInicio": "2017-01-16T18:25:43Z",
    "dtFim": "2017-08-16T18:25:43Z"
  }
]
```
4.Atualizar Campanha
-

Para atualizar uma campanha, use o endpoint `/api/campanha/{id}` , onde o id é o ID da campanha a ser atualizada, com o método HTTP PUT e um JSON no request, como no exemplo abaixo:

`PUT http://localhost:8080/api/campanha`

Body

    {
      "nome": "Campanha Nova",
      "time" : {"id": 10, "nome": "Atlético de Botucatu"},
      "dtInicio": "2017-06-16T18:25:43Z",
      "dtFim": "2017-06-16T18:25:43Z"
    }

Output

    {
        "id": 12,
        "nome": "Campanha Nova",
        "time": {
            "id": 1,
            "nome": "Atlético de Botucatu"
        },
        "dtInicio": "2017-06-16T18:25:43Z",
        "dtFim": "2017-06-16T18:25:43Z"
    }
    
5.Deletar Campanha
-

Para deletar uma campanha, use o endpoint `/api/campanha/{id}` , onde o id é o ID da campanha a ser deletada, com o método HTTP DELETE, como no exemplo abaixo:

`DELETE http://localhost:8080/api/campanha/12`

Output

O output será o objeto que foi deletado

    {
        "id": 12,
        "nome": "Campanha Nova",
        "time": {
            "id": 1,
            "nome": "Atlético de Botucatu"
        },
        "dtInicio": "2017-06-16T18:25:43Z",
        "dtFim": "2017-06-16T18:25:43Z"
    }

6.Criar Cliente
-

Para criar um cliente, use o endpoint `/api/cliente` com o método HTTP POST e um JSON no request, como no exemplo abaixo:

`POST http://localhost:8080/api/cliente`

Body

    {
      "nome": "Gustavo Bemfica",
      "email": "gu.sartori@gmail.com",
      "time" : {"id": 1, "nome": "Atlético de Botucatu"},
      "dtNascimento": "1983-11-09T18:25:43Z"
    }

Output

O output do serviço será o cliente carregada agora com o ID da base de dados:

    {
        "id": 20,
        "email": "gu.sartori@gmail.com",
        "nome": "Gustavo Bemfica",
        "dtNascimento": "1983-11-09T18:25:43Z",
        "time": {
            "id": 1,
            "nome": "Atlético de Botucatu"
        },
        "campanhaList": null
    }

Caso o cliente já existe então são mostrados: uma mensagem amigável, as campanhas à qual ele contratou e às quais ele pode aderir (que sejam do mesmo time e esteja com data vigente válida, ou seja, data atual maior ou igual que data de início e menor ou igual que data final da campanha).

    {
        "message": "Cliente com email já cadastrado.",
        "cliente": {
            "id": 20,
            "email": "gu.sartori@gmail.com",
            "nome": "Gustavo Bemfica",
            "dtNascimento": "1983-11-09T18:25:43Z",
            "time": {
                "id": 1,
                "nome": "Atlético de Botucatu"
            },
            "campanhaList": [],
        },
        "campanhasDisponiveis": [{
            "id": 10,
            "nome": "Campanha 1",
            "time": {
                "id": 1,
                "nome": "Atlético de Botucatu"
            },
            "dtInicio": "2017-01-16T18:25:43Z",
            "dtFim": "2017-08-16T18:25:43Z"
        },
        {
            "id": 11,
            "nome": "Campanha 1",
            "time": {
                "id": 1,
                "nome": "Atlético de Botucatu"
            },
            "dtInicio": "2017-01-16T18:25:43Z",
            "dtFim": "2017-08-16T18:25:43Z"
        }
        ],
    }

7.Listar Cliente pelo ID
-

Para listar uma campanha, use o endpoint `/api/cliente/{id}` com o método HTTP GET substituindo o {id} pelo ID desejado, como no exemplo abaixo:

`GET http://localhost:8080/api/cliente/10`

Output

    {
        "id": 20,
        "email": "gu.sartori@gmail.com",
        "nome": "Gustavo Bemfica",
        "dtNascimento": "1983-11-09T18:25:43Z",
        "time": {
            "id": 1,
            "nome": "Atlético de Botucatu"
        },
        "campanhaList": [],
    }
    
8.Listar todos os clientes
-

Para listar todos os clientes, use o endpoint `/api/cliente/all` com o método HTTP GET, como no exemplo abaixo:

`GET http://localhost:8080/api/cliente/all`

Output

```
[
  {
    "id": 20,
    "email": "gu.sartori@gmail.com",
    "nome": "Gustavo Sartori Bemfica",
    "dtNascimento": "1983-11-09T18:25:43Z",
    "time": {
      "id": 1,
      "nome": "Atlético de Botucatu"
    },
    "campanhaList": []
  },
  {
    "id": 21,
    "email": "maria_jose@gmail.com",
    "nome": "Maria José",
    "dtNascimento": "1976-11-09T18:25:43Z",
    "time": {
      "id": 2,
      "nome": "Bauru"
    },
    "campanhaList": []
  }
]
```
9.Atualizar Cliente
-

Para atualizar um cliente, use o endpoint `/api/cliente/{id}` , onde o id é o ID do cliente a ser atualizado, com o método HTTP PUT e um JSON no request, como no exemplo abaixo:

`PUT http://localhost:8080/api/campanha`

Body

    {
      "nome": "Gustavo Sartori Bemfica",
      "email": "gu.sartori83@gmail.com",
      "time" : {"id": 1, "nome": "Atlético de Botucatu"},
      "dtNascimento": "1983-11-09T18:25:43Z"
    }

Output

    {
      "id": 20,
      "email": "gu.sartori83@gmail.com",
      "nome": "Gustavo Sartori Bemfica",
      "dtNascimento": "1983-11-09T18:25:43Z",
      "time": {
        "id": 1,
        "nome": "Atlético de Botucatu"
      },
      "campanhaList": []
    }
    
10.Deletar Cliente
-

Para deletar um cliente, use o endpoint `/api/cliente/{id}` , onde o id é o ID do cliente a ser deletado, com o método HTTP DELETE, como no exemplo abaixo:

`DELETE http://localhost:8080/api/cliente/20`

Output

O output será o objeto que foi deletado

    {
      "id": 20,
      "email": "gu.sartori@gmail.com",
      "nome": "Gustavo Sartori Bemfica",
      "dtNascimento": "1983-11-09T18:25:43Z",
      "time": {
        "id": 1,
        "nome": "Atlético de Botucatu"
      },
      "campanhaList": []
    }
    
11.Associar Campanha à Cliente
-

Para associar uma campanha a um cliente, use o endpoint `/api/cliente?idCliente={idCliente}&idCampanha={idCampanha}` , onde o {idCliente} é o ID do cliente a ser associado e {idCampanha} é o ID da campanha a ser associada, com o método HTTP POST, como no exemplo abaixo:

`POST http://localhost:8080/api/cliente?idCliente=10&idCampanha=22`

Output

O output será o objeto cliente com a nova campanha associada. Lembrar que a campanha deve estar vigente e ser do mesmo time do cliente.
```
    {
      "id": 22,
      "email": "gu.sartori@gmail.com",
      "nome": "Gustavo Bemfica",
      "dtNascimento": "1983-11-09T18:25:43Z",
      "time": {
        "id": 1,
        "nome": "Atlético de Botucatu"
      },
      "campanhaList": [
        {
          "id": 10,
          "nome": "Campanha 1",
          "time": 1,
          "dtInicio": "2017-01-16T18:25:43Z",
          "dtFim": "2017-08-16T18:25:43Z"
        }
      ]
    }
```
12.Listar todos os times disponíveis
-

Para listar todos os times, use o endpoint `/api/time/all` com o método HTTP GET, como no exemplo abaixo:

`GET http://localhost:8080/api/time/all`

Output
```
    [
      {
        "id": 1,
        "nome": "Atlético de Botucatu"
      },
      {
        "id": 2,
        "nome": "Bauru"
      },
      {
        "id": 1000,
        "nome": "São Paulo"
      },
      {
        "id": 1001,
        "nome": "Palmeiras"
      },
      {
        "id": 1002,
        "nome": "Santos"
      },
      {
        "id": 1003,
        "nome": "Corinthians"
      }
    ]
    ```