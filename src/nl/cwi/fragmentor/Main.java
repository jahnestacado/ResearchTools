package nl.cwi.fragmentor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import nl.cwi.fragmentor.io.FragmentFilePath;
import nl.cwi.fragmentor.io.ReadFile;
import nl.cwi.fragmentor.io.WriteFile;

public class Main {
	private final static String MAIN_FOLDER = "/home/jahn/Desktop/thesis/";
	
	public static void main(String[] args) {
			FragmentFilePath paths = new FragmentFilePath(MAIN_FOLDER);
			for(String path:paths.getAllPaths()){
				fragmentation(path);
			}
	}
	
	private static void fragmentation(String path) {
		
		try {
			ReadFile reader = new ReadFile(path);
			FileInfo info = new FileInfo();
			info.setType(reader.getType());
			info.setName(reader.getName());
			FragmentFactory factory = new FragmentFactory(reader.fileToBytes());
			info.setSize(factory.getFragmentSize());
		
			List<Integer[]> fragments = factory.getFileFragments();
			List<Integer[]> filteredFragments = FragmentFiltering.getFilteredFragments(fragments);
			List<Float> ratios = FragmentFiltering.calculateRatio(filteredFragments);

		    ByteInstanceCounter counter = new ByteInstanceCounter(filteredFragments);
		    List<LinkedHashMap<Integer, Integer>> stats = counter.getStats();
		    
			WriteFile write = new WriteFile(filteredFragments,ratios, info,stats);
			write.produceOutput();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
	
	

	
	
}
