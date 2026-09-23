package api_teste.ds.controllers;

import java.net.URI; //importa a classe uri para construir e manipular HTTP de novos recursos
import java.util.List; //importa a interface list para manipular onde a classe controller esta localizada
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid; //importa a anotação para acionar a validaçao do corpo de requisiçao
import api_teste.ds.models.Task; //importa a entidade Task do pacote de modelos do projeto
import api_teste.ds.services.TaskService; //importa a caçasse de serviço TaskService do projeto

@RestController // define a classe com um controlador REST que retorna respostas em JSON
@RequestMapping ("/task") //define /task como a rota base de todos do endpoins deste controlador
@Validated //habilita o suporte as validaçoes dentro do controlador

public class TaskController { //declaraçao de classe pibulica TaskController

    @Autowired 
    private TaskService taskService;

    @GetMapping("/{id}") //mapeia requisiçoes HTTP GET na rota "/task/{id}"
    public ResponseEntity<Task> findById(@PathVariable Long id){ //busca tarefa especifica pelo seu ID
        Task obj = this.taskService.findById(id); //chama a camada de serviço para buscar a tarefa pelo seu ID
        return ResponseEntity.ok().body(obj); //retorna HTTP 200(ok)
    } //fim do metodo findById

    @GetMapping("/user/{userid}")
    public ResponseEntity<List<Task>> findAllByUserId(@PathVariable Long userId){
        List<Task> objs = this.taskService.findAllByUserId(userId);
        return ResponseEntity.ok().body(objs);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj){
        this.taskService.create(obj);
        URI url = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(url).build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable Long id){
        obj.setId(id);
        this.taskService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
