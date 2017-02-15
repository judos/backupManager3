package ch.judos.backupManager.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.csv.CSVFileReader;
import ch.judos.generic.data.csv.CSVFileWriter;

public class PathStorage implements Iterable<PathEntry> {

	private DynamicList<PathEntry> list;

	public PathStorage() {
		this.list = new DynamicList<PathEntry>();
	}

	public int getSize() {
		return this.list.size();
	}

	public PathEntry getPathEntry(int index) {
		return this.list.get(index);
	}

	public void loadFromFile(File file) {
		try {
			CSVFileReader reader = CSVFileReader.readFile(file);
			this.list = new DynamicList<PathEntry>();
			for (int i = 0; i < reader.countEntries(); i++) {
				HashMap<String, String> entry = reader.getEntry(i);
				this.list.add(new PathEntry(entry.get("changePath"), entry.get("backupPath"),
					entry.get("status"), Boolean.valueOf(entry.get("selected"))));
			}
		}
		catch (IOException e) {
		}
	}

	public void saveToFile(File backupPathFile) throws IOException {
		String[] attributes = {"changePath", "backupPath", "status", "selected"};
		CSVFileWriter writer = new CSVFileWriter(attributes);
		for (PathEntry entry : this.list) {
			writer.addEntry(new String[]{entry.getChangePath(), entry.getBackupPath(), entry
				.getLastBackup(), String.valueOf(entry.isSelected())});
		}
		writer.writeFile(backupPathFile);
	}

	public void removePathIndexes(int[] selected) {
		for (int i = selected.length - 1; i >= 0; i--) {
			this.list.remove(selected[i]);
		}
	}

	public void addPath(PathEntry entry) {
		this.list.add(entry);
	}

	@Override
	public Iterator<PathEntry> iterator() {
		return this.list.iterator();
	}

	public PathStorage cloneSelected() {
		PathStorage result = new PathStorage();
		for (PathEntry entry : this) {
			if (entry.isSelected())
				result.addPath(entry);
		}
		return result;
	}

}
