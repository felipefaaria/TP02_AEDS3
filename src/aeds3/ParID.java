package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParID implements aeds3.RegistroArvoreBMais<ParID> {

  private int id1;
  private int id2;
  private short TAMANHO = 8;

  public ParID() {
    this(-1, -1);
  }

  public ParID(int n1) {
    this(n1, -1);
  }

  public ParID(int n1, int n2) {
    try {
      this.id1 = n1;
      this.id2 = n2;
    } catch (Exception ec) {
      ec.printStackTrace();
    }
  }

  @Override
  public ParID clone() {
    return new ParID(this.id1, this.id2);
  }

  public short size() {
    return this.TAMANHO;
  }

  public int compareTo(ParID a) {
    if (this.id1 != a.id1)
      return this.id1 - a.id1;
    else

      return this.id2 == -1 ? 0 : this.id2 - a.id2;
  }

  public String toString() {
    return String.format("%3d", this.id1) + ";" + String.format("%-3d", this.id2);
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.id1);
    dos.writeInt(this.id2);
    return baos.toByteArray();
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.id1 = dis.readInt();
    this.id2 = dis.readInt();
  }

  public int getId2() {
    return id2;
  }

}