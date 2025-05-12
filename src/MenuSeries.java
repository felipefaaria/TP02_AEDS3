
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import classes.Ator;
import classes.Episodio;
import classes.Serie;
import modelo.ArquivoEpisodios;
import modelo.ArquivoSeries;
import modelo.ArquivoAtores;

public class MenuSeries {
    ArquivoSeries seriesArq;
    ArquivoEpisodios episodiosArq;
    ArquivoAtores atoresArq;

    private static Scanner sc = new Scanner(System.in);

    public MenuSeries() throws Exception {
        seriesArq = new ArquivoSeries();

        episodiosArq = new ArquivoEpisodios();

        atoresArq = new ArquivoAtores();
    }

    public void menu() {
        int select;
        do {
            System.out.println("\n\nAEDsIII");
            System.out.println("-------");
            System.out.println("> Inicio > Series");
            System.out.println("\n1 - Buscar");
            System.out.println("2 - Incluir");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("5 - Listar todos episodios da serie");
            System.out.println("6 - Listar episodios por temporada");
            System.out.println("7 - Listar todas as series cadastradas");
            System.out.println("8 - Listar todos os atores de uma serie");
            System.out.println("0 - Voltar");

            System.out.print("\nOpcao: ");
            try {
                select = Integer.valueOf(sc.nextLine());
            } catch (NumberFormatException e) {
                select = -1;
            }

            switch (select) {
                case 1:
                    buscarSerie();
                    break;
                case 2:
                    incluirSerie();
                    break;
                case 3:
                    alterarSerie();
                    break;
                case 4:
                    excluirSerie();
                    break;
                case 5:

                    listarEpisodiosPorSerie();
                    break;
                case 6:

                    listarEpisodiosPorTemporada();
                    break;
                case 7:

                    listarTodasSeries();
                    break;
                case 8:

                    listarAtoresPorSerie();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }

        } while (select != 0);
    }

