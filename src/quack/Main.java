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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.json.JSONObject;
import org.omg.CORBA.portable.InputStream;

import sun.misc.IOUtils;

import com.sun.tools.hat.internal.parser.Reader;

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

    // TODO: WHERE DOES THIS FUNCT GET CALLED FROM???
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
             //TODO: Just testing out the HTTP get for queries, DELETE later
             String strUrl = "http://api.stackexchange.com/2.1/search?order=desc&sort=activity&tagged=java&intitle=add%20line&site=stackoverflow&filter=!--iqJbOieOg3";
             URL url = new URL(strUrl);
     		 //java.net.URLConnection connection = url.openConnection();
     		 //java.io.InputStream is = connection.getInputStream();
     		 //InputStreamReader isr = new InputStreamReader(is);
     		 //BufferedReader br = new BufferedReader(isr);
     		 BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
     		 StringBuilder builder = new StringBuilder();
     		 String temp_line = null;
     		 String rawText = null;
     		 while ((temp_line = br.readLine()) != null)
     	     {
     			builder.append(temp_line); 
     			//System.out.print("heelo");
     	     }
     	     // JSONObject json = new JSONObject(rawText.toString());
     		//Object obj=JSONValue.parse(content.toString());
     		//JSONArray finalResult=(JSONArray)obj;
     		//System.out.println(finalResult);
     		
             //TODO: end of http section
     		 
             //Vector<MyCompletionProposal> list = new Vector();
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
                 boolean sureItsAQuack = UU.matches(" |^_$", quack);
                 if (!sureItsAQuack) {
                     return list;
                 }
             }
             Ident quackIdent = new Ident(quack);

             log("version: 0.0.13");
             log("input: " + quack);

             // work here
             UU.profile("part 1");

             // work here
             UU.profile("ast build");

             String standinExpression = "null";
             StringBuffer buf = new StringBuffer(doc.get());
             buf.replace(quackOffset, cursorOffset, standinExpression);
             CompilationUnit ast = EclipseUtil.compile(unit, unit
                     .getJavaProject(), buf.toString().toCharArray(), 0);

             // work here
             UU.profile("ast build");

             // work here
             UU.profile("model");

             Model model = modelCache.getModel(unit, ast);
             synchronized (model) {
                 model.processTypesForAST(ast);

                 // work here
                 UU.profile("model");

                 // work here
                 UU.profile("count funcs");

                 // work here
                 Bag<String> functionCallCounts = new Bag();
                 EclipseUtil.countCallsToDifferentMethodsAndFields(ast,
                         functionCallCounts);

                 // work here
                 UU.profile("count funcs");

                 // work here
                 UU.profile("walker{}");

                 model.functionCallCounts = functionCallCounts;
                 Deslopper d = new Deslopper();
                 Walker w = new Walker(ast, model, quackOffset, quackIdent, d);

                 // work here
                 UU.profile("walker{}");

                 // work here
                 UU.profile("all");
//                 UU.profilePrint();

                 if (w.guesses.size() == 0) {
                     log("no guesses");
                 }
                 
                 int i = 0;
                 //for (i=0; i<1;i++){
                 for (String guess : w.guesses) {
                 	list.add(new SOCompletionProposal(unit.getJavaProject()
                             .getProject(), guess, quackOffset, quack.length(),
                             guess.length(), null, guess + " [from Quack]",
                             null, null, 1000000 - i));
                     /*list.add(new MyCompletionProposal(unit.getJavaProject()
                             .getProject(), guess, quackOffset, quack.length(),
                             guess.length(), null, guess + " [from Quack]",
                             null, null, 1000000 - i));*/
                     i++;
                 }
                 return list;
             }
         } catch (Throwable e) {
             log("Main.java(at end)", e);
             throw new Error(e);
         }
     }
    
    public List<MyCompletionProposal> getCOMPProposals(ICompilationUnit unit,
           IDocument doc, int selectionOffset, int selectionLength) {
    	try {
            lastProject = unit.getJavaProject().getProject();

            // work here
            UU.profileClear();
            UU.profile("all");

            // work here
            UU.profile("part 1");

            Main main = Main.getMain();
            Vector<MyCompletionProposal> list = new Vector();
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
                boolean sureItsAQuack = UU.matches(" |^_$", quack);
                if (!sureItsAQuack) {
                    return list;
                }
            }
            Ident quackIdent = new Ident(quack);

            log("version: 0.0.13");
            log("input: " + quack);

            // work here
            UU.profile("part 1");

            // work here
            UU.profile("ast build");

            String standinExpression = "null";
            StringBuffer buf = new StringBuffer(doc.get());
            buf.replace(quackOffset, cursorOffset, standinExpression);
            CompilationUnit ast = EclipseUtil.compile(unit, unit
                    .getJavaProject(), buf.toString().toCharArray(), 0);

            // work here
            UU.profile("ast build");

            // work here
            UU.profile("model");

            Model model = modelCache.getModel(unit, ast);
            synchronized (model) {
                model.processTypesForAST(ast);

                // work here
                UU.profile("model");

                // work here
                UU.profile("count funcs");

                // work here
                Bag<String> functionCallCounts = new Bag();
                EclipseUtil.countCallsToDifferentMethodsAndFields(ast,
                        functionCallCounts);

                // work here
                UU.profile("count funcs");

                // work here
                UU.profile("walker{}");

                model.functionCallCounts = functionCallCounts;
                Deslopper d = new Deslopper();
                Walker w = new Walker(ast, model, quackOffset, quackIdent, d);

                // work here
                UU.profile("walker{}");

                // work here
                UU.profile("all");
//                UU.profilePrint();

                if (w.guesses.size() == 0) {
                    log("no guesses");
                }
                
                int i = 0;
                for (String guess : w.guesses) {
                    list.add(new MyCompletionProposal(unit.getJavaProject()
                            .getProject(), guess, quackOffset, quack.length(),
                            guess.length(), null, guess + " [from Quack]",
                            null, null, 1000000 - i));
                    i++;
                }
                return list;
            }
        } catch (Throwable e) {
            log("Main.java(at end)", e);
            throw new Error(e);
        }
    }
}