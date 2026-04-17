package de.creditreform.crefoteam.cte.testsupporttool.gui.utils;

import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestSupportHelperTest {

    private static TestSupportHelper newHelper() {
        EnvironmentConfig env = new EnvironmentConfig("ENE");
        return new TestSupportHelper(env, null, null, null, null);
    }

    @Test
    void getScaledDimension_shrinksToFitLabel_keepingAspectRatio() {
        TestSupportHelper helper = newHelper();
        JLabel label = new JLabel();
        label.setSize(100, 100);
        BufferedImage img = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);

        // Image 200x100, Label 100x100 → ratio = min(0.5, 1.0) = 0.5 → 100x50.
        Dimension scaled = helper.getScaledDimension(label, img);
        assertThat(scaled).isEqualTo(new Dimension(100, 50));
    }

    @Test
    void getScaledDimension_zeroLabelSize_returnsZeroDimension() {
        TestSupportHelper helper = newHelper();
        JLabel label = new JLabel();
        label.setSize(0, 0);
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        Dimension scaled = helper.getScaledDimension(label, img);
        assertThat(scaled).isEqualTo(new Dimension(0, 0));
    }

    @Test
    void refreshProcessImage_nullStream_returnsNull() throws Exception {
        TestSupportHelper helper = newHelper();
        assertThat(helper.refreshProcessImage(null, new JLabel(), false)).isNull();
    }

    @Test
    void refreshProcessImage_setsIconAndReturnsImage() throws Exception {
        TestSupportHelper helper = newHelper();
        // Mini-PNG generieren statt Datei laden
        BufferedImage source = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(source, "png", bos);
        InputStream stream = new ByteArrayInputStream(bos.toByteArray());
        JLabel label = new JLabel();
        label.setSize(20, 20);

        BufferedImage result = helper.refreshProcessImage(stream, label, false);

        assertThat(result).isNotNull();
        assertThat(result.getWidth()).isEqualTo(10);
        assertThat(label.getIcon()).isNotNull();
    }

    @Test
    void refreshProcessImage_resize_scalesViaIcon() throws Exception {
        TestSupportHelper helper = newHelper();
        BufferedImage source = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(source, "png", bos);
        JLabel label = new JLabel();
        label.setSize(10, 10);

        BufferedImage result = helper.refreshProcessImage(
                new ByteArrayInputStream(bos.toByteArray()), label, true);

        // refreshProcessImage liefert das Original-Image zurueck (nicht das skalierte).
        assertThat(result.getWidth()).isEqualTo(20);
        assertThat(label.getIcon()).isNotNull();
    }

    @Test
    void claudeModeStubs_throwUnsupported() {
        TestSupportHelper helper = newHelper();
        Map<String, ?> empty = Map.of();

        assertThatThrownBy(() -> helper.checkStartCoinditions((Map) empty, false))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("CLAUDE_MODE");
        assertThatThrownBy(() -> helper.checkRunningJobs((Map) empty))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("CLAUDE_MODE");
        assertThatThrownBy(() -> helper.checkJvms((Map) empty))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("CLAUDE_MODE");
        assertThatThrownBy(() -> helper.killOrContinueRunningActivitiProcess("k", "p", false))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("CLAUDE_MODE");
    }

    @Test
    void getterStubs_returnNull() {
        TestSupportHelper helper = newHelper();
        assertThat(helper.getTesunRestServiceWLS()).isNull();
    }
}
