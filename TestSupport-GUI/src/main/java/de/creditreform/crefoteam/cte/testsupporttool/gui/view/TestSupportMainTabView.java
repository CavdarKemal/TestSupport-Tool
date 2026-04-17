package de.creditreform.crefoteam.cte.testsupporttool.gui.view;

import de.creditreform.crefoteam.cte.testsupporttool.gui.design.TestSupportMainTabPabel;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.GUIStaticUtils;
import de.creditreform.crefoteam.cte.testsupporttool.gui.utils.TestSupportHelper;
import de.creditreform.crefoteam.cte.testsupporttool.logging.TimelineLogger;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.function.Supplier;
import javax.swing.*;

public class TestSupportMainTabView extends TestSupportMainTabPabel {

    private BufferedImage lastProcessImage;
    private final boolean resizeProcessImage = false;
    private Supplier<TestSupportHelper> testSupportHelperSupplier;

    public TestSupportMainTabView() {
        super();
        initListeners();
    }

    public void init(Supplier<TestSupportHelper> testSupportHelperSupplier) {
        this.testSupportHelperSupplier = testSupportHelperSupplier;
    }

    private void initListeners() {
        getButtonClearLOGPanel().addActionListener(e -> getTextAreaTaskListenerInfo().setText(""));
        getScrollPanelProcessImage().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                doResize();
            }
        });
    }

    public void appendToConsole(String message) {
        if (message == null) return;
        final String forFile = message.startsWith("\n") ? message.substring(1) : message;
        TimelineLogger.info(this.getClass(), forFile);
        SwingUtilities.invokeLater(() -> {
            getTextAreaTaskListenerInfo().append(message.replaceAll("\t", "  "));
            if (getCheckBoxScrollToEnd().isSelected()) {
                getTextAreaTaskListenerInfo().setCaretPosition(getTextAreaTaskListenerInfo().getDocument().getLength() - 1);
            }
        });
    }

    public void setProcessImage(InputStream inputStream) {
        SwingUtilities.invokeLater(() -> {
            try {
                JLabel jLabel = (JLabel) getScrollPanelProcessImage().getViewport().getComponent(0);
                lastProcessImage = testSupportHelperSupplier.get().refreshProcessImage(inputStream, jLabel, resizeProcessImage);
            } catch (Exception ex) {
                GUIStaticUtils.showExceptionMessage(this, "Fehler beim Erzeugen des Bitmaps!", ex);
            }
        });
    }

    private void doResize() {
        JLabel jLabel = (JLabel) getScrollPanelProcessImage().getViewport().getComponent(0);
        TestSupportHelper helper = testSupportHelperSupplier != null ? testSupportHelperSupplier.get() : null;
        if (resizeProcessImage && lastProcessImage != null && helper != null) {
            try {
                Dimension scaledDimension = helper.getScaledDimension(jLabel, lastProcessImage);
                Image resizedImage = lastProcessImage.getScaledInstance(
                        (int) scaledDimension.getWidth(), (int) scaledDimension.getHeight(), Image.SCALE_DEFAULT);
                jLabel.setIcon(new ImageIcon(resizedImage));
            } catch (Exception ex) {
                appendToConsole(GUIStaticUtils.showExceptionMessage(this, "Fehler!", ex));
            }
        }
    }
}
