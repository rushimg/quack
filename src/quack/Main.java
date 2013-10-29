package quack;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.omg.CORBA.portable.InputStream;

import sun.misc.IOUtils;

import com.sun.tools.hat.internal.parser.Reader;
import com.sun.tools.javac.code.Attribute.Array;

import MyUtil.Bag;
import MyUtil.Pair;
import MyUtil.UU;

public class Main {

    public static Main main;

    public IProject lastProject;

    public ModelCache modelCache;

    public LinkedBlockingQueue<Pair<IProject, String>> logQueue;

    public Main() {
        main = this;
        modelCache = new ModelCache(1);
        logQueue = new LinkedBlockingQueue();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Pair<IProject, String> pair = logQueue.poll(60 * 1000,
                                TimeUnit.MILLISECONDS);
                        if (pair != null) {
                            IProject proj = pair.left;
                            String s = pair.right;
                            if (proj != null) {
                                IFile f = pair.left.getFile("quack.log");
                                if (!f.exists()) {
                                    f.create(new StringBufferInputStream(s),
                                            true, null);
                                } else {
                                    f.appendContents(
                                            new StringBufferInputStream(s),
                                            true, false, null);
                                }
                                UU.sleep(100);
                            } else {
                                System.out
                                        .println("-- last project is null, so print here --");
                                System.out.println(s);
                            }
                        }
                    } catch (Throwable t) {
                        System.out.println("-- error in log thread --");
                        t.printStackTrace();
                        UU.sleep(30 * 1000);
                    }
                }
            }
        }).start();
    }

    public static Main getMain() {
        if (main == null) {
            new Main();
        }
        return main;
    }

    public void log(String s) {
        s = System.currentTimeMillis() + ": " + s + "\n";
        logQueue.offer(new Pair<IProject, String>(lastProject, s));
    }

    public void log(String origin, Throwable t) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(out));
        log("ERROR IN " + origin + ":\n" + out.toString());

        t.printStackTrace();
    }

    public List<SOCompletionProposal> getProposals(ICompilationUnit unit,
            IDocument doc, int selectionOffset, int selectionLength) {
     	try {
             lastProject = unit.getJavaProject().getProject();
            
             // work here
             UU.profileClear();
             UU.profile("all");

             // work here
             UU.profile("part 1");

             Main main = Main.getMain();
            
             Vector<SOCompletionProposal> list = new Vector();
             int cursorOffset = selectionOffset + selectionLength;
             IJavaProject javaProject = unit.getJavaProject();
       
             // find the quack (keyword command)
             String quack;
             int quackOffset;
             if (selectionLength > 0) {
                 quack = doc.get(selectionOffset, selectionLength);
                 quackOffset = selectionOffset;
             } else {
                 IRegion lineRegion = doc
                         .getLineInformationOfOffset(cursorOffset);
                 int lineOffset = lineRegion.getOffset();
                 String line = doc.get(lineOffset, cursorOffset - lineOffset);
                 quackOffset = EclipseUtil.determineKeywordsStart(line);
                 if (quackOffset < 0)
                     return list;
                 quack = line.substring(quackOffset);
                 quackOffset += lineOffset;
                 
                 // make sure there is a space in it before we conclude it is a quack
                 //TODO: do we really need this check? 
                 boolean sureItsAQuack = UU.matches(" |^_$", quack);
                 if (!sureItsAQuack) {
                     return list;
                 }
             }
             
             String standinExpression = "";
             StringBuffer buf = new StringBuffer(doc.get());
             buf.replace(quackOffset, cursorOffset, standinExpression);
             CompilationUnit ast = EclipseUtil.compile(unit, unit
                     .getJavaProject(), buf.toString().toCharArray(), 0);
             //System.out.print(ast.toString());
             
             //TODO: get vars from SO
             VariableParser varPar = new VariableParser();
             //varPar.runParser(unit,ast);
             
             SOFunctions sof = new SOFunctions();
             URL url = sof.createURL(quack);
             log("API Call URL: "+ url.toString());
             String raw_text = sof.httpGetSO(url);
             Vector<ResponseObj> rawResponses =  sof.processJSON(raw_text);
             
             for(int i=0; i< rawResponses.size(); i++){

                 	list.add(new SOCompletionProposal(unit.getJavaProject()
                             .getProject(), rawResponses.get(i).getReplacementString(), quackOffset, quack.length(),
                             rawResponses.get(i).getDisplayString().length(), null, rawResponses.get(i).getDisplayString() + " [from SO_Quack]",
                             null, null, 1000000 - i));
                 	
                    varPar.parseSO(unit, rawResponses.get(i).getReplacementString());
             }
             	
                 return list;
             
         } catch (Throwable e) {
             log("Main.java(at end)", e);
             throw new Error(e);
         }
     }

}
