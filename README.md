# FlowTrack — Gestão de Equipes e Produção (versão genérica)

## Novidades desta versão

- **Aba de Gráficos** (só para o Gestor): registros por item, registros por
  colaborador e check-ins por colaborador, em barras horizontais — sem
  biblioteca externa, feito com Compose puro.
- **Visual mais sério**: ícones do Material Design no lugar de emoji em
  toda a interface (barra superior, navegação, alertas, ações).
- **Modo escuro**: alternável a qualquer momento pelo ícone no canto
  superior direito (tela de login e todas as telas internas). A cor muda
  na hora, sem precisar reabrir o app.


Versão adaptada do projeto AgroTech, generalizada para servir **qualquer tipo
de empresa** — indústria, serviços, tecnologia, construção, comércio, ou
agro — não só hortas comunitárias.

## O que mudou em relação à versão AgroTech

| AgroTech (específico) | FlowTrack (genérico) |
|---|---|
| Voluntário | **Colaborador** |
| Administrador | **Gestor** |
| Colheita | **Registro de Produção** |
| Produto fixo (Alface, Tomate...) | **Item em texto livre** (qualquer coisa: peças, atendimentos, relatórios...) |
| Unidade fixa em "kg" | **Unidade configurável** (kg, unidades, horas, litros, m, m², peças, atendimentos ou personalizada) |
| Cor verde "agro" | **Paleta neutra azul-marinho/cinza**, com identidade genérica de dashboard corporativo |
| "AgroTech Monitor" | **"FlowTrack"** |

A estrutura (Room, Compose, navegação) é a mesma — só a terminologia, os
dados de exemplo e a paleta de cores mudaram para não remeter a nenhum
segmento específico.

## Por que isso serve pra qualquer empresa

- **Check-in de presença** funciona igual pra qualquer equipe que trabalhe em
  turnos: fábrica, loja, prestação de serviço, obra, etc.
- **Registro de produção** não tem categoria fixa — o colaborador digita o
  que fez ("peças montadas", "atendimentos", "relatórios entregues",
  "metros instalados"...) e escolhe a unidade. Isso cobre manufatura,
  atendimento, TI, construção, logística, e por aí vai.
- **Cadastro de colaborador** é neutro (nome, contato, disponibilidade,
  observações) — não pressupõe função específica.

## Como abrir no Android Studio

1. Extraia o zip.
2. Abra a pasta `FlowTrack` no Android Studio (File > Open).
3. Aguarde o Gradle Sync.
4. Rode num emulador ou celular (Shift+F10).

## Login de demonstração

- **Gestor**: qualquer usuário/senha.
- **Colaborador**: digite um nome já cadastrado (ex: `Marina`, `Pedro`,
  `Rafael`) — ou qualquer nome novo, que também funciona (sem histórico).

O banco vem com 4 colaboradores e 6 registros de produção de exemplo,
misturando tipos de trabalho diferentes (montagem, atendimento, campo,
embalagem, suporte, instalação) só para mostrar a flexibilidade.

## Personalizando pra sua empresa

Pontos fáceis de trocar:

- **Nome do app**: `strings.xml` (`app_name`) e `LoginScreen.kt` (texto "FlowTrack").
- **Cores**: `ui/theme/Color.kt` — troque `DeepPrimary`, `MidPrimary`, `Gold`
  pelas cores da sua marca.
- **Unidades disponíveis**: lista `units` em `ProductionScreen.kt`.
- **Dados de exemplo (seed)**: `AppDatabase.kt`, função `seedDatabase`.
- **Ícone do app**: ainda não incluído — adicione em `res/mipmap-*`.

## Próximos passos sugeridos

- Autenticação real (hoje é simplificada, sem senha de verdade)
- Múltiplas unidades/filiais por empresa
- Exportação de relatórios (PDF/Excel)
- Notificações de turno
- Permitir o gestor customizar o nome do app, cor e unidades direto na tela
  de configurações, sem precisar editar código
