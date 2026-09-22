//declara o caminho onde a classe esta dentro do codigo
package api_teste.ds.services;

//importa optional, usado para tratar valores que podem nao estar presentes (evita NullExceptionPointer)
import java.util.Optional;

//importa a anotaçao do Spring para a injeçao automatica de dependencias
import org.springframework.beans.factory.annotation.Autowired;
//importa a anotação que define essa classe como um componente de serviço gerenciado pelo Spring
import org.springframework.stereotype.Service;
//importa a anotaçao para gerenciar transaçoes no banco de dados (garante atomicidade na operaçao)
import org.springframework.transaction.annotation.Transactional;

//importa o models.User
import api_teste.ds.models.User;
//importa o models.Task
import api_teste.ds.models.Task;
//importa a interface do repositorio responsavel pelas operaçoes no banco de dados
import api_teste.ds.repositories.TaskRepository;
//importa a interface do repositorio responsavel pelas operaçoes no banco de dados
import api_teste.ds.repositories.UserRepository;

//anotaçao que indica no Spring que essa classe contem as regras de negocios da entidade user
@Service 
public class UserService {

    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private TaskRepository taskRepository;

    public User findById(Long Id){

        Optional<User> user = this.userRepository.findById(Id);
        
        return user.orElseThrow(()-> new RuntimeException(
            "Usuário não encontrado! Id: " + Id + ", Tipo: " + user.class.getName()
        ));
    }
    @Transactional 
    public User create(User obj){

        obj.setId(null);

        obj = this.userRepository.save(obj);

        this.taskRepository.saveAll(obj.getClass());

        return obj;
    } 

    @Transactional 

    public User update(User obj){
        
        User newObj = findById(obj.getId());

        newObj.setPassword(obj.getPassword());

        return this.userRepository.save(newObj);
    }
    
    public void delete(Long Id){

        findById(Id);

        try{
            this.userRepository.deleteById(Id);
        } catch (Exception e){
            throw new RuntimeException("Não é possivel exibir pois há entidade relacionada");
        }
    }
}
