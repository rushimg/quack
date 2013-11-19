package quack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.text.Document;



public class VariableParser {
	protected List<String> originalVars;
	protected List<String> soVars;
	protected List<String> soMethds;
	protected static List<String> tempVars = new Vector<String>();
	protected static List<String> tempVars2 = new Vector<String>();
	protected static List<String> tempMethds = new Vector<String>();
	
	public VariableParser() {
		this.originalVars = new Vector<String>();
		this.soVars = new Vector<String>();
		this.soMethds = new Vector<String>();
	}

	public void printList(List<String> vars) {
		for (String e : vars)
			System.out.println(e);
		System.out.println();
	}

	public void printListOFLists(List<List<String>> vars) {
		for (List<String> e : vars)
			this.printList(e);
	}
	/*
	public void parseMethods(ICompilationUnit unit) {
		try {
			IType type = unit.findPrimaryType();
			IMethod[] methods = type.getMethods();
			for (IMethod method : methods) {
				String temp = "Method: " + method.toString();
				this.originalVars.add(temp);
				System.out.println("Method: " + temp);
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}
	*/
	public void parseCU(CompilationUnit unit) {
		final CompilationUnit cu = unit;
		cu.accept(new ASTVisitor() {
			Set<String> names = new HashSet<String>();

			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				/*VariableParser.tempVars.add("Declaration of '" + name
						+ "' at line "
						+ cu.getLineNumber(name.getStartPosition()));*/
				
				if (node.resolveBinding() != null) {
					
					VariableParser.tempVars.add(node.resolveBinding()
							.toString());
				}
				return false; // do not continue to avoid usage info
			}

			/*public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
					VariableParser.tempVars.add("Usage of '" + node
							+ "' at line "
							+ cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}*/

		});

