package org.freeplane.plugin.script;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

import org.junit.Test;

public class CompiledFilesSpec {

	@SuppressWarnings("serial")
	private File file(String name, final long lastModified) {
		return new File(name) {
			@Override
			public long lastModified() {
				return lastModified;
			}
			
		};
	}
	
	@Test
	public void filtersEmptySet() throws Exception {
		final PrecompiledClasses uut = new PrecompiledClasses(0, Collections.<String>emptySet());
		Collection<File> files = Collections.emptySet();
		assertThat(uut.filterNewAndNewer(files)).containsExactly();
	}
	
	
	@Test
	public void filteringRemovesOlderFile() throws Exception {
		final File file = file("file", 1);
		final PrecompiledClasses uut = new PrecompiledClasses(2, Collections.singleton(file.getAbsolutePath()));
		Collection<File> files = Collections.singleton(file);
		assertThat(uut.filterNewAndNewer(files)).containsExactly();
	}

	
	@Test
	public void filteringKeepsNewFile() throws Exception {
		final File file = file("file", 1);
		final PrecompiledClasses uut = new PrecompiledClasses(2, Collections.singleton(file.getAbsolutePath()));
		final File newFile = file("newFile", 1);
		Collection<File> files = Collections.singleton(newFile);
		assertThat(uut.filterNewAndNewer(files)).containsExactly(newFile);
	}
	
	@Test
	public void savesToXml() throws Exception {
		final PrecompiledClasses uut = new PrecompiledClasses(2, Collections.singleton("file"));
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        // create XMLEventWriter
        final StringWriter stream = new StringWriter();
		XMLEventWriter eventWriter = outputFactory
                .createXMLEventWriter(stream);
		uut.writeThrowExceptions(eventWriter);
		
		String expected = "<?xml version=\"1.0\" encoding=\"utf-8\"?><compiledFiles compilationTime=\"2\">\n" + 
				"	<file>file</file>\n" + 
				"</compiledFiles>\n";
		assertThat(stream.toString()).isEqualTo(expected);

	}
	
	@Test
	public void loadsFromXml() throws Exception {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><compiledFiles compilationTime=\"2\">\n" + 
				"	<file>file</file>\n" + 
				"</compiledFiles>\n";
        // Setup a new eventReader

        XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xml));
        
        PrecompiledClasses uut = PrecompiledClasses.readThrowExceptions(eventReader);
		
        final PrecompiledClasses expected = new PrecompiledClasses(2, Collections.singleton("file"));
		assertThat(uut).usingRecursiveComparison().isEqualTo(expected);

	}
	
	@Test
	public void createsFromCollection() throws Exception {
        final PrecompiledClasses compiledFiles = new PrecompiledClasses((long) 2);
		compiledFiles.addAll(Collections.singleton(new File("file")));
		PrecompiledClasses uut = compiledFiles;
		
        final PrecompiledClasses expected = new PrecompiledClasses(2, Collections.singleton(new File("file").getAbsolutePath()));
		assertThat(uut).usingRecursiveComparison().isEqualTo(expected);
	}
}
