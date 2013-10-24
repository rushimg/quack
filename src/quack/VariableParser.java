package quack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import javax.accessibility.AccessibleTextSequence;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.text.edits.TextEdit;

import sun.font.CreatedFontTracker;

import com.sun.org.apache.xpath.internal.operations.And;
import com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor;
import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;

public class VariableParser {
	
	public Map<String, String> varMap;
	
	public VariableParser() {
		// TODO Auto-generated constructor stub
	}

	public void parse(String inputCode){
		BufferedReader bufReader = new BufferedReader(new StringReader(inputCode));
		String line=null;
		try {
			while( (line=bufReader.readLine()) != null )
			{
				line = this.cleanString(line);
				//Simple Ones (primitives)
				/*if (line.indexOf("int") != -1)
					System.out.println(line);
				else if (line.indexOf("char") != -1)
					System.out.println(line);
				else*/
				this.getInstances(line);
				//More complex ones (objects)
			}
		} catch (IOException e) { e.printStackTrace();}
		
		//System.out.println(inputCode);
	}
	
	private String cleanString(String unCleanString){
		//repalce with space
		String cleanerUnCleanString = "";
		//cleanerUnCleanString = unCleanString.replace(")", " ");
		cleanerUnCleanString = unCleanString.replace(")", " ");
		cleanerUnCleanString = cleanerUnCleanString.replace("(", " ");
		cleanerUnCleanString = cleanerUnCleanString.replace("]"," ");
		cleanerUnCleanString = cleanerUnCleanString.replace("["," ");
		cleanerUnCleanString = cleanerUnCleanString.replace(","," ");
		//replace with nothing
		cleanerUnCleanString = cleanerUnCleanString.replace("\"","");
		cleanerUnCleanString = cleanerUnCleanString.replace("\'","");
		return cleanerUnCleanString;
	}
	
	private String getInstances(String line){
		// idea here is that java programs follow the typical format ClassName instanceName
		String[] spaces = line.split(" ");
		for(int i=0; i< spaces.length-1 ; i++){
			if ((spaces[i].length() > 0) && (spaces[i+1].length() > 0)){
				//TODO: add in all primitive types
				if (Character.isUpperCase(spaces[i].charAt(0)) || "int".equals(spaces[i])){
					if (Character.isLowerCase(spaces[i+1].charAt(0)))
						System.out.println(spaces[i] + " " + spaces[i+1]);
				}
			}
		}
		return "";
	}
	
	public void getGlobals(ICompilationUnit unit){
		try {
			for (IType type : unit.getAllTypes()) { 
			 for (IField ifield : type.getFields()) { 
				 System.out.println("iField "+ifield);
			 }
			}
		} catch (JavaModelException e) {e.printStackTrace();}
	}
	
	public void parse(ICompilationUnit unit){
		try{
		IType type = unit.findPrimaryType();
		IMethod[] methods = type.getMethods();
		for(IMethod method : methods)
		{ 
		System.out.println("Method: " + method);
		ASTParser parser = ASTParser.newParser(AST.JLS3); 
		parser.setSource(unit);
		parser.setSourceRange(method.getSourceRange().getOffset(), method.getSourceRange().getLength());
		//parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
		//parser.setSource(method.getSource().toCharArray());
		//parser.setProject(method.getJavaProject()); 
		parser.setResolveBindings(true);
		final CompilationUnit cu = (CompilationUnit)parser.createAST(null);
		cu.accept(new ASTVisitor() {
			Set names = new HashSet();

			public boolean visit(VariableDeclarationFragment node) {
			SimpleName name = node.getName();
			this.names.add(name.getIdentifier());
			System.out.println("Declaration of '" + name + "' at line " +
					cu.getLineNumber(name.getStartPosition()));
			return false; // do not continue to avoid usage info
			}

			public boolean visit(SimpleName node) {
			if (this.names.contains(node.getIdentifier())) {
			System.out.println("Usage of '" + node + "' at line " +
			cu.getLineNumber(node.getStartPosition()));
			}
			return true;
			}
		}); 
		}
		}
		catch(JavaModelException e){ e.printStackTrace();} 
		//return cu;
	} 
		
		//System.out.print(cUnit.);
	
	public void top(ICompilationUnit unit){
		//CompilationUnit cUnit = 
		this.parse(unit);
		//ASTVisitor visitor = new ASTVisitor();
		//System.out.print(cUnit.getProblems());
		//cUnit.
		//System.out.print(cUnit);
	}
	/*StringBuffer buf = new StringBuffer(doc.get());
    CompilationUnit ast = EclipseUtil.compile(unit, unit
            .getJavaProject(), buf.toString().toCharArray(), 0);
    Model model = modelCache.getModel(unit, ast);
    model.createTypeList();
    System.out.print(model.genericToParameterized);
    System.out.print(model.processedTypes);*/
    // This gets all the global vars and methods
	
	
}
