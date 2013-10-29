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
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jface.text.Document;
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

	public void getGlobals(ICompilationUnit unit) {
		try {
			for (IType type : unit.getAllTypes()) {
				for (IField ifield : type.getFields()) {
					System.out.println("iField " + ifield);
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

	public void parse(ICompilationUnit unit) {
		try {
			IType type = unit.findPrimaryType();
			IMethod[] methods = type.getMethods();
			for (IMethod method : methods)
				System.out.println("Method: " + method);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		;
	}

	public void parseCU(CompilationUnit unit) {
		final CompilationUnit cu = unit;
		cu.accept(new ASTVisitor() {
			Set names = new HashSet();

			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '" + name + "' at line "
						+ cu.getLineNumber(name.getStartPosition()));
				System.out.println(name.resolveBinding());
				return false; // do not continue to avoid usage info
			}

			public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
					System.out.println("Usage of '" + node + "' at line "
							+ cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}
		});
	}
	
	/*private CompilationUnit getCU(ICompilationUnit unit, int quackOffset, int cursorOffset) {
		 ASTParser parser = ASTParser.newParser(AST.JLS3);
		 parser.setKind(ASTParser.K_COMPILATION_UNIT);
		 parser.setSource(unit);
		 parser.setResolveBindings(true);
		 parser.setBindingsRecovery(true);
		 CompilationUnit compiUnit = (CompilationUnit) parser.createAST(null);
		 System.out.print(compiUnit.toString());
		 String standinExpression = "";
         StringBuffer buf = new StringBuffer(compiUnit.toString());
         buf.replace(quackOffset, cursorOffset, standinExpression);
         CompilationUnit ast = EclipseUtil.compile(unit, unit
                 .getJavaProject(), buf.toString().toCharArray(), 0);
        // System.out.print(ast.toString());
		 return compiUnit;
	}*/

	public void runParser(ICompilationUnit unit,CompilationUnit ast) {
		this.parse(unit);
		this.parseCU(ast);
	}

	
	public void parseSO(ICompilationUnit unit , String repString) {
		this.addClass(unit, repString);
		this.addMethod(unit, repString);
	}
	
	private void addMethod(ICompilationUnit unit ,String repString){
		String methString = "public class SO { public void run(){ " + repString + " } }";
		this.printParsed(unit, methString);
	}
	
	private CompilationUnit createCU(String rawCode){
		 Document doc = new Document(rawCode);
		 ASTParser parser = ASTParser.newParser(AST.JLS3);
		 parser.setKind(ASTParser.K_COMPILATION_UNIT);
		 parser.setSource(doc.get().toCharArray());
		
		 parser.setResolveBindings(true);
		 parser.setBindingsRecovery(true);
		 //parser.setBindingsRecovery(true);
		 CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		 return cu;
		 /*cu.recordModifications();
		 AST ast = cu.getAST();
		 ImportDeclaration id = ast.newImportDeclaration();
		 id.setName(ast.newName(new String[] {"java", "util", "Set"});
		 cu.imports().add(id); // add import declaration at end
		 TextEdit edits = cu.rewrite(document, null);
		 UndoEdit undo = edits.apply(document);*/
	}
	private void addClass(ICompilationUnit unit , String repString){
		String classString = "public class SO { " + repString + "}";
		this.printParsed(unit, classString);
	}
	
	private void printParsed(ICompilationUnit unit , String addString){
		//CompilationUnit ast = this.createCU(addString);
		StringBuffer buf = new StringBuffer(addString);
		CompilationUnit ast = EclipseUtil.compile(unit, unit.getJavaProject(),
		buf.toString().toCharArray(), 0);
		System.out.print(ast.toString());
		this.parseCU(ast);
	}
}
