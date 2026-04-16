package de.creditreform.crefoteam.cte.testsupporttool.gui;

import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIFrame;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import org.junit.jupiter.api.AfterEach;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JFileChooserOperator;
import org.netbeans.jemmy.operators.JFrameOperator;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Jemmy-Basisklasse fuer GUI-Tests. Port aus
 * {@code testsupport_client.tesun.gui.BaseGUITest} (JUnit 4 → JUnit 5).
 *
 * <p>Unterklassen uebergeben den aufzuraeumenden {@link GUIFrame} via
 * {@link #setGuiFrame(GUIFrame)} in ihrem {@code @BeforeEach}-Setup —
 * {@code @AfterEach} im Basistest schließt ihn dann automatisch.
 */
public abstract class BaseGUITest {

    protected GUIFrame guiFrame;
    protected JFrameOperator frameOperator;

    protected final void setGuiFrame(GUIFrame guiFrame) {
        this.guiFrame = guiFrame;
        this.frameOperator = new JFrameOperator(guiFrame);
    }

    @AfterEach
    void tearDown() {
        if (guiFrame != null) {
            guiFrame.setVisible(false);
            guiFrame.dispose();
            guiFrame = null;
        }
        frameOperator = null;
    }

    protected void ensureDialogVisible(Component component, boolean shouldBeVisible) {
        GUIStaticUtils.warteBisken(30);
        if (shouldBeVisible) {
            assertThat(component.isVisible()).isTrue();
        } else {
            assertThat(component.isVisible()).isFalse();
        }
    }

    protected JDialogOperator ensureDialogVisible(String dlgTitle, boolean shouldBeVisible) {
        JDialogOperator dialogOperator = new JDialogOperator(dlgTitle);
        Container contentPane = dialogOperator.getContentPane();
        ensureDialogVisible(contentPane, shouldBeVisible);
        return dialogOperator;
    }

    protected void confirmDialog(String dialogTitle, String strErrorMsg, String strCommand) {
        JDialogOperator dialogOperator = ensureDialogVisible(dialogTitle, true);
        if (strErrorMsg != null && !strErrorMsg.isEmpty()) {
            Container contentPane = dialogOperator.getContentPane();
            String strMessage = (String) ((JOptionPane) contentPane.getComponent(0)).getMessage();
            assertThat(strMessage).contains(strErrorMsg);
        }
        JButtonOperator buttonOperator = new JButtonOperator(dialogOperator, strCommand);
        pushSafely(buttonOperator);
    }

    protected void confirmJFileChooser(File fileToBeChoosen, String buttonText) {
        JFileChooserOperator jFileChooserOperator = new JFileChooserOperator();
        ensureDialogVisible(jFileChooserOperator.getWindow(), true);
        jFileChooserOperator.setSelectedFile(fileToBeChoosen);
        JButtonOperator buttonOperatorOpen = new JButtonOperator(jFileChooserOperator, buttonText);
        pushSafely(buttonOperatorOpen);
    }

    /**
     * Klickt einen Button stabil per {@code SwingUtilities.invokeLater(button::doClick)}
     * statt ueber {@link JButtonOperator#push()}. Jemmy's push() ist anfaellig dafuer,
     * unter bestimmten Window-Focus-/Timing-Bedingungen den Click zu verlieren.
     *
     * <p><b>invokeLater (nicht invokeAndWait):</b> der ActionListener eines Buttons
     * kann einen modalen Dialog oeffnen. Modal-Dialoge laufen in einer secondary
     * event loop auf dem EDT — der doClick()-Call kehrt erst zurueck, wenn der
     * Dialog geschlossen wird. Mit invokeAndWait wuerde der Test-Thread warten,
     * obwohl er selbst denjenigen Dialog wegklicken muesste → Deadlock.
     */
    protected static void pushSafely(JButtonOperator buttonOperator) {
        JButton button = (JButton) buttonOperator.getSource();
        SwingUtilities.invokeLater(button::doClick);
    }

    protected static void pushSafely(ContainerOperator container, String buttonText) {
        pushSafely(new JButtonOperator(container, buttonText));
    }

    /**
     * Erzeugt eine GUIFrame-Subklasse-Instanz auf dem EDT.
     *
     * <p>Hintergrund: Swing erlaubt nur dem EDT, Komponenten zu mutieren. Der
     * {@code TestSupportGUI}-Konstruktor setzt Content-Pane/Visible — Layout/
     * Validate trigget Document-Locks auf JTextAreas, die mit Background-Thread-
     * Locks kollidieren koennen, wenn der Konstruktor vom Test-Thread lief.
     */
    protected static <T> T createOnEdt(Supplier<T> factory) {
        Object[] holder = new Object[1];
        try {
            SwingUtilities.invokeAndWait(() -> holder[0] = factory.get());
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) throw (RuntimeException) cause;
            if (cause instanceof Error) throw (Error) cause;
            throw new RuntimeException(cause);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("createOnEdt interrupted", e);
        }
        @SuppressWarnings("unchecked")
        T result = (T) holder[0];
        return result;
    }
}
