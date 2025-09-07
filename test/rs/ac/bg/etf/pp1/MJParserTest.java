package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
//import rs.etf.pp1.symboltable.Tab;

public class MJParserTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		Logger log = Logger.getLogger(MJParserTest.class);
		if (args.length < 2) {
			log.error("Not enough arguments supplied! Usage: MJParser <source-file> <obj-file> ");
			return;
		}
		
		File sourceCode = new File("test/program.mj");
		if (!sourceCode.exists()) {
			log.error("Source file [" + sourceCode.getAbsolutePath() + "] not found!");
			return;
		}
			
		log.info("Compiling source file: " + sourceCode.getAbsolutePath());
		
		try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {
			Yylex lexer = new Yylex(br);
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  // start of parsing
	        Program prog = (Program)(s.value); // s.value contains address of root node of ast tree
	        
	        log.info(prog.toString("")); // Printing the tree
	        log.info("=======================================================");
	        
	        Tab.init(); // Initializing the universe scope
	        
	        // Inserting Bool type into symbol table (not sure why it doesn't already exist there)
	        Struct bool_type = new Struct(Struct.Bool);
	        Obj bool_obj = Tab.insert(Obj.Type, "bool", bool_type);
	        bool_obj.setAdr(-1);
	        bool_obj.setLevel(-1);
	        
	        // Inserting Set type into symbol table
	        Struct set_type = new Struct(Struct.Array, Tab.intType);
	        Obj set_obj = Tab.insert(Obj.Type, "set", set_type);
	        set_obj.setAdr(-1);
	        set_obj.setLevel(-1);
	        
	        // Semantic Analyzer
	        SemanticAnalyzer sem_analyzer = new SemanticAnalyzer();
	        prog.traverseBottomUp(sem_analyzer);
	        
	        log.info("=======================================================");
	        Tab.dump(); // Printing the symbol table
	        
	        if (!p.errorDetected && sem_analyzer.passed()) {
	        	// Code generation
	        	File obj_file = new File("test/program.obj"); // Create Obj file that will contain bytecode
	        	if (obj_file.exists()) { // delete that file if it already exists
	        		obj_file.delete();
	        	}
	        	
	        	CodeGenerator code_generator = new CodeGenerator();
	        	prog.traverseBottomUp(code_generator);
	        	Code.dataSize = sem_analyzer.n_vars;
	        	Code.mainPc = code_generator.getMainPc();
	        	Code.write(new FileOutputStream(obj_file));
	        	
	        	log.info("Parsiranje uspesno zavrseno!");
	        }
	        else {
	        	log.error("Parsiranje NIJE uspesno zavrseno!");
	        }
		}
	}
}
