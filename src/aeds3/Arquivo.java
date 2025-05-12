package aeds3;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class Arquivo<T extends EntidadeArquivo> {

    public String nomeClasse;
    RandomAccessFile arquivo;
    HashExtensivel<ParIDEndereco> indiceDireto;
    Constructor<T> builder;
    final int TAMANHO_CABECALHO = 13;

    public Arquivo(String nc, Constructor<T> builder) throws Exception {
        this.nomeClasse = nc;
        this.builder = builder;
        File arq = new File("./dados");
        if (!arq.exists())
            arq.mkdir();
        arq = new File("./dados/" + nomeClasse);
        if (!arq.exists())
            arq.mkdir();
        arquivo = new RandomAccessFile("./dados/" + nomeClasse + "/" + nomeClasse + ".db", "rw");
        if (arquivo.length() < TAMANHO_CABECALHO) {
            arquivo.writeByte(2);
            arquivo.writeInt(0);
            arquivo.writeLong(-1);
            (new File("./dados/" + nomeClasse + "/indiceDireito.d.db")).delete();
            (new File("./dados/" + nomeClasse + "/indiceDireito.c.db")).delete();
        }
        indiceDireto = new HashExtensivel<>(
                ParIDEndereco.class.getConstructor(),
                4,
                "./dados/" + nomeClasse + "/indiceDireito.d.db",
                "./dados/" + nomeClasse + "/indiceDireito.c.db");
    }

    public int create(T classe) throws Exception {

        arquivo.seek(1);
        int novoID = arquivo.readInt() + 1;
        classe.setID(novoID);
        arquivo.seek(1);
        arquivo.writeInt(novoID);

        byte[] vb = classe.toByteArray();
        long endereco = getDeleted(vb.length);
        if (endereco == -1) {
            endereco = arquivo.length();
            arquivo.seek(endereco);
            arquivo.writeByte(' ');
            arquivo.writeShort(vb.length);
            arquivo.write(vb);
        } else {
            arquivo.seek(endereco);
            arquivo.writeByte(' ');
            arquivo.skipBytes(2);
            arquivo.write(vb);
        }
        indiceDireto.create(new ParIDEndereco(novoID, endereco));
        return novoID;
    }

    public T read(int id) throws Exception {
        ParIDEndereco pId = indiceDireto.read(id);
        if (pId == null)
            return null;
        long endereco = pId.getEndereco();
        if (endereco == -1)
            return null;

        arquivo.seek(endereco);
        byte lapide = arquivo.readByte();
        short tamanho = arquivo.readShort();
        if (lapide == ' ') {
            byte[] dados = new byte[tamanho];
            arquivo.read(dados);
            T entidade = builder.newInstance();
            entidade.fromByteArray(dados);
            if (entidade.getID() == id)
                return entidade;
        }
        return null;
    }

    public boolean delete(int id) throws Exception {
        ParIDEndereco pId = indiceDireto.read(id);
        if (pId == null)
            return false;
        long endereco = pId.getEndereco();
        if (endereco == -1)
            return false;

        arquivo.seek(endereco);
        byte lapide = arquivo.readByte();
        short tamanho = arquivo.readShort();
        if (lapide == ' ') {
            byte[] dados = new byte[tamanho];
            arquivo.read(dados);
            T entidade = builder.newInstance();
            entidade.fromByteArray(dados);
            if (entidade.getID() == id) {
                arquivo.seek(endereco);
                arquivo.writeByte('*');
                addDeleted(tamanho, endereco);
                indiceDireto.delete(id);
                return true;
            }
        }
        return false;
    }

    public boolean update(T novaEntidade) throws Exception {
        ParIDEndereco pId = indiceDireto.read(novaEntidade.getID());
        if (pId == null)
            return false;
        long endereco = pId.getEndereco();
        if (endereco == -1)
            return false;

        arquivo.seek(endereco);
        byte lapide = arquivo.readByte();
        short tamanho = arquivo.readShort();
        if (lapide == ' ') {
            byte[] dados = new byte[tamanho];
            arquivo.read(dados);
            T classe = builder.newInstance();
            classe.fromByteArray(dados);
            if (classe.getID() == novaEntidade.getID()) {
                byte[] dados2 = novaEntidade.toByteArray();
                short tamanho2 = (short) dados2.length;
                if (tamanho2 <= tamanho) {
                    arquivo.seek(endereco + 3);
                    arquivo.write(dados2);
                } else {
                    arquivo.seek(endereco);
                    arquivo.writeByte('*');
                    addDeleted(tamanho, endereco);

                    endereco = getDeleted(tamanho2);
                    if (endereco == -1) {
                        endereco = arquivo.length();
                        arquivo.seek(endereco);
                        arquivo.writeByte(' ');
                        arquivo.writeShort(tamanho2);
                        arquivo.write(dados2);
                    } else {
                        arquivo.seek(endereco);
                        arquivo.writeByte(' ');
                        arquivo.skipBytes(2);
                        arquivo.write(dados2);
                    }
                    indiceDireto.update(new ParIDEndereco(novaEntidade.getID(), endereco));
                }
                return true;
            }
        } else {
            arquivo.skipBytes(tamanho);
        }
        return false;
    }

    public void addDeleted(int tamanhoEspaco, long enderecoEspaco) throws Exception {
        long anterior = 5;
        long proximo = -1;
        byte lapide;
        short tamanho;
        arquivo.seek(anterior);
        long endereco = arquivo.readLong();
        if (endereco == -1) {
            arquivo.seek(5);
            arquivo.writeLong(enderecoEspaco);
            arquivo.seek(enderecoEspaco + 3);
            arquivo.writeLong(proximo); // -1
        } else {
            do {
                arquivo.seek(endereco);
                lapide = arquivo.readByte();
                tamanho = arquivo.readShort();
                proximo = arquivo.readLong();
                if (lapide == '*' && tamanhoEspaco < tamanho) {
                    if (anterior == 5) {
                        arquivo.seek(anterior);
                    } else {
                        arquivo.seek(anterior + 3);
                    }
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco + 3);
                    arquivo.writeLong(endereco);
                    break;
                }
                if (proximo == -1) {
                    arquivo.seek(endereco + 3);
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco + 3);
                    arquivo.writeLong(-1);
                    break;
                }
                anterior = endereco;
                endereco = proximo;
            } while (endereco != -1);
        }
    }

    public long getDeleted(int tamanhoNecessario) throws Exception {
        long anterior = 5;
        arquivo.seek(anterior);
        long endereco = arquivo.readLong();
        byte lapide;
        int tamanho;
        long proximo;
        while (endereco != -1) {
            arquivo.seek(endereco);
            lapide = arquivo.readByte();
            tamanho = arquivo.readShort();
            proximo = arquivo.readLong();
            if (lapide == '*' && tamanho >= tamanhoNecessario) {
                if (anterior == 5)
                    arquivo.seek(anterior);
                else
                    arquivo.seek(anterior + 3);
                arquivo.writeLong(proximo);
                break;
            }
            anterior = endereco;
            endereco = proximo;
        }
        return endereco;
    }

    public List<T> readAll() throws Exception {
        List<T> allClasses = new ArrayList<>();
        arquivo.seek(TAMANHO_CABECALHO);
        while (arquivo.getFilePointer() < arquivo.length()) {
            byte lapide = arquivo.readByte();
            short tamanho = arquivo.readShort();
            if (lapide == ' ') {
                byte[] dados = new byte[tamanho];
                arquivo.read(dados);
                T entidade = builder.newInstance();
                entidade.fromByteArray(dados);
                allClasses.add(entidade);
            } else {
                arquivo.skipBytes(tamanho);
            }
        }
        return allClasses;
    }
}
