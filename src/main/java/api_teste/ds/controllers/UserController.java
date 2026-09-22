package api_teste.ds.controllers;

import java.net.URI;  //importa a classe uri para construir e manipular HTTP de novos recursos
import org.springframework.beans.factory.annotation.Autowired; //injeçao automatica do spring
import org.springframework.http.ResponseEntity; //importa a classe para montar a resposta http completa (status, headers, corpo da mensagem)
import org.springframework.validation.annotation.Validated; //importa anotacao para habilitar suporte a validação do controller
import org.springframework.web.bind.annotation.DeleteMapping; //mapeia requisições do tipo delete
import org.springframework.web.bind.annotation.GetMapping; //mapeia requisições do tipo get
import org.springframework.web.bind.annotation.PathVariable; //mapeia variaveis passadas diretamente via caminho da URL
import org.springframework.web.bind.annotation.PostMapping; //mapeia requisições do tipo POST
import org.springframework.web.bind.annotation.PutMapping; //mapeia requisições do tipo PUT
import org.springframework.web.bind.annotation.RequestBody; //converte objetos JSON em objeto em JAVA
import org.springframework.web.bind.annotation.RequestMapping; //importa anotação para definir o caminho/rota bas do controlador
import org.springframework.web.bind.annotation.RestController; //importa anotação que define esta classe como um controller REST
import org.springframework.web.servlet.support.ServletUriComponentsBuilder; //importa utilitario para gerar a URI da requisição atual dinamicamente

import api_teste.ds.models.User;
import api_teste.ds.models.User.CreateUser;
import api_teste.ds.models.User.UpdateUser;
import api_teste.ds.services.UserService;

@RestController //define a classe como um controlador REST que retorna respostas em JSON
@RequestMapping ("/user") //define que todas as rotas desta classe terao como prefixo o caminho "/user"
@Validated //ativa a verificação de validações nos parametros recebidos no controller

public class UserController {
    
    @Autowired 
    private UserService userService;

    @GetMapping ("/{id}") //mapeia requisições HTTP GET na rota "/user/{id}"
    public ResponseEntity<User> findById(@PathVariable Long Id){ //metodo para buscar usuario por id capturado na URL
        User obj=this.userService.findById(Id); //invoca a busca do usuario atraves do ID recebido
        return ResponseEntity.ok().body(obj) //retorna codigo HTTP 200(pk) com o objeto User no corpo da resposta
    } //fim do metodo FindById

    @PostMapping 
    public ResponseEntity<Void> create(@Validated (CreateUser.class) @RequestBody User obj){
        this.userService.create(obj);
        URI url = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(obj.getId()).toUri();
            return ResponseEntity.created(url).build();
    }
}