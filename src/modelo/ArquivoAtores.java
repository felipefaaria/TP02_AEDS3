package modelo;

import java.io.File;
import java.util.ArrayList;

import aeds3.*;
import classes.Ator;

public class ArquivoAtores extends Arquivo<Ator> {

  Arquivo<Ator> arqAtores;

  // índice do nome do ator
  ArvoreBMais<ParNomeId> indiceNome;

  // índice de relacionamento ator e série
  ArvoreBMais<ParID> indiceRelacaoSerieAtor;

  public ArquivoAtores() throws Exception {

    super("episodios", Ator.class.getConstructor());

    File directory = new File("./dados/ator");
    if (!directory.exists()) {
      directory.mkdirs();
    }

    indiceNome = new ArvoreBMais<>(
        ParNomeId.class.getConstructor(), 5, "./dados/ator" + "/indiceNome.db");

    indiceRelacaoSerieAtor = new ArvoreBMais<>(
        ParID.class.getConstructor(), 5, "./dados/ator" + "/indiceRelacaoSerieAtor.db");

  }

  @Override
  public int create(Ator at) throws Exception {

    int id = super.create(at);

    indiceNome.create(new ParNomeId(at.getNomeAtor(), id));

    indiceRelacaoSerieAtor.create(new ParID(at.getIDSerie(), id));

    return id;

  }

  public Ator[] readNome(String nome) throws Exception {

    if (nome.length() == 0)
      return null;

    ArrayList<ParNomeId> pares = indiceNome.read(new ParNomeId(nome, -1));

    if (!pares.isEmpty()) {

      Ator[] atores = new Ator[pares.size()];

      int i = 0;

      for (ParNomeId par : pares) {

        atores[i++] = read(par.getId());

      }

      return atores;

    } else {

      return null;
    }

  }

  @Override
  public boolean delete(int id) throws Exception {

    Ator at = read(id);

    if (at != null) {

      if (super.delete(id)) {

        return indiceNome.delete(new ParNomeId(at.getNomeAtor(), id))
            && indiceRelacaoSerieAtor.delete(new ParID(at.getIDSerie(), id));

      }

    }

    return false;

  }

  @Override
  public boolean update(Ator novoAtor) throws Exception {

    Ator at = read(novoAtor.getID());

    if (at != null) {

      if (super.update(novoAtor)) {

        if (!at.getNomeAtor().equals(novoAtor.getNomeAtor())) {

          indiceNome.delete(new ParNomeId(at.getNomeAtor(), at.getID()));
          indiceNome.create(new ParNomeId(novoAtor.getNomeAtor(), novoAtor.getID()));

        }
        return true;
      }

    }
    return false;
  }

  public Ator[] readPorSerie(int idSerie) throws Exception {

    ArrayList<ParID> pares = indiceRelacaoSerieAtor.read(new ParID(idSerie, -1));

    if (pares.isEmpty()) {
      return null;
    }

    Ator[] atores = new Ator[pares.size()];
    int i = 0;

    for (ParID par : pares) {

      atores[i++] = read(par.getId2());

    }

    return atores;

  }

}