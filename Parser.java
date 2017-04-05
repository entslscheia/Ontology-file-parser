import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	public static File file = new File("dbpedia_2015-10.owl");
	public static BufferedReader reader = null;
	
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		Set<Integer> set = new HashSet<Integer>();
		File nfile = new File("data.txt");
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(nfile);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		writer = new BufferedWriter(fw);
		try {
	        System.out.println("以行为单位读取文件内容，一次读一整行：");
	        reader = new BufferedReader(new FileReader(file));
	        String tempString = null;
	        // 一次读入一行，直到读入null为文件结束
	        boolean flag = true;
	        String child = null, parent = null;
	        while ((tempString = reader.readLine()) != null) {
	        	
	        	if(flag){
	        		if(!tempString.contains("="))
	        			break;
	        		child = tempString.split("=")[1];
	        		child = child.substring(0,child.length() - 2);
	        		child = child.replace("\"", "<");
	        		child = child.concat(">");
	        		//System.out.println(child);
	        		flag = !flag;
	        		continue;
	        	}
	        	if(!flag){
	        		String pattern = "<rdfs:subClassOf rdf:resource=\"http://dbpedia.org/ontology/[a-z|A-Z]*\"/>|<rdfs:subClassOf rdf:resource=\"http://www.w3.org/2002/07/owl#Thing\"/>";
	        	    Pattern r = Pattern.compile(pattern);
	        	    Matcher m = r.matcher(tempString);
	        	    if(m.find()){
	        	    	parent = m.group(0).split("=")[1];
	        	        parent = parent.substring(0, parent.length()-3);
	        	        parent = parent.replace("\"", "<");
	        	        parent = parent.concat(">");
	        	        //System.out.println(parent);
	        	    }
	        	    flag = !flag;
	        	}
	        	if(parent.contains("dbpedia.org/ontology") || parent.contains("owl#Thing")){
	    			writer.write(child);
	    			writer.write("____");
	    			writer.write("<http://www.w3.org/2000/01/rdf-schema#subClassOf>");
	    			writer.write("____");
	    			writer.write(parent);
	    			writer.newLine();
	    			System.out.println(child + " " + parent);
	        	}
	        }
	        writer.close();
			fw.close();
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	}
}