		for (String e : VariableParser.tempVars)
			this.originalVars.add(e);
	}

	/*public void test(CompilationUnit unit) {
		// unit.compile
		ICompilationUnit compilationUnit = (ICompilationUnit) unit;
		try {

			IType type = compilationUnit.findPrimaryType();
			IMethod[] methods = type.getMethods();
			for (IMethod method : methods) {
				System.out.print("method: " + method.toString());
				
				 * ASTParser parser = ASTParser.newParser(AST.JLS3);
				 * parser.setSource(compilationUnit);
				 * parser.setSourceRange(method.getSourceRange().getOffset(),
				 * method.getSourceRange().getLength());
				 * //parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);
				 * //parser.setSource(method.getSource().toCharArray());
				 * //parser.setProject(method.getJavaProject());
				 * parser.setResolveBindings(true); CompilationUnit cu =
				 * (CompilationUnit)parser.createAST(null); cu.accept(new
				 * ASTMethodVisitor());
				 

				// If the visitor visit the right VariableDeclarationFragment,
				// then the right IMethod is the current 'method' variable

			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}*/

	public List<String> getMethods(CompilationUnit unit){
		final CompilationUnit cu = unit;
		cu.accept(new ASTVisitor() {
            public boolean visit(final MethodDeclaration node) {
            	//System.out.println("declaring method: " + node.getName());
            	VariableParser.tempMethds.add(node.getName().toString());
            	//System.out.println("returns type: " + node.getReturnType2());
            	VariableParser.tempMethds.add(node.getReturnType2().toString());
                List<String> parameters = new ArrayList<String>();
                for (Object parameter : node.parameters()) {
                    VariableDeclaration variableDeclaration = (VariableDeclaration) parameter;
                    String type = variableDeclaration.getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY).
                            toString();
                    for (int i = 0; i < variableDeclaration.getExtraDimensions(); i++) {
                        type += "[]";
                    }
                    VariableParser.tempMethds.add(type);
                    //parameters.add(type);
                }
                //System.out.println("input params: " + parameters.toString());
            
                return true;
            }});
		List<String> retList = new Vector<String>();
		for (String e : VariableParser.tempMethds) {
			retList.add(e);
		}

		for (int i = 0; i < VariableParser.tempMethds.size(); i++) {
			VariableParser.tempMethds.remove(i--);
		}

		return retList;
	}
            
	public List<String> parseCU_SO(CompilationUnit unit) {
		final CompilationUnit cu = unit;
		cu.accept(new ASTVisitor() {
			Set<String> names = new HashSet<String>();
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				VariableParser.tempVars2.add("Declaration of '" + name
						+ "' at line "
						+ cu.getLineNumber(name.getStartPosition()));
				//System.out.println(node.toString());
				/*if (node.resolveBinding() != null) {
					
					System.out.println(node.resolveBinding().get);
				}*/
				if (name.resolveBinding() != null) {
					VariableParser.tempVars2.add(name.resolveBinding()
							.toString());
				}

				return false; // do not continue to avoid usage info
			}

			public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
					VariableParser.tempVars2.add("Usage of '" + node
							+ "' at line "
							+ cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}

		});
		List<String> retList = new Vector<String>();
		for (String e : VariableParser.tempVars2) {
			retList.add(e);
		}

		for (int i = 0; i < VariableParser.tempVars2.size(); i++) {
			VariableParser.tempVars2.remove(i--);
		}

		return retList;
	}

	public List<String> runParser(ICompilationUnit unit, CompilationUnit ast) {
		this.parseCU(ast);
		return this.originalVars;
	}

	public List<String> parseSO(ICompilationUnit unit, String repString) {
		// this.parseMethods(repString);
		List<String> retString = new Vector<String>();
		retString = this.addClass(unit, repString);
		//retString.addAll(this.addMethod(unit, repString)); // only accepting full methods for now
		return retString;
	}

	private List<String> addMethod(ICompilationUnit unit, String repString) {
		String methString = "public class SO { \n public void run(){ \n "
				+ repString + " \n } \n }";
		return this.printParsed(unit, methString);
	}

	private List<String> addClass(ICompilationUnit unit, String repString) {
		String classString = "public class SO { \n " + repString + " \n }";
		return this.printParsed(unit, classString);
	}

	private List<String> printParsed(ICompilationUnit unit, String addString) {
		addString = this.getImports(unit) + '\n' + addString;
		StringBuffer buf = new StringBuffer(addString);
		CompilationUnit ast = EclipseUtil.compile(unit, unit.getJavaProject(),
				buf.toString().toCharArray(), 0);
		//System.out.print(ast.toString());

		return this.getMethods(ast);
		//return this.parseCU_SO(ast);
	}

	private String getImports(ICompilationUnit unit) {
		String retString = null;
		try {
			for (IImportDeclaration I : unit.getImports()) {
				retString = (I.toString());
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return retString;
	}

	/*
	 * private CompilationUnit getCU(ICompilationUnit unit, int quackOffset, int
	 * cursorOffset) { ASTParser parser = ASTParser.newParser(AST.JLS3);
	 * parser.setKind(ASTParser.K_COMPILATION_UNIT); parser.setSource(unit);
	 * parser.setResolveBindings(true); parser.setBindingsRecovery(true);
	 * CompilationUnit compiUnit = (CompilationUnit) parser.createAST(null);
	 * System.out.print(compiUnit.toString()); String standinExpression = "";
	 * StringBuffer buf = new StringBuffer(compiUnit.toString());
	 * buf.replace(quackOffset, cursorOffset, standinExpression);
	 * CompilationUnit ast = EclipseUtil.compile(unit, unit .getJavaProject(),
	 * buf.toString().toCharArray(), 0); // System.out.print(ast.toString());
	 * return compiUnit; }
	 */
	/*
	 * private ICompilationUnit createCU(String rawCode){ Document doc = new
	 * Document(rawCode); ASTParser parser = ASTParser.newParser(AST.JLS3);
	 * parser.setKind(ASTParser.K_COMPILATION_UNIT);
	 * parser.setSource(doc.get().toCharArray());
	 * 
	 * parser.setResolveBindings(true); parser.setBindingsRecovery(true);
	 * //parser.setBindingsRecovery(true); ICompilationUnit cu =
	 * (ICompilationUnit) parser.createAST(null);
	 * System.out.print(cu.toString()); return cu; }
	 */

	/*
	 * public void getGlobals(ICompilationUnit unit) { try { for (IType type :
	 * unit.getAllTypes()) { for (IField ifield : type.getFields()) {
	 * System.out.println("iField " + ifield); } } } catch (JavaModelException
	 * e) { e.printStackTrace(); } }
	 */
}
