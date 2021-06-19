Projeto: scheduleTransfer (Agendamento de transferências)

##"Ferramentas" usadas##

Spring Boot 2.5.1 - Uso do Framework open source visando a enorme facilidade dada na montagem de estrutura do projeto
					que se baseia no padrão de inversão de controle e injeção de dependência.
					
Java 1.8      	  - Versão estável mais usada no mercado de trabalho.

Junit 4.13.2      - Incluído no projeto visando o desenvolvimento dos testes unitários.

Mockito       	  - Auxiliador nos testes unitários no controle de Mocks.

Banco H2      	  - Banco em memória utilizado para armazenamento das informações de forma mais fácil e rápidas sem a necessidade de uma base de dados "fisíca".

Swagger 2.9.2 	  - Utilizado para documentar/descrever a API de uma forma que informe os tipos e exemplos de request
					Outro ponto implementado para realizar as requisições diretamente pela "interface" swagger.
					
##Montagem do ambiente##

> Instalar Java 8 (configurar variáveis de ambiente)

> Instalar Maven  (configurar variáveis de ambiente)

> Baixar projeto
	Download zip
	Instalar git
		git clone (git clone https://github.com/rodrigocSantosDev/scheduleTransfer.git)
		
> Baixar dependências:		
	No terminal dentro da pasta do projeto executar a sequência de comandos:
	mvn dependency:resolve
	mvn clean install

> Importar projeto na ferramenta desejada e executar.
		Sugestões: Eclipse e IntelliJ

> Fazer requisições nos endPoints 
	Sugestões: Acessar Swagger para ver os endPoints e simular chamadas ou fazer as requisições via postman.	
					
##Acessos##
> localhost:8080/swagger-ui.html - Swagger.

> localhost:8080/h2 - Acesso ao Banco H2 (url: jdbc:h2:mem:schedulingTransfer, username:sa, password:password).
					Password exposta por se tratar de uma api que acessa banco em memória e execução local.
					
>localhost:8080/cvc/transfer
		{
		  "originAccount": "string",
		  "destinationAccount": "string",
		  "transferDate": "string",
		  "value": 0
		}
		
> localhost:8080/cvc/findAll

> localhost:8080/cvc/findByTransferDate/{date}

> localhost:8080/cvc/findBySchedulingDate/{date}

##Regras##
Objetivo: Simular o agendamento de transferências financeiras, calculando taxas de acordo com as regras abaixo:

> 1) O usuário deve poder agendar uma transferência financeira com as seguintes informações:

Conta de origem (padrão XXXXXX)
Conta de destino (padrão XXXXXX)
Valor da transferência
Taxa (a ser calculada)
Data da transferência (data que será realizada a operação)
Data de agendamento (hoje)

> 2) Cada tipo de transação segue uma regra diferente para cálculo da taxa.
Obs: As taxas abaixo seguem da mais prioritária para a menos.

A: Transferências no mesmo dia do agendamento tem uma taxa de $3 mais 3% do valor a ser
transferido;
B: Transferências até 10 dias da data de agendamento possuem uma taxa de $12 mulƟplicado
pela quanƟdade de dias da data de agendamento até a data de transferência.
C: Operações do Ɵpo C tem uma taxa regressiva conforme a data de transferência:
Acima de 10 até 20 dias da data de agendamento 8%
Acima de 20 até 30 dias da data de agendamento 6%
Acima de 30 até 40 dias da data de agendamento 4%
Acima de 40 dias da data de agendamento e valor superior a 100.000 2%

> 3) O usuário deve poder ver todos os agendamentos cadastrados.