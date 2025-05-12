
import modelo.*;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import classes.*;

public class MenuAtores {

    ArquivoAtores atoresArq;
    ArquivoSeries seriesArq;
    private static Scanner sc = new Scanner(System.in);

    public MenuAtores() throws Exception {
        atoresArq = new ArquivoAtores();
        seriesArq = new ArquivoSeries();
    }

    public void menu() {

        int select;
        do {

            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Inicio > Atores");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir um ator de uma serie");
            System.out.println("5 - Excluir todos os atores de uma serie");
            System.out.println("6 - Listar todas as series em que um ator atua");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                select = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                select = -1;
            }

            switch (select) {
                case 1:
                    buscarAtor();
                    break;
                case 2:
                    adicionarAtor();
                    break;
                case 3:
                    alterarAtor();
                    break;
                case 4:
                    excluirAtor();
                    break;
                case 5:
                    excluirAtoresPorSerie();
                    break;
                case 6:
                    listarSeriesDeAtor();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (select != 0);
    }

    public void buscarAtor() {

        System.out.println("\nBusca de atores");
        System.out.print("Digite o nome do ator: ");
        String nome = sc.nextLine();

        try {

            Ator[] atores = atoresArq.readNome(nome);

            if (atores != null && atores.length > 0) {
                boolean atorEncontrado = false;

                for (Ator ator : atores) {

                    if (ator != null) {
                        mostrsearchAtortor(ator);
                        atorEncontrado = true;
                    }

                }

                if (!atorEncontrado) {
                    System.out.println("Nenhum ator encontrado com esse nome.");
                }

            } else {
                System.out.println("Ator nao encontrado.");
            }

        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possivel buscar o ator!");
            e.printStackTrace();
        }
    }

    public void adicionarAtor() {

        System.out.println("\n Adicionar ator");

        String nome = "";
        int IDSerie = 0;

        int idadeAtor = 0;
        boolean genero = false;
        char generoChar = ' ';

        Serie[] sa = null;

        do {
            System.out.println("\nQual o nome da serie na qual esse ator atua? ");
            String nomeSerie = sc.nextLine();

            try {

                sa = seriesArq.readNome(nomeSerie);

            } catch (Exception e) {

                System.out.println("Erro do sistema. Não foi possivel buscar a serie!");
                e.printStackTrace();

            }

            if (sa == null) {

                System.out.println("ERRO: Serie nao cadastrada. Tente novamente: ");

            } else {

                IDSerie = sa[0].getID();
            }

        } while (sa == null);

        do {

            System.out.print("\nNome COMPLETO do ator (min. de 1 letras ou vazio para cancelar): ");

            nome = sc.nextLine();

            if (nome.length() == 0) {
                return;
            }

            if (nome.length() < 1) {
                System.err.println("O nome do ator deve ter no minimo 1 caracteres.");
            }

            if (nome.length() > 50) {
                System.err.println("O nome do ator deve ter no maximo 50 caracteres.");
            }

        } while (nome.length() < 1 || nome.length() > 50);

        do {

            System.out.print("Idade: (Numero inteiro positivo): ");

            idadeAtor = sc.nextInt();

            sc.nextLine();

            if (idadeAtor < 0)
                System.out.println("A idade deve ser um numero inteiro e positivo: ");

        } while (idadeAtor < 0);

        do {
            System.out.print("Genero digite F ou M: ");

            generoChar = sc.nextLine().charAt(0);

            if (generoChar == 'F' || generoChar == 'f') {
                genero = true;
            } else if (generoChar == 'M' || generoChar == 'm') {
                genero = false;
            } else {
                System.out.println("O genero deve ser F ou M: ");
            }

        } while (generoChar != 'F' && generoChar != 'f' && generoChar != 'M' && generoChar != 'm');

        System.out.print("\nConfirma a inclusao do ator? (S/N) ");

        char resp = sc.nextLine().charAt(0);

        if (resp == 'S' || resp == 's') {

            try {
                Ator a = new Ator(IDSerie, nome, idadeAtor, genero);
                atoresArq.create(a);
                System.out.println("Ator incluido com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possivel incluir o ator!");
            }
        }
    }

    public void alterarAtor() {

        System.out.println("\nAlteracao de ator");
        System.out.print("Digite o nome do ator: ");
        String nome = sc.nextLine();
        Ator[] searchAtor = null;

        try {

            searchAtor = atoresArq.readNome(nome);

            if (searchAtor != null && searchAtor.length > 0) {

                System.out.println("\nRegistros encontrados para o ator de nome '" + nome + "':");

                for (int i = 0; i < searchAtor.length; i++) {
                    System.out.println("\n[" + (i + 1) + "] ");
                    mostrsearchAtortor(searchAtor[i]);
                }

                System.out.printf("\nSelecione (1-%d) qual registro deseja alterar: ", searchAtor.length);
                int select = sc.nextInt();
                sc.nextLine();

                if (select < 1 || select > searchAtor.length) {
                    System.out.println("select invalida.");
                    return;
                }

                Ator ator = searchAtor[select - 1];
                System.out.println("Registro do ator escolhido:");
                mostrsearchAtortor(ator);

                System.out.print("\nNovo nome (deixe em branco para manter o anterior): ");
                String novoNome = sc.nextLine();
                if (!novoNome.isEmpty()) {
                    ator.setNomeAtor(novoNome);
                }

                System.out.print("Nova idade (deixe em branco para manter a anterior): ");
                String novaIdade = sc.nextLine();
                if (!novaIdade.isEmpty()) {

                    try {
                        ator.setIdadeAtor(Integer.parseInt(novaIdade));
                    } catch (NumberFormatException e) {
                        System.err.println("Idade invalida. Valor mantido.");
                    }
                }

                System.out.print("Novo genero (F/M) (deixe em branco para manter o anterior): ");
                String novoGenero = sc.nextLine();

                if (!novoGenero.isEmpty()) {

                    try {

                        if (novoGenero.equalsIgnoreCase("F")) {
                            ator.setGenero(true);
                        } else if (novoGenero.equalsIgnoreCase("M")) {
                            ator.setGenero(false);
                        }

                    } catch (Exception e) {

                        System.err.println("Genero invalido. Valor mantido.");

                    }
                }

                System.out.print("\nConfirma as alteracoes? (S/N) ");

                char resp = sc.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {

                    boolean alterado = atoresArq.update(ator);

                    if (alterado) {
                        System.out.println("Ator alterado com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar o ator.");
                    }

                } else {

                    System.out.println("Alteracoes canceladas.");

                }

            } else {
                System.out.println("Nenhum cadastro de ator encontrado com esse nome.");
            }
        } catch (Exception e) {

            System.out.println("Erro do sistema. Nao foi possivel alterar o ator!");
            e.printStackTrace();

        }
    }

    public void excluirAtor() {

        System.out.println("\nExclusão de ator");
        System.out.print("Digite o nome do ator: ");
        String nome = sc.nextLine();
        Ator[] searchAtor = null;

        try {

            searchAtor = atoresArq.readNome(nome);

            if (searchAtor != null && searchAtor.length > 0) {

                System.out.println("\nRegistros encontrados do ator com o nome '" + nome + "':");
                for (int i = 0; i < searchAtor.length; i++) {
                    System.out.println("\n[" + (i + 1) + "] ");
                    mostrsearchAtortor(searchAtor[i]);
                }

                System.out.printf("\nSelecione (1-%d) qual registro do ator deseja excluir: ", searchAtor.length);
                int select = sc.nextInt();
                sc.nextLine();

                if (select < 1 || select > searchAtor.length) {
                    System.out.println("select invalida.");
                    return;
                }

                Ator ator = searchAtor[select - 1];
                System.out.print("\nConfirma a exclusao do ator? (S/N) ");
                char resp = sc.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {

                    boolean excluido = atoresArq.delete(ator.getID());

                    if (excluido) {
                        System.out.println("Registro de ator excluido com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir o ator.");
                    }

                } else {
                    System.out.println("Exclusao cancelada.");
                }

            } else {
                System.out.println("Nenhum registro de ator encontrado com esse nome.");
            }

        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o ator!");
            e.printStackTrace();
        }
    }

    public void mostrsearchAtortor(Ator ator) {

        if (ator != null) {

            System.out.println("\nDetalhes do Ator:");
            System.out.println("----------------------");

            try {
                Serie s = seriesArq.read(ator.getIDSerie());
                System.out.printf("Serie........: %s\n", (s != null ? s.getNome() : "Serie não encontrada"));

            } catch (Exception e) {
                System.out.println("Erro: nao foi possível buscar a serie na qual o ator atua.");
            }

            System.out.printf("ID do registro: %d\n", +ator.getID());
            System.out.printf("Nome..........: %s\n", ator.getNomeAtor());
            System.out.printf("Idade.........: %d\n", ator.getIdadeAtor());
            System.out.printf("Genero........: %s\n", (ator.getGenero() ? "Feminino" : "Masculino"));

            System.out.println("----------------------");
        }
    }

    public void excluirAtoresPorSerie() {

        System.out.print("Digite o nome da serie para excluir todos os atores que trabalham nela: ");
        String nomeSerie = sc.nextLine();

        try {

            Serie[] series = seriesArq.readNome(nomeSerie);

            if (series == null || series.length == 0) {
                System.out.println("Serie não encontrada.");
                return;
            }

            Serie serie = series[0];
            System.out.println("Serie encontrada.");
            System.out.printf("Nome da serie: %s\n", serie.getNome());

            System.out.print("\nConfirma a exclusao de todos os atores da serie? (S/N) ");
            char resp = sc.nextLine().charAt(0);

            if (resp == 'S' || resp == 's') {

                boolean encontrouErro = false;

                while (true) {

                    Ator[] atores = atoresArq.readPorSerie(serie.getID());

                    if (atores == null || atores.length == 0) {
                        break;
                    }

                    for (Ator ator : atores) {
                        try {

                            System.out.printf("Tentando excluir o ator '%s'...\n", ator.getNomeAtor());
                            boolean excluido = atoresArq.delete(ator.getID());

                            if (excluido) {
                                System.out.printf("Ator '%s' excluido com sucesso.\n", ator.getNomeAtor());
                            } else {
                                System.out.printf("Erro ao excluir o ator '%s'.\n", ator.getNomeAtor());
                                encontrouErro = true;
                            }

                        } catch (Exception e) {
                            System.out.printf("Erro ao excluir o ator '%s': %s\n", ator.getNomeAtor(), e.getMessage());
                            e.printStackTrace();
                            encontrouErro = true;
                        }
                    }
                }

                if (!encontrouErro) {
                    System.out.println("Todos os atores da serie foram excluidos com sucesso.");
                } else {
                    System.out.println("ERRO. Alguns atores nao puderam ser excluidos.");
                }
            } else {
                System.out.println("Exclusao cancelada.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao excluir atores da serie!");
            e.printStackTrace();
        }
    }

    public void listarSeriesDeAtor() {

        System.out.println("\nListagem de series por ator");
        System.out.print("Digite o nome do ator: ");
        String nomeAtor = sc.nextLine();

        try {

            Ator[] atores = atoresArq.readNome(nomeAtor);

            if (atores == null || atores.length == 0) {
                System.out.println("Ator nao encontrado.");
                return;
            }

            Ator ator = atores[0];
            System.out.println("\nAtor encontrado:");
            System.out.println("----------------------------");
            System.out.println("Nome: " + ator.getNomeAtor());
            System.out.println("Idade: " + ator.getIdadeAtor());
            System.out.println("Genero: " + (ator.getGenero() ? "Feminino" : "Masculino"));

            Set<Integer> seriesIDs = new HashSet<>();

            for (Ator at : atores) {
                if (at != null) {
                    seriesIDs.add(at.getIDSerie());
                }
            }

            System.out.println("\nSeries em que o ator trabalha:");
            for (int idSerie : seriesIDs) {
                try {
                    Serie serie = seriesArq.read(idSerie);
                    if (serie != null) {
                        System.out.println("- " + serie.getNome());
                    } else {
                        System.out.println("- Serie nao encontrada para o IDSerie: " + idSerie);
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao buscar a serie com IDSerie: " + idSerie);
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {

            System.out.println("Erro ao listar series do ator!");
            e.printStackTrace();

        }
    }

}
