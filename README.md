# 🎬 Locadora System - Sistema de Gestão de Locadora de VHS

## 📋 Sobre o Projeto

Sistema completo de PDV (Ponto de Venda) para locadora de fitas VHS, desenvolvido com **Java 17** e **Spring Boot 3.1.5**. O sistema gerencia todo o ciclo operacional de uma locadora física: desde o cadastro de clientes e filmes, controle de fitas físicas, locações, devoluções, cálculo de multas, até relatórios gerenciais e fechamento de caixa.

### 🎯 Funcionalidades Principais

- **Gestão de Clientes**: Cadastro, consulta, bloqueio por inadimplência e verificação de pendências
- **Gestão de Filmes**: Cadastro manual e importação automática via API OMDb
- **Gestão de Fitas**: Controle individual de fitas físicas com status (Disponível, Alugada, Danificada, Perdida)
- **Operações de Locação**: Aluguel múltiplo, renovação de prazo, devolução com cálculo automático de multas
- **Gestão de Multas**: Configuração flexível (valor fixo, porcentagem diária, porcentagem fixa)
- **Checkout Financeiro**: Cálculo de pendências e processamento de pagamentos
- **Dashboard Gerencial**: Ranking de filmes mais alugados e relatório diário de fechamento
- **Monitoramento**: Métricas Prometheus, health checks e logs otimizados

---

## 🛠️ Tecnologias e Ferramentas

### Backend Core
| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.1.5 | Framework principal |
| Spring Security | 3.1.5 | Autenticação e autorização JWT |
| Spring Data JPA | 3.1.5 | Persistência de dados |
| Hibernate | 6.2.13 | ORM (Mapeamento Objeto-Relacional) |
| Flyway | 9.16.3 | Migração e versionamento de banco |

### Banco de Dados e Cache
| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| PostgreSQL | 15+ | Banco de dados relacional |
| Redis | 7+ | Cache distribuído |
| HikariCP | 5.0+ | Pool de conexões otimizado |

### Integração e APIs
| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| OpenFeign | 4.0+ | Cliente HTTP declarativo |
| OMDb API | - | Importação de dados de filmes |

### Utilitários
| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Lombok | 1.18.30 | Redução de boilerplate code |
| JJWT | 0.11.5 | Geração e validação de tokens JWT |
| Bean Validation | 3.0+ | Validação de dados |
| Maven | 3.8.5 | Gerenciador de dependências |

### DevOps e Monitoramento
| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Docker | 24+ | Containerização |
| Docker Compose | 2.20+ | Orquestração de containers |
| Spring Actuator | 3.1.5 | Monitoramento e métricas |
| Prometheus | - | Exportação de métricas |

---

## 📐 Práticas e Padrões de Desenvolvimento

