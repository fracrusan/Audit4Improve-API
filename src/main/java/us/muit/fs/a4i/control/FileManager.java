/**
 * 
 */
package us.muit.fs.a4i.control;

/**
 * <p>Interfaz con los aspectos comunes al manejo de ficheros</p>
 * <p>Se separa de ReportManager porque a veces los informes se pueden persistir en bases de datos, u otros sistemas de persistencia</p>
 * @author Isabel Román
 *
 */
public interface FileManager {

	/**
	 *  <p>Establece la localización del fichero</p>
	 * @param path localización del fichero de informe
	 */
	public void setPath(String path);
	/**
	 * <p>Establece la localización del fichero</p>
	 * @param path localización del fichero de informe
	 */
	public void setName(String name);
}
