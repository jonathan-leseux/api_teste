package api_teste.ds.configs;

import org.springframework.context.annotation.Configuration; //importa a anotaçao de configuraçao do Spring Container
import org.springframework.web.servlet.config.annotation.CorsRegistry; //importa a classe responsavel por registrar as regras do CORS
import org.springframework.web.servlet.config.annotation.EnableWebMvc; //importa a anotação que habilita os recursos do Spring Web Mvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; //importa a interface de customização do Spring Mvc

@Configuration //indica que essa classe possui configuraçoes de Beans que deve ser inicializos no Spring 
@EnableWebMvc  //importa e ativa o suporte basico as requisiçoes e controladores Web Mvc do Spring

public class WebConfig implements WebMvcConfigurer { //classe de configuraçao que implementa o contrato de customizaçao do Spring
    
    @Override //sobreescreve o metodo de mapeamento CORS padrao da interface WebMvcConfigurer
    public void addCorsMappings(CorsRegistry registry){ //metodo indicado pelo Spring para registrar as regras do CORS
        registry.addMapping("/**"); //libera qualquer rota da API(conringa "/**") para aceitar chamadas externas

        

    }
}
