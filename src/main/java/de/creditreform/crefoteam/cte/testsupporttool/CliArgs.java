package de.creditreform.crefoteam.cte.testsupporttool;

/**
 * Parser für die {@link Main}-Kommandozeilenargumente. Unterstützte Form:
 *
 * <pre>
 *   e:&lt;envName&gt;       (alias: env, environment)
 *   Demo:&lt;true|false&gt; (alias: demoMode)
 * </pre>
 *
 * Jeweils mit oder ohne führendem Bindestrich. Beispiel:
 * <pre>
 *   java ... Main e:ENE -Demo:true
 *   java ... Main -e:GEE Demo:false
 *   java ... Main                 (Defaults: kein env → Demo)
 * </pre>
 *
 * Defaults: {@code envName == null} → in-memory Demo-Config;
 * {@code demoMode == null} → leitet sich aus dem env ab
 * (kein env = Demo, mit env = Real).
 */
public final class CliArgs {

    private final String envName;
    private final Boolean demoMode;

    private CliArgs(String envName, Boolean demoMode) {
        this.envName = envName;
        this.demoMode = demoMode;
    }

    public static CliArgs parse(String[] args) {
        String envName = null;
        Boolean demoMode = null;
        if (args == null) {
            return new CliArgs(null, null);
        }
        for (String raw : args) {
            if (raw == null || raw.isBlank()) continue;
            String token = raw.startsWith("-") ? raw.substring(1) : raw;
            int colon = token.indexOf(':');
            if (colon < 0) {
                throw new IllegalArgumentException("Argument '" + raw
                        + "' hat das falsche Format. Erwartet: <key>:<value>");
            }
            String key = token.substring(0, colon).trim().toLowerCase();
            String value = token.substring(colon + 1).trim();
            switch (key) {
                case "e":
                case "env":
                case "environment":
                    envName = value;
                    break;
                case "demo":
                case "demomode":
                    demoMode = parseBool(value);
                    break;
                default:
                    throw new IllegalArgumentException("Unbekanntes Argument '" + raw
                            + "'. Erlaubt: e:<env>, Demo:<true|false>");
            }
        }
        return new CliArgs(envName, demoMode);
    }

    private static Boolean parseBool(String value) {
        if ("true".equalsIgnoreCase(value) || "1".equals(value) || "ja".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value) || "0".equals(value) || "nein".equalsIgnoreCase(value)) return false;
        throw new IllegalArgumentException("Boolean-Wert '" + value + "' nicht erkannt (true/false/1/0/ja/nein)");
    }

    /** Gewählter Umgebungsname; {@code null} = Demo-Config ohne Properties-Datei. */
    public String getEnvName() { return envName; }

    /**
     * Effektiver Demo-Mode: wenn explizit gesetzt, dieser Wert; sonst
     * {@code true}, falls kein Env angegeben wurde.
     */
    public boolean isDemoMode() {
        if (demoMode != null) return demoMode;
        return envName == null;
    }

    /** Liefert {@code true}, falls Demo-Mode explizit gesetzt wurde. */
    public boolean isDemoExplicit() { return demoMode != null; }

    public static String usage() {
        return "Usage: java ... Main [e:<envName>] [Demo:<true|false>]\n"
                + "  e:<envName>       Lädt <envName>-config.properties (z. B. e:ENE).\n"
                + "                    Ohne diesen Parameter wird die in-memory Demo-Config genutzt.\n"
                + "  Demo:<true|false> Override für den Demo-Mode (default: true ohne env, false mit env).\n"
                + "Beispiele:\n"
                + "  Main                       → Demo-Mode mit in-memory Config\n"
                + "  Main e:ENE                 → Real-Mode gegen ENE-config.properties\n"
                + "  Main e:ENE -Demo:true      → Lädt ENE-Config, läuft aber im Demo-Mode (kein REST)";
    }
}