    public void buscarSerie() {
        System.out.println("\nBusca de serie");
        String nome;

        System.out.print("\nNome: ");
        nome = sc.nextLine();

        try {
            Serie[] series = seriesArq.readNome(nome);

            if (series != null && series.length > 0) {

                for (Serie serie : series) {
                    mostraSerie(serie);
                }

            } else {

                System.out.println("Serie nao encontrada.");

            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possivel buscar o serie!");
            e.printStackTrace();
        }
    }

    public void listarEpisodiosPorSerie() {

        System.out.println("\nListagem de episodios");
        String nome;

        System.out.print("\nNome da serie: ");
        nome = sc.nextLine();

        try {

            Serie[] series = seriesArq.readNome(nome);

            if (series == null || series.length == 0) {

                System.out.println("Serie nao encontrada.");
                return;

            }

            Serie s = series[0];
            System.out.println("Serie encontrada:");

            int idSerie = s.getID();

            Episodio[] episodios = episodiosArq.readPorSerie(idSerie);

            if (episodios == null || episodios.length == 0) {
                System.out.println("Nenhum episodio encontrado para esta serie.");
                return;
            }

            System.out.println("\nEpisodios da serie:");
            for (Episodio episodio : episodios) {
                System.out.println("----------------------------");
                System.out.println("Nome: " + episodio.getNome());
                System.out.println("Temporada: " + episodio.getTemporada());
                System.out.println("Duracao: " + episodio.getDuracao() + " minutos");
                System.out.println("Data de Lancamento: " + episodio.getLancamento());
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar episodios da serie!");
            e.printStackTrace();
        }
    }

    public void listarAtoresPorSerie() {

        System.out.println("\nListagem de atores por serie");
        String nome;

        System.out.print("\nNome da serie: ");
        nome = sc.nextLine();
        try {

            Serie[] series = seriesArq.readNome(nome);

            if (series == null || series.length == 0) {
                System.out.println("Serie nao encontrada.");
                return;
            }

            Serie s = series[0];
            System.out.println("Serie encontrada:");
            mostraSerie(s);

            int idSerie = s.getID();

            Ator[] atores = atoresArq.readPorSerie(idSerie);

            if (atores == null || atores.length == 0) {
                System.out.println("Nenhum ator encontrado para esta serie.");
                return;
            }

            System.out.println("\nAtores da serie:");
            for (Ator ator : atores) {
                System.out.println("----------------------------");
                System.out.println("Nome: " + ator.getNomeAtor());
                System.out.println("Idade: " + ator.getIdadeAtor());
                System.out.println("Genero: " + (ator.getGenero() ? "Feminino" : "Masculino"));
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar atores da serie!");
            e.printStackTrace();
        }
    }

    public void listarEpisodiosPorTemporada() {

        System.out.println("\nListagem de episodios por temporada");
        String nomeSerie;

        System.out.print("\nNome da serie: ");
        nomeSerie = sc.nextLine();

        try {

            Serie[] series = seriesArq.readNome(nomeSerie);

            if (series == null || series.length == 0) {
                System.out.println("Serie nao encontrada.");
                return;
            }

            Serie serie = series[0];
            System.out.println("Serie encontrada:");
            mostraSerie(serie);

            System.out.print("\nDigite o número da temporada desejada: ");
            int temporadaDesejada = Integer.parseInt(sc.nextLine());

            Episodio[] episodios = episodiosArq.readPorSerie(serie.getID());

            if (episodios == null || episodios.length == 0) {
                System.out.println("Nenhum episodio encontrado para esta serie.");
                return;
            }

            System.out.println("\nEpisodios da temporada " + temporadaDesejada + ":");
            boolean encontrouEpisodios = false;
            for (Episodio episodio : episodios) {
                if (episodio.getTemporada() == temporadaDesejada) {
                    System.out.println("----------------------------");
                    System.out.println("Nome: " + episodio.getNome());
                    System.out.println("Temporada: " + episodio.getTemporada());
                    System.out.println("Duracao: " + episodio.getDuracao() + " minutos");
                    System.out.println("Data de Lancamento: " + episodio.getLancamento());
                    encontrouEpisodios = true;
                }
            }

            if (!encontrouEpisodios) {
                System.out.println("Nenhum episodio encontrado para a temporada " + temporadaDesejada + ".");
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar episodios da temporada!");
            e.printStackTrace();
        }
    }

    public void incluirSerie() {
        String nome = "";
        String sinopse = "";
        String streaming = "";
        LocalDate dataLancamento = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("\nInclusao de serie");

        do {
            System.out.print("\nNome (min. de 3 letras ou vazio para cancelar): ");
            nome = sc.nextLine();
            if (nome.length() == 0) {
                return;
            }
            if (nome.length() < 3) {
                System.err.println("O nome da serie deve ter no minimo 3 caracteres.");
            }
        } while (nome.length() < 3);

        try {

            Serie[] series = seriesArq.readNome(nome);

            if (series != null && series.length > 0) {
                System.err.println("Uma serie com esse nome ja existe");
                return;
            } else {

                do {
                    System.out.print("Sinopse (no minimo 10 digitos): ");

                    sinopse = sc.nextLine();
                    if (sinopse.length() < 10) {
                        System.err.println("A sinopse deve ter no minimo 10 digitos.");
                    }

                } while (sinopse.length() < 10);

                do {
                    System.out.print("Streaming: (no minimo 3 digitos): ");

                    streaming = sc.nextLine();

                    if (streaming.length() < 3) {
                        System.err.println("O streaming deve ter no minimo 3 digitos.");
                    }

                } while (streaming.length() < 3);

                boolean dadosCorretos = false;
                do {
                    System.out.print("Data de lancamento (DD/MM/AAAA): ");
                    String dataStr = sc.nextLine();

                    try {
                        dataLancamento = LocalDate.parse(dataStr, formatter);
                        dadosCorretos = true;
                    } catch (Exception e) {
                        System.err.println("Data invalida! Use o formato DD/MM/AAAA.");
                    }

                } while (!dadosCorretos);

                System.out.print("\nConfirma a inclusao da serie? (S/N) ");

                char resp = sc.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {
                    try {
                        Serie s = new Serie(nome, dataLancamento, sinopse, streaming);
                        seriesArq.create(s);
                        System.out.println("Serie incluida com sucesso.");
                    } catch (Exception e) {
                        System.out.println("Erro do sistema. Nao foi possivel incluir a serie!");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possivel buscar a serie!");
            e.printStackTrace();
        }

    }

    public void alterarSerie() {
        System.out.println("\nAlteracao de serie");
        String nome = "";
        boolean nomeValido = false;

        do {
            System.out.print("\nNome (3 digitos): ");
            nome = sc.nextLine();

            if (nome.isEmpty()) {
                return;
            }

            // Validacao do nome
            if (nome.length() > 2) {
                nomeValido = true;
            } else {
                System.out.println("Nome invalido. O nome deve conter no minimo 3 digitos.");
            }

        } while (!nomeValido);

        try {

            Serie[] s = seriesArq.readNome(nome);

            if (s == null || s.length == 0) {
                return;
            }

            Serie serie = s[0];

            if (serie != null) {
                System.out.println("Serie encontrada:");
                mostraSerie(serie);

                Episodio[] epVinculados = episodiosArq.readPorSerie(serie.getID());

                Ator[] atoresVinculados = atoresArq.readPorSerie(serie.getID());

                if ((epVinculados != null && epVinculados.length > 0)
                        || (atoresVinculados != null && atoresVinculados.length > 0)) {

                    if (epVinculados != null && epVinculados.length > 0) {
                        System.out.println(
                                "Nao e possivel alterar o nome da serie pois existem episodios ligados a ela.");
                        System.out.println("Exclua primeiro todos os episodios dessa serie no menu EPISODIOS.");
                    }

                    if (atoresVinculados != null && atoresVinculados.length > 0) {
                        System.out.println("Nao e possivel alterar o nome da serie pois existem atores ligados a ela.");
                        System.out.println("Exclua primeiro todos os atores dessa serie no menu ATORES.");
                    }

                } else {

                    System.out.print("\nNovo nome (deixe em branco para manter o anterior): ");
                    String novoNome = sc.nextLine();

                    if (!novoNome.isEmpty()) {

                        Serie[] seriesComMesmoNome = seriesArq.readNome(novoNome);
                        if (seriesComMesmoNome != null && seriesComMesmoNome.length > 0) {
                            System.out.println("Erro: Ja existe uma serie registrada com este nome.");
                            return;
                        }

                        serie.setNome(novoNome);
                    }
                }

                System.out.print("Nova sinopse (deixe em branco para manter o anterior): ");
                String novaSinopse = sc.nextLine();

                if (!novaSinopse.isEmpty()) {
                    serie.setSinopse(novaSinopse);
                }

                System.out.print("Novo streaming (deixe em branco para manter o anterior): ");
                String novoStreaming = sc.nextLine();

                if (!novoStreaming.isEmpty()) {
                    serie.setStreaming(novoStreaming);
                }

                System.out.print("Nova data de lancamento (DD/MM/AAAA) (deixe em branco para manter a anterior): ");
                String novaDataLancamento = sc.nextLine();

                if (!novaDataLancamento.isEmpty()) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        serie.setLancamento(LocalDate.parse(novaDataLancamento, formatter));
                    } catch (Exception e) {
                        System.err.println("Data invalida. Valor mantido.");
                    }
                }

                System.out.print("\nConfirma as alteracoes? (S/N) ");

                char resp = sc.nextLine().charAt(0);

                if (resp == 'S' || resp == 's') {

                    boolean alterado = seriesArq.update(serie);

                    if (alterado) {
                        System.out.println("Serie alterada com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar a serie.");
                    }
                } else {
                    System.out.println("Alteracoes canceladas.");
                }
            } else {
                System.out.println("Serie nao encontrada.");
            }

        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possivel alterar o serie!");
            e.printStackTrace();
        }
    }

    public void excluirSerie() {
        System.out.println("\nExclusao de serie");
        String nome;
        boolean nomeValido = false;

        do {
            System.out.print("\nNome (3 digitos): ");
            nome = sc.nextLine();
            if (nome.isEmpty()) {
                return;
            }
            if (nome.length() > 2) {
                nomeValido = true;
            } else {
                System.out.println("Nome invalido. O nome deve conter no minimo 3 digitos.");
            }
        } while (!nomeValido);

        try {

            Serie[] s = seriesArq.readNome(nome);
            if (s == null || s.length == 0) {
                System.out.println("Serie nao encontrada.");
                return;
            }

            Serie serie = s[0];

            Episodio[] epVinculados = episodiosArq.readPorSerie(serie.getID());

            Ator[] atoresVinculados = atoresArq.readPorSerie(serie.getID());

            if ((epVinculados != null && epVinculados.length > 0)
                    || (atoresVinculados != null && atoresVinculados.length > 0)) {

                if (epVinculados != null && epVinculados.length > 0) {
                    System.out.println("Nao e possivel excluir a serie pois existem episodios ligados a ela.");
                    System.out.println("Exclua primeiro todos os episodios dessa serie no menu EPISODIOS.");
                }

                if (atoresVinculados != null && atoresVinculados.length > 0) {
                    System.out.println("Nao e possivel excluir a serie pois existem atores ligados a ela.");
                    System.out.println("Exclua primeiro todos os atores dessa serie no menu ATORES.");
                }

                return;
            }

            System.out.println("Serie encontrada:");
            mostraSerie(serie);

            System.out.print("\nConfirma a exclusao da serie? (S/N) ");

            char resp = sc.nextLine().charAt(0);

            if (resp == 'S' || resp == 's') {
                boolean excluido = seriesArq.delete(serie.getID());
                if (excluido) {
                    System.out.println("Serie excluida com sucesso.");
                } else {
                    System.out.println("Erro ao excluir a serie.");
                }
            } else {
                System.out.println("Exclusao cancelada.");
            }

        } catch (Exception e) {
            System.out.println("Erro do sistema. Nao foi possivel excluir o serie!");
            e.printStackTrace();
        }
    }

    public void mostraSerie(Serie serie) {
        if (serie != null) {
            System.out.println("\nDetalhes da Serie:");
            System.out.println("----------------------");
            System.out.printf("Nome.........: %s\n", serie.getNome());
            System.out.printf("ID...........: %d\n", serie.getID());
            System.out.printf("Streaming....: %s\n", serie.getStreaming());
            System.out.printf("Sinopse......: %s\n", serie.getSinopse());
            System.out.printf("Lancamento: %s\n",
                    serie.getLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("----------------------");
        }
    }

    public void listarTodasSeries() {
        System.out.println("\nListagem de todas as series:");

        try {

            List<Serie> series = seriesArq.readAll();

            if (series.isEmpty()) {
                System.out.println("Nenhuma serie encontrada.");
                return;
            }

            for (Serie serie : series) {
                mostraSerie(serie);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar todas as series!");
            e.printStackTrace();
        }
    }
}
