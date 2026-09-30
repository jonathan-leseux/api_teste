//url base da API Spring Boot para buscar as tarefas do usuario de ID 1
const url = "http://localhost:8080/task/user/1";

//função responsavel por ocultar o icone de carregamento
function hideLoader(){

    //busca o elemento HTML com o Id 'loading' e altera seu destino de exibição para oculta-lo
    document.getElementById("loading").style.display = "none";

}

//funçao responsavel por construir o html da tabela e preenche-lo com as tarefas
function show(task){
    //cria uma string contendo o cabeçalho da tabela utilizando template Literals
    let tab =`
    <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Descrição</th>
            <th scope="col">Usuário</th>
            <th scope="col">User ID</th>
        </tr>
    </thead>
    `;

    //interador 'for...of'
    for(let task of tasks){
        //concatena uma nova linha html(<tr>) com as colunas(<td>) preenchidas
        tab += `
        <tr>
            <td scope="row">${task.id}</td>
            <td>${task.description}</td>
            <td>${task.user.username}</td>
            <td>${task.user.id}</td>        
        </tr>
        `;
    }
    //injeta a string acumulada diretamente na tabela atraves do ID 'tasks'
    document.getElementById("tasks").innerHTML = tab
    //funçao assincronada encarregada de realizar a requisição HTTP GET para a API
    async function getAPI(url) {
        
        //executa a requisição HTTP usando a API nativa fetch() e aguarda(wait) a resposta da rede
        const response = await fetch(url,{method:"GET"});

        //converte o corpo da resposta HTTP e formato JSON e guarda na variavel
        var data = await response.json();

        //se a resposta for obtida com sucesso, executa a funçao de ocultar o carregamento
        if(response){
            hideLoader();
        }
    
    }
    show(data);
}
getAPI(url);