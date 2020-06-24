package nl.overheid.stelsel.gba.reva.bag.triemap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * De <tt>FileStorageController</tt> slaat objecten op in een enkel bestand en
 * geeft daarbij een verwijzing terug naar de locatie van het object in het
 * bestand. Aan de hand van deze verwijzing kan het object weer uit het bestand
 * worden teruggelezen.
 * 
 * @param <T>
 *          Het type object dat in het bestand wordt opgeslagen.
 * 
 */
public class FileStorageController<T extends Serializable> {

  private static final Logger LOGGER = LoggerFactory.getLogger( FileStorageController.class );

  private Path file;

  public FileStorageController( Path file ) {
    this.file = file;
  }

  public FileStoragePointer add( T object ) throws IOException {
    FileStoragePointer fp = null;

    try( FileChannel channel = FileChannel.open( file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
            StandardOpenOption.SYNC, StandardOpenOption.APPEND );
            FileLock lock = channel.tryLock();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream( baos ); ) {
      stream.writeObject( object );
      long pointer = channel.position();
      int size = channel.write( ByteBuffer.wrap( baos.toByteArray() ) );

      fp = new FileStoragePointer( pointer, size );
    }

    return fp;
  }

  @SuppressWarnings( "unchecked" )
  public T get( FileStoragePointer pointer ) throws IOException {
    T object = null;

    byte[] bytes = new byte[pointer.getSize()];
    try( FileChannel channel = FileChannel.open( file, StandardOpenOption.READ ) ) {
      channel.read( ByteBuffer.wrap( bytes ), pointer.getPointer() );
    }

    try( ObjectInputStream stream = new ObjectInputStream( new ByteArrayInputStream( bytes ) ) ) {
      object = (T) stream.readObject();
    } catch( ClassNotFoundException cnfe ) {
      LOGGER.error( "Object class niet gevonden.", cnfe );
    }

    return object;
  }
}
