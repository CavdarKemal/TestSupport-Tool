package de.creditreform.crefoteam.cte.tesun.rest.dto;

import java.util.Collections;
import java.util.List;

public class TesunConfigInfo {
    private String umgebungsKuerzel;
    private List<TesunConfigExportInfo> exportPfade = Collections.emptyList();
    private List<TesunConfigUploadInfo> uploadPfade = Collections.emptyList();

    public String getUmgebungsKuerzel() { return umgebungsKuerzel; }
    public void setUmgebungsKuerzel(String v) { this.umgebungsKuerzel = v; }

    public List<TesunConfigExportInfo> getExportPfade() { return exportPfade; }
    public void setExportPfade(List<TesunConfigExportInfo> v) { this.exportPfade = v; }

    public List<TesunConfigUploadInfo> getUploadPfade() { return uploadPfade; }
    public void setUploadPfade(List<TesunConfigUploadInfo> v) { this.uploadPfade = v; }
}
