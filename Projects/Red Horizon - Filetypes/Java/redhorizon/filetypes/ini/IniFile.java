
package redhorizon.filetypes.ini;

import redhorizon.filetypes.AbstractFile;
import redhorizon.filetypes.FileExtensions;
import redhorizon.utilities.BufferUtility;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Representation of an <tt>INI</tt> file.  These are the system configuration
 * files you'd usually find in a Windows environment which are simply lists of
 * key/value pairs grouped by sections.
 * 
 * @author Emanuel Rabina
 */
@FileExtensions("ini")
public class IniFile extends AbstractFile {

	private final HashMap<String,Map<String,String>> inidata = new HashMap<>();

	/**
	 * Constructor, read an existing INI file.
	 * 
	 * @param name		  The name of this file.
	 * @param bytechannel Channel to the INI file to read.
	 */
	public IniFile(String name, ReadableByteChannel bytechannel) {

		super(name);

		// Read the entire channel, parse channel contents
		ByteBuffer contents = BufferUtility.readRemaining(bytechannel);
		parseContents(new String(contents.array()));
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void close() {
	}

	/**
	 * Returns the collection of name/value mappings that exist under the given
	 * section name.
	 * 
	 * @param sectionname Name of the section.
	 * @return Collection of name/value mappings for the section, or
	 * 		   <tt>null</tt> if a section with that name doesn't exist.
	 */
	public Map<String,String> getSection(String sectionname) {

		return inidata.get(sectionname);
	}

	/**
	 * Returns the value of the given parameter in the given section.
	 * 
	 * @param sectionname The name of the section the parameter resides.
	 * @param paramname	  The name of the parameter.
	 * @return The value of the setting, or <tt>null</tt> if that parameter
	 * 		   doesn't exist.
	 */
	public String getValue(String sectionname, String paramname) {

		return getSection(sectionname).get(paramname);
	}

	/**
	 * Parse the INI file contents, building the sections and key/value pairs
	 * from it.
	 * 
	 * @param contents
	 */
	private void parseContents(String contents) {

		IniFileReader reader = new IniFileReader();
		inidata.putAll(reader.read(contents));
	}
}
