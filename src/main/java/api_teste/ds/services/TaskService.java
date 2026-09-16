//pacote onde esta a classe de serviço do projeto
package api_teste.ds.services;

//importa list da biblioteca padrao do java para manipular coleções de objetos
import java.util.List;
//importa optional, usado para tratar valores que podem nao estar presentes (evita NullExceptionPointer)
import java.util.Optional;

//importa a anotaçao do Spring para a injeçao automatica de dependencias
import org.springframework.beans.factory.annotation.Autowired;
//importa a anotação que define essa classe como um componente de serviço gerenciado pelo Spring
import org.springframework.stereotype.Service;
//importa a anotaçao para gerenciar transaçoes no banco de dados (garante atomicidade na operaçao)
import org.springframework.transaction.annotation.Transactional;

//importa o models.Task
import api_teste.ds.models.Task;
//importa o models.User
import api_teste.ds.models.User;
//importa a interface do repositorio responsavel pelas operaçoes no banco de dados
import api_teste.ds.repositories.TaskRepository;

import jakarta.persistence.Id;

//anotaçao que indica para o Spring que essa classe contem as regras de negocio
@Service 
public class TaskService {

    //injeta automaticamente a instancia do TaskRepository gerenciado pelo Spring
    @Autowired 
    private TaskRepository taskRepository;

    //injeta automaticamente a instancia do UserService para validar o usuario
    @Autowired 
    private UserService userService;

    //metodo para buscar task apartir do ID
    public Task findById(Long Id){
        //executa a busca no banco, retorna um Optional contendo (ou nao) a Task
        Optional<Task> task = this.taskRepository.findById(Id);

        //se a tarefa existir, retorna o objeto, se estiver vazio, lança um RunTimeException
        return task.orElseThrow(()-> new RuntimeException(
            "Tarefa não encontrada! Id: " + Id + ", Tipo: " + Task.class.getName()
        ));
    }

    //metodo para buscar todas as tarefas vinculadas a um determinado usuario
    public List<Task> findByUserId(Long UserId){
        //chama o userService para garantir que o usuario existe no banco (lança exceçao se nao existir)
        this.userService.findById(UserId);

        //executa a busca customizada no repositorio filtrando pelo id do usuario
        List<Task> tasks = this.taskRepository.findByUserId(UserId);

        //retorna a lista de tarefas
        return tasks;
    }

        //garante que a criaçao ocorra dentro de uma transaçao de banco de dados (rolback automatico se falhar)
        @Transactional
        public Task create(Task obj){
            //valida se o usuario informado no objeto realmente existe no banco e recupera seus dados
            User user = this.userService.findById(obj.getUser().getId());

            //define o id como null para garantir que o JPA realize uma inserçao(INSERT) e nao uma atualização
            obj.setId(Id:null);
            //associa a entidade user completa e validade a tarefa
            obj.setUser(user);
            //salva a nova tarefa no banco de dados e atualiza 'obj' com o ID gerado
            obj = this.taskRepository.save(obj);

            //retorna a tarefa salva
            return obj;
        } 
        
        //garante que a atualização ocorra dentro de transaçõa isolada no banco
        @Transactional 
        public Task update(Task obj){
            //reaproveita o findById para verificar se a tarefa a ser atualizada existe realmente
            Task newObj = findById(obj.getId());
            //atualiza apenas o campo descriçao do objeto persistido com o novo valor
            newObj.setDescription(obj.getDescription());
            //salva a alteração no banco de dados e retorna o objeto atualizado
            return this.taskRepository.save(newObj);
        }

        //metodo para deletar uma tarefa pelo Id
        public void delete(Long Id){
            //verifica se a tarefa existe antes de tentar deletar
            findById(Id);

            try{
                //solicita a remoçao da tarefa no banco de dados pelo Id
                this.taskRepository.deleteById(Id);
            } catch (Exception e){
                //captura execçoes (como violaçoes de chave estrangeira e lança uma mensagem amigavel)
                throw new RuntimeException("Nao é possivel excluir pois nao ha tarefas relacionadas");
            }
        }

    
}

