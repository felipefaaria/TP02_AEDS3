# Trabalho Pratico 02  -  AEDS3

## Descrição

Neste segundo trabalho prático da disciplina, ampliamos o sistema PUCFlix com novas funcionalidades focadas no cadastro de Atores e na criação de um vínculo muitos-para-muitos com as Séries. A ideia foi permitir que cada série pudesse ter diversos atores associados, e que um mesmo ator pudesse participar de várias produções — como acontece em qualquer catálogo real de streaming.

Mantivemos o padrão MVC que já vínhamos usando desde o TP1, o que ajudou bastante na organização do código. A interface foi adaptada para incluir menus e opções voltadas ao gerenciamento de atores e dos vínculos entre séries e atores. Também nos preocupamos com a consistência: o sistema impede, por exemplo, a exclusão de atores que ainda estejam vinculados a séries, e remove automaticamente os vínculos quando uma série é apagada.

Para representar esse tipo de relacionamento e garantir desempenho nas consultas, adotamos duas Árvores B+. Uma delas guarda os pares (idSerie, idAtor), e a outra (idAtor, idSerie). Com isso, conseguimos recuperar facilmente tanto todos os atores de uma série quanto todas as séries de um ator. Além disso, implementamos as operações básicas (CRUD) para gerenciar os dados de atores,

## Integrantes

-Arthur de Sá
-Daniel Collina
-Felipe Faria

## Experiência

Essa parte do trabalho foi, sem dúvida, mais desafiadora do que a anterior. Tivemos que lidar com a atualização de duas estruturas diferentes ao mesmo tempo, o que exigiu muito cuidado para evitar inconsistências. Por exemplo, quando associamos um ator a uma série, é preciso garantir que o vínculo seja criado corretamente nas duas árvores. Se alguma etapa falhar, isso pode deixar o sistema em um estado incorreto.

Também tivemos que implementar algumas regras extras, como a proibição de apagar um ator que ainda esteja vinculado a alguma série. Para isso, foi necessário fazer buscas específicas na árvore antes de permitir a exclusão. Essas lógicas aumentaram um pouco a complexidade, mas foram importantes para manter a integridade do sistema.

## Testes

Testamos bastante as funcionalidades, tanto manualmente quanto com cenários planejados para forçar erros. Os principais testes envolveram tentar criar vínculos duplicados, excluir séries com múltiplos atores e verificar se os dados estavam sendo refletidos corretamente nas duas direções. No geral, o comportamento foi estável.

Conseguimos finalizar tudo que foi pedido para essa entrega. As funcionalidades estão completas e funcionando.

## Checklist
As operações de inclusão, busca, alteração e exclusão de atores estão implementadas e funcionando corretamente? Sim

O relacionamento entre séries e atores foi implementado com árvores B+ e funciona corretamente, assegurando a consistência entre as duas entidades? Sim

É possível consultar quais são os atores de uma série? Sim

É possível consultar quais são as séries de um ator? Sim

A remoção de séries remove os seus vínculos de atores? Sim

A inclusão de um ator em uma série ou em um episódio se limita aos atores existentes? Sim

A remoção de um ator checa se há alguma série vinculada a ele? Sim

O trabalho está funcionando corretamente? Sim

O trabalho está completo? Sim

O trabalho é original e não a cópia de um trabalho de outro grupo? Sim

## Link TP01
- [TP1](https://github.com/felipefaaria/TP01_AEDS3)

