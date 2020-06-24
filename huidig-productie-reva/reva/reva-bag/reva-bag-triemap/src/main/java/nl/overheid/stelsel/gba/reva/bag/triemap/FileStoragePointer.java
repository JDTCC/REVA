package nl.overheid.stelsel.gba.reva.bag.triemap;

import java.io.Serializable;

/**
 * Een <tt>FileStoragePointer</tt> houdt de verwijzing naar een object in een
 * bestand bij. Aan de hand van deze verwijzing kan de
 * <tt>FileStorageController</tt> het object weer teruglezen uit het bestand.
 * 
 */
public class FileStoragePointer implements Serializable {

  private static final long serialVersionUID = 1L;
  private long pointer;
  private int size;

  public FileStoragePointer( long pointer, int size ) {
    this.pointer = pointer;
    this.size = size;
  }

  public long getPointer() {
    return pointer;
  }

  public int getSize() {
    return size;
  }
}
