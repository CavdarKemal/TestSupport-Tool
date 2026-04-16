package de.creditreform.crefoteam.cte.testsupporttool.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/** Exception-Formatierungshilfen. Gezogen aus {@code TesunUtilites}. */
public final class ExceptionUtils {

    private ExceptionUtils() { }

    public static String buildExceptionMessage(Throwable ex, int maxLines) {
        String className = "";
        String errMsg = "";
        if (ex != null) {
            className = ex.getClass().getName();
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            printWriter.flush();
            errMsg = writer.toString();
        }
        if (errMsg.startsWith("null") || errMsg.isBlank()) {
            while (ex != null) {
                errMsg += ex.getMessage();
                errMsg += "\n\t";
                ex = ex.getCause();
            }
        }
        if (errMsg.startsWith("null") || errMsg.isBlank()) {
            errMsg = className;
        }
        return errMsg;
    }
}
