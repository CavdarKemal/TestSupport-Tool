package de.creditreform.crefoteam.cte.tesun.xmlsearch.domain;

import de.creditreform.crefoteam.cte.tesun.util.TesunDateUtils;
import de.creditreform.crefoteam.cte.tesun.xmlsearch.handler.LogInfo;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class DecryptHandler {
    private final String passPhrase;
    private final String searchName;
    private XmlStreamListenerGroup listenerGroup;

    public DecryptHandler(XmlStreamListenerGroup listenerGroup, String passPhrase, String searchName) {
        this.searchName = searchName;
        this.listenerGroup = listenerGroup;
        this.passPhrase = passPhrase;
        // CLAUDE_MODE: this.pgpUtil = new LargeFilesPGPUtil() entfernt (ctreader nicht verfügbar)
    }

    public File decryptFile(File encryptedFile, File privateKeyFile, boolean force) throws Exception {
        // CLAUDE_MODE: PGP-Entschlüsselung nicht portiert (ctreader nicht verfügbar)
        throw new UnsupportedOperationException("PGP-Entschlüsselung nicht portiert (ctreader nicht verfügbar).");
    }

}