### Princípios de Design
- **KISS (Keep It Simple, Stupid)**: Código simples e direto, sem complexidade desnecessária
- **DRY (Don't Repeat Yourself)**: Centralização de lógica reutilizável (FineService, CheckoutService)
- **YAGNI (You Aren't Gonna Need It)**: Implementação apenas de funcionalidades essenciais para o PDV
- **Object Calisthenics**: Métodos curtos (<15 linhas), nomes expressivos, encapsulamento adequado
- **Clean Code**: Organização clara de pacotes, injeção de dependência via Lombok

### Arquitetura
- **Camadas bem definidas**: Controller → Service → Repository
- **DTO implícito**: Uso direto das entidades (simplicidade para o escopo)
- **Injeção de dependência**: Via construtor com `@RequiredArgsConstructor`
- **Transações gerenciadas**: `@Transactional` nos serviços

### Segurança
- **JWT Stateless**: Tokens para autenticação sem estado
- **BCrypt**: Criptografia de senhas
- **Role-based access**: Controle por perfis (ADMIN, MANAGER, EMPLOYEE)
- **Rate limiting**: Proteção contra ataques de força bruta

### Performance
- **Connection Pool**: HikariCP otimizado (max 10 conexões)
- **Batch Processing**: Hibernate com batch_size=20
- **Redis Cache**: Cache de consultas frequentes (filmes)
- **GZIP Compression**: Compressão de respostas HTTP
- **Lazy Loading**: Carregamento sob demanda de relacionamentos

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- Docker e Docker Compose (recomendado)
- ou Java 17 + Maven 3.8+ + PostgreSQL 15+ + Redis 7+

### Opção 1: Docker Compose (Recomendado)

```bash
# 1. Clonar o repositório
git clone https://github.com/seu-usuario/locadora-system.git
cd locadora-system

# 2. Configurar variáveis de ambiente
cp .env.example .env
# Editar .env with suas credenciais

# 3. Subir os containers
docker compose up --build

# 4. Acessar a aplicação
curl http://localhost:8080/actuator/health
```

### Opção 2: Execução Local

```bash
# 1. Configurar PostgreSQL e Redis
sudo systemctl start postgresql
sudo systemctl start redis-server

# 2. Criar banco de dados
createdb -U locadora_user locadora_db

# 3. Configurar variáveis de ambiente
export DB_HOST=localhost
export DB_USERNAME=locadora_user
export DB_PASSWORD=locadora_password
export OMDB_API_KEY=sua_chave_api

# 4. Executar a aplicação
./mvnw spring-boot:run
```

---

## 📚 Endpoints Principais

### Autenticação
```bash
# Registrar usuário
POST /api/v1/auth/register
{
  "username": "admin",
  "email": "admin@locadora.com",
  "password": "Admin@123",
  "role": "ADMIN"
}

# Login
POST /api/v1/auth/login
{
  "username": "admin",
  "password": "Admin@123"
}
# Retorna: {"token": "eyJhbGciOiJ..."}
```

### Clientes
```bash
# Criar cliente (requer ADMIN)
POST /api/v1/customers
Authorization: Bearer ${TOKEN}

# Listar clientes
GET /api/v1/customers

# Ver pendências
GET /api/v1/customers/{id}/pending
```

### Filmes
```bash
# Importar do OMDb
POST /api/v1/movies/import/{imdbId}

# Listar filmes disponíveis
GET /api/v1/movies/available
```

### Locações
```bash
# Alugar fita
POST /api/v1/rentals?customerId={id}&tapeIds={tapeId}&days=3

# Devolver
PUT /api/v1/rentals/{rentalId}/return?rewound=true

# Renovar
PUT /api/v1/rentals/{rentalId}/renew?extraDays=2
```

### Dashboard
```bash
# Filmes mais alugados
GET /api/v1/dashboard/most-rented?limit=10

# Relatório diário
GET /api/v1/dashboard/daily-report?date=2024-01-15
```

---

## 🗄️ Estrutura do Banco de Dados

### Principais Tabelas

```sql
-- Usuários do sistema
users (id, username, email, password, role, active)

-- Clientes
customers (id, name, document, email, phone, address, active)

-- Filmes
movies (id, title, imdb_id, director, genre, release_year, daily_rate)

-- Fitas físicas
tapes (id, movie_id, status)  -- status: AVAILABLE, RENTED, DAMAGED, LOST

-- Locações
rentals (id, customer_id, tape_id, rental_date, expected_return_date, 
         total_amount, fine_amount, status, rewound)

-- Multas
fines (id, name, fine_type, value, active)  -- fine_type: FIXED_AMOUNT, DAILY_PERCENTAGE, FIXED_PERCENTAGE

-- Danos
damage_records (id, rental_id, damage_type, damage_cost, paid)
```

### Migrações Flyway
- `V1__create_initial_schema.sql`: Estrutura base
- `V2__create_tapes_table.sql`: Tabela de fitas
- `V3__fix_imdb_rating_type.sql`: Correção de tipo

---

## 📊 Monitoramento e Métricas

### Endpoints Actuator

| Endpoint | Descrição |
|----------|-----------|
| `/actuator/health` | Status da aplicação |
| `/actuator/info` | Informações do build |
| `/actuator/metrics` | Métricas da JVM e aplicação |
| `/actuator/prometheus` | Métricas no formato Prometheus |

### Exemplo de Métricas
```bash
# Requests por segundo
curl http://localhost:8080/actuator/metrics/http.server.requests

# Uso de memória
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Conexões ativas do banco
curl http://localhost:8080/actuator/metrics/hikaricp.connections.active
```

---

## 🧪 Testando o Sistema

### Script de Teste Automatizado

```bash
#!/bin/bash
# test_api.sh - Testa todas as funcionalidades

# 1. Registrar admin
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@test.com","password":"Admin@123","role":"ADMIN"}'

# 2. Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Admin@123"}' \
  | jq -r '.token')

# 3. Importar filme
curl -X POST "http://localhost:8080/api/v1/movies/import/tt0111161" \
  -H "Authorization: Bearer ${TOKEN}"

# 4. Criar cliente
CUSTOMER_ID=$(curl -s -X POST http://localhost:8080/api/v1/customers \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"name":"João Silva","document":"123.456.789-00","email":"joao@email.com"}' \
  | jq -r '.id')

echo "✅ Testes concluídos! Cliente ID: ${CUSTOMER_ID}"
```

---

## 🔒 Variáveis de Ambiente

```bash
# Banco de Dados
DB_HOST=localhost
DB_NAME=locadora_db
DB_USERNAME=locadora_user
DB_PASSWORD=locadora_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_DATABASE=0
REDIS_USERNAME=default
REDIS_PASSWORD=redis_password

# APIs Externas
OMDB_API_KEY=sua_chave_omdb

# Segurança JWT
JWT_SECRET=chave_super_secreta_com_pelo_menos_32_bytes
JWT_EXPIRATION=86400000

# Server
PORT=8080
```

---

## 📈 Performance e Otimizações

### Configurações de Performance Implementadas

| Configuração | Valor | Benefício |
|-------------|-------|-----------|
| HikariCP max pool size | 10 | Conexões simultâneas otimizadas |
| Hibernate batch_size | 20 | Updates em lote |
| Redis connection pool | 8 | Cache eficiente |
| GZIP Compression | ativo | 70% menos banda |
| Tomcat max threads | 200 | Concorrência otimizada |
| Log level | INFO/WARN | Redução de I/O |

---

## 🐛 Troubleshooting

### Erro: Connection refused ao PostgreSQL
```bash
# Verificar se PostgreSQL está rodando
sudo systemctl status postgresql

# Verificar se o banco existe
psql -l | grep locadora_db

# Criar banco se necessário
createdb -U locadora_user locadora_db
```

### Erro: Token JWT inválido
```bash
# Verificar se o token está no formato correto
# Deve ser: "Authorization: Bearer eyJhbGciOiJ..."

# Verificar expiração do token
# Tokens expiram após 24 horas (configurável)
```

### Erro: Redis connection failed
```bash
# Testar conexão Redis
redis-cli ping

# Verificar senha no .env
# Se não tiver senha, deixar REDIS_PASSWORD vazio
```

---

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

### Padrões de Commit
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação
- `refactor`: Refatoração
- `test`: Testes
- `chore`: Mautenção

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👥 Autores

- **Bruno** - *Desenvolvimento inicial* - [GitHub](https://github.com/seu-usuario)

---

## 🎯 Roadmap Futuro

- [ ] Interface web com React/Angular
- [ ] Reserva de filmes online
- [ ] Envio de e-mail de cobrança automático
- [ ] Relatórios avançados (PDF/Excel)
- [ ] Integração com gateway de pagamento
- [ ] Aplicativo mobile para clientes
- [ ] Chatbot para atendimento

---

**Desenvolvido com ☕ e dedicação**
