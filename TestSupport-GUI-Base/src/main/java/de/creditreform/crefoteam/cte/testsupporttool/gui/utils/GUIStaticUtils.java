package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.utils.GUIStaticUtils}.
 *
 * <p>Sammlung statischer GUI-Helper: File-Chooser, Dialoge, JTree/JTable-
 * Operationen, Wildcard-Matching, kleine Konvertierungs-Utilities.
 *
 * <p><b>CLAUDE_MODE-Bereiche:</b>
 * <ul>
 *   <li>{@link #showExceptionMessage} nutzt im Original
 *       {@code TesunUtilites.buildExceptionMessage(ex, 20)} — hier durch
 *       ein direktes {@code ex.toString()} ersetzt.</li>
 *   <li>{@link #checkIfBpmnFileExists} und
 *       {@link #uploadStateEngineProcessesFromClassPath} sind Activiti-spezifisch
 *       und im Spike als UnsupportedOperation-Stubs ausgefuehrt.</li>
 *   <li>{@link #getVersionFromPOM} braucht {@code maven-model} (im Spike
 *       absichtlich nicht als Dependency); liefert null. Ersatz:
 *       {@code EnvironmentConfig.getVersionFromBuildInfo()}.</li>
 * </ul>
 */
public class GUIStaticUtils {
    private static final String LOGGER_NAME = "TIMELINE";
    private static final Cursor DEF_CURSOR = Cursor.getDefaultCursor();
    private static final Cursor WAIT_CURSOR = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

    public static void setWaitCursor(Component parent, boolean yesno) {
        parent.setCursor(yesno ? WAIT_CURSOR : DEF_CURSOR);
    }

    public static void warteBisken(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (InterruptedException ex) {
        }
    }

    private static class MultipleFileFilter extends FileFilter {
        private final String filterRegExp;
        public MultipleFileFilter(String filterRegExp) {
            this.filterRegExp = filterRegExp;
        }

        @Override
        public boolean accept(File theFile) {
            if (theFile.isDirectory()) {
                return true;
            }
            String[] extensions = filterRegExp.split("\\|");
            for (String extension : extensions) {
                if (theFile.getName().endsWith(extension)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "*." + filterRegExp;
        }
    }

    public static List<String> chooseFileNames(Component parent, String defaultName, final String filterRegExp, boolean mltipleSelektion) {
        String path = defaultName;
        JFileChooser chooser = new JFileChooser(path);
        FileFilter fileFilter = new MultipleFileFilter(filterRegExp);
        chooser.setMultiSelectionEnabled(mltipleSelektion);
        chooser.addChoosableFileFilter(fileFilter);
        chooser.setFileFilter(fileFilter);
        int option = chooser.showOpenDialog(parent);
        List<String> selectedNames = new ArrayList<>();
        if (option == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            for (File file : files) {
                selectedNames.add(file.getPath());
            }
        }
        return selectedNames;
    }

    public static String chooseDirectory(Component parent, String fullPath, String dialogTitle) {
        String choosenDir = "";
        JFileChooser chooser = new JFileChooser(fullPath);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Quellen", ".", "gpg", "pgp", "zip");
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setDialogTitle(dialogTitle);
        int option = chooser.showOpenDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                choosenDir = chooser.getSelectedFile().getAbsolutePath();
            }
        }
        return choosenDir;
    }

    public static String chooseFileName(Component parent, String fullPath, final String chooseableExtension, boolean open) {
        String dirName = fullPath;
        String fileName = "";
        if (!open && (fullPath != null)) {
            int lastIndexOf = fullPath.lastIndexOf(File.separator);
            if (lastIndexOf > 0) {
                dirName = fullPath.substring(0, lastIndexOf);
                fileName = fullPath.substring(lastIndexOf + 1);
            }
        }
        JFileChooser chooser = new JFileChooser(dirName);
        if (!fileName.isEmpty()) {
            chooser.setSelectedFile(new File(fileName));
        }
        if (chooseableExtension != null) {
            String[] extensions = chooseableExtension.split("[,;|]");
            chooser.setFileFilter(new TheFileFilter(extensions[0]));
            for (int i = 1; i < extensions.length; i++) {
                chooser.addChoosableFileFilter(new TheFileFilter(extensions[i]));
            }
        }
        int option;
        if (open) {
            option = chooser.showOpenDialog(parent);
        } else {
            option = chooser.showSaveDialog(parent);
        }
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                String fName = chooser.getSelectedFile().getPath().toLowerCase();
                return fName;
            }
        }
        return null;
    }

    public static boolean checkFileName(Component parent, String oldFileName, String newFileName) {
        if ((newFileName == null) || newFileName.isEmpty()) {
            return false;
        }
        if (Objects.equals(newFileName, oldFileName)) {
            return true;
        }
        File theFile = new File(newFileName);
        if (theFile.exists()) {
            int option = JOptionPane.showConfirmDialog(parent, "Soll die Datei überschrieben werden?", "Datei Speichern", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                theFile.renameTo(new File(newFileName + "." + System.nanoTime()));
                return true;
            }
            return false;
        }
        return true;
    }

    public static String showExceptionMessage(Component parent, String titelMsg, Throwable ex) {
        // CLAUDE_MODE: Original ruft TesunUtilites.buildExceptionMessage(ex, 20).
        // Ersatz: direkte ex.toString() bis TesunUtilites portiert ist.
        String errMsg = ex == null ? "" : ex.toString();
        JScrollPane jScrollPane = getJScrollPaneFor(parent, errMsg);
        JOptionPane.showMessageDialog(parent, jScrollPane, titelMsg, JOptionPane.ERROR_MESSAGE);
        return errMsg;
    }
    public static int showConfirmDialog(Component parent, String message, String appTitle, int option) {
        JScrollPane jScrollPane = getJScrollPaneFor(parent, message);
        int answer = JOptionPane.showConfirmDialog(parent, jScrollPane, appTitle, option);
        return answer;
    }

    public static Boolean showConfirmDialog(Component parent, String message, String appTitle) {
        JScrollPane jScrollPane = getJScrollPaneFor(parent, message);
        int confirm = JOptionPane.showConfirmDialog(parent, jScrollPane, appTitle, JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_NO_OPTION;
    }

    private static JScrollPane getJScrollPaneFor(Component parent, String message) {
        JTextArea textArea = new JTextArea(message);
        JScrollPane jScrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.addHierarchyListener(new HierarchyListener() {
            public void hierarchyChanged(HierarchyEvent e) {
                //when the hierarchy changes get the ancestor for the message
                Window window = SwingUtilities.getWindowAncestor(jScrollPane);
                //check to see if the ancestor is an instance of Dialog and isn't resizable
                if (window instanceof Dialog) {
                    Dialog dialog = (Dialog) window;
                    if (!dialog.isResizable()) {
                        //set resizable to true
                        dialog.setResizable(true);
                    }
                }
            }
        });
        int width = parent.getWidth();
        if (width < 100) {
            width = 800;
        }
        int height = parent.getHeight();
        if (height < 100) {
            height = 400;
        }
        jScrollPane.setPreferredSize(new Dimension(width / 2, height / 2));
        return jScrollPane;
    }

    public static <T> T[] concatenate(T[] A, T[] B) {
        int aLen = A.length;
        int bLen = B.length;

        @SuppressWarnings("unchecked")
        T[] C = (T[]) Array.newInstance(A.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);

        return C;
    }

    public static String findFirstFile(final String dirName, final String fileNamePattern) {
        File curDir = new File(dirName);
        Collection<File> fileNameList = FileUtils.listFiles(curDir, new SuffixFileFilter(fileNamePattern), TrueFileFilter.TRUE);
        if (!fileNameList.isEmpty()) {
            File firstFile = fileNameList.iterator().next();
            return firstFile.getPath();
        }
        return "";
    }

    public static void expandAll(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        TreePath treePath = new TreePath(root.getFirstLeaf().getPath());
        tree.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
    }

    public static void expandToLast(JTree tree) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        TreePath treePath = new TreePath(root.getLastLeaf().getPath());
        tree.setSelectionPath(treePath);
        tree.scrollPathToVisible(treePath);
    }

    public static void collapseAll(JTree tree) {
        int row = tree.getRowCount() - 1;
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
    }

    public static void selectRowInTable(JTable tableSearchCriterias, int newIndex) {
        tableSearchCriterias.setRowSelectionInterval(newIndex, newIndex);
        tableSearchCriterias.scrollRectToVisible(tableSearchCriterias.getCellRect(newIndex, 0, true));
    }

    static class TheFileFilter extends FileFilter {
        private final String extension;
        String regex;

        public TheFileFilter(String extension) {
            this.extension = extension;
            regex = wildcardToRegex(this.extension.toLowerCase());
        }

        @Override
        public boolean accept(File file) {
            return file.isDirectory() || file.getName().toLowerCase().matches(regex);
        }

        @Override
        public String getDescription() {
            return extension + "-Dateien";
        }

        public String wildcardToRegex(String wildcardStr) {
            Pattern regex = Pattern.compile("[^*?\\\\]+|(\\*)|(\\?)|(\\\\)");
            Matcher m = regex.matcher(wildcardStr);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                if (m.group(1) != null) m.appendReplacement(sb, ".*");
                else if (m.group(2) != null) m.appendReplacement(sb, ".");
                else if (m.group(3) != null) m.appendReplacement(sb, "\\\\\\\\");
                else m.appendReplacement(sb, "\\\\Q" + m.group(0) + "\\\\E");
            }
            m.appendTail(sb);
            return sb.toString();
        }
    }

    public static boolean isEmpty(String strVal) {
        return strVal == null || strVal.isEmpty();
    }

    public static boolean isValue(Object objValue) {
        return (objValue != null) && !objValue.toString().isEmpty();
    }

    public static Boolean parseBoolean(Object objValue) {
        if (GUIStaticUtils.isValue(objValue)) {
            return Boolean.parseBoolean(objValue.toString());
        }
        return null;
    }

    public static Integer parseInt(Object objValue) {
        if (GUIStaticUtils.isValue(objValue)) {
            return Integer.parseInt(objValue.toString());
        }
        return null;
    }

    public static Long parseLong(Object objValue) {
        if (GUIStaticUtils.isValue(objValue)) {
            return Long.parseLong(objValue.toString());
        }
        return null;
    }

    public static Dimension calcSomePercentOfScreenSize(double xPercent, double yPercent) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension d = new Dimension((int) (dim.getWidth() * xPercent / 100), (int) (dim.getHeight() * yPercent / 100));
        return d;
    }

    public static Frame getParentFrame(Container container) {
        while (container != null) {
            container = container.getParent();
            if (container instanceof Frame) {
                return (Frame) container;
            }
        }
        return null;
    }

    /** StateMachine braucht kein BPMN-Deployment — immer true. */
    public static boolean checkIfBpmnFileExists(Object stateEngineRestService, String envName, String bpmnFileName, boolean askIfExists) {
        return true;
    }

    /** StateMachine braucht kein BPMN-Deployment — leere Liste. */
    public static List<File> uploadStateEngineProcessesFromClassPath(Object stateEngineRestService, String envName) {
        return new ArrayList<>();
    }

    public static File prepareBpmnFileForEnvironment(File srcFile, File dstFile, String envName) throws Exception {
        String oldContent = FileUtils.readFileToString(srcFile);
        String newContent = oldContent.replaceAll("%ENV%", envName);
        FileUtils.writeStringToFile(dstFile, newContent);
        return dstFile;
    }

    /**
     * <p><b>CLAUDE_MODE:</b> Originalmethode liest pomFileName via
     * {@code MavenXpp3Reader}. Im Spike vermeiden wir die maven-model-
     * Dependency. Bevorzugte Quelle:
     * {@code EnvironmentConfig.getVersionFromBuildInfo()}.
     */
    public static String getVersionFromPOM(String pomFileName) {
        return new de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig((String) null).getVersionFromBuildInfo();
    }
}
