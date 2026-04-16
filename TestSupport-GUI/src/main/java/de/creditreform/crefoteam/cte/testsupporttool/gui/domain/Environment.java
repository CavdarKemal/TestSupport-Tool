package de.creditreform.crefoteam.cte.testsupporttool.gui.domain;

import de.creditreform.crefoteam.cte.rest.RestInvokerConfig;
import de.creditreform.crefoteam.cte.tesun.util.TestCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * 1:1-Port aus {@code testsupport_client.tesun.gui.domain.Environment}.
 *
 * <p>Datentraeger fuer eine Umgebungs-Definition (Pfade, REST-Configs,
 * Customer-Liste). Wird von der GUI als Modell-Knoten genutzt.
 */
public class Environment implements Serializable {
    protected final static Logger logger = LoggerFactory.getLogger(Environment.class);
    /***
     DEFAULT.TEMPLATE-PATH=./ENEDownloadDir
     DEFAULT.GENERATE-PATH=./ENEGeneratedDir
     DEFAULT.EXPORTED-PATH=./ENEExportedXMLsDir
     DEFAULT.COLLECTED-XMLS-PATH=./ENECollectedXMLsDir
     DEFAULT.TEMPLATE-REF-XMLS-PATH=./ENETemplateRefXMLsDir
     DEFAULT.GENERATE-REF-XMLS-PATH=./ENEGeneratedRefXMLsDir
     DEFAULT.CHECKED-RXMLS-PATH=./ENECheckedXMLsDir

     DEFAULT.SERVICE-DOWNLOAD-URI= http://rhsctem015.ecofis.de:7077/cte_tesun_service/tesun/xmlaccess
     DEFAULT.SERVICE-DOWNLOAD-USERNAME=tesuntestene
     DEFAULT.SERVICE-DOWNLOAD-PASSWORD=tesuntestene

     DEFAULT.SERVICE-UPLOAD-URI= http://rhsctem015.ecofis.de:7077/cte_tesun_service/tesun/xmlaccess
     DEFAULT.SERVICE-UPLOAD-USERNAME=tesuntestene
     DEFAULT.SERVICE-UPLOAD-PASSWORD=tesuntestene

     DEFAULT.SERVICE-EXPORTS-URI= http://rhsctem015.ecofis.de:7077/cte_tesun_service/tesun/configinfo
     DEFAULT.SERVICE-EXPORTS-USERNAME=tesuntestene
     DEFAULT.SERVICE-EXPORTS-PASSWORD=tesuntestene
     */
    private String envName;
    private String statePropsFileName;
    private String configPropsFileName;
    private String searchCfgFileName;
    private String searchResultsPath;
    private String downloadPath;
    private String generatedPath;
    private String exportedPath;
    private String collectedPath;
    private String regeneratedPath;
    private String templateRefPath;
    private String checkedPath;
    private RestInvokerConfig RestInvokerConfigDownload;
    private RestInvokerConfig RestInvokerConfigUpload;
    private RestInvokerConfig RestInvokerConfigExports;
    private List<TestCustomer> customerTestInfos;

    public Environment(String envName) {
        this.envName = envName;
    }

    public List<TestCustomer> getCustomerTestInfos() {
        return customerTestInfos;
    }

    public void setCustomerTestInfos(List<TestCustomer> CustomerTestInfos) {
        this.customerTestInfos = CustomerTestInfos;
    }

    public String getSearchCfgFileName() {
        return searchCfgFileName;
    }

    public void setSearchCfgFileName(String searchCfgFileName) {
        this.searchCfgFileName = searchCfgFileName;
    }

    public String getSearchResultsPath() {
        return searchResultsPath;
    }

    public void setSearchResultsPath(String searchResultsPath) {
        this.searchResultsPath = searchResultsPath;
    }

    public String getConfigPropsFileName() {
        return configPropsFileName;
    }

    public void setConfigPropsFileName(String configPropsFileName) {
        this.configPropsFileName = configPropsFileName;
    }

    public String getStatePropsFileName() {
        return statePropsFileName;
    }

    public void setStatePropsFileName(String statePropsFileName) {
        this.statePropsFileName = statePropsFileName;
    }

    public String getTemplateRefPath() {
        return templateRefPath;
    }

    public void setTemplateRefPath(String templateRefPath) {
        this.templateRefPath = templateRefPath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getGeneratedPath() {
        return generatedPath;
    }

    public void setGeneratedPath(String generatedPath) {
        this.generatedPath = generatedPath;
    }

    public String getExportedPath() {
        return exportedPath;
    }

    public void setExportedPath(String exportedPath) {
        this.exportedPath = exportedPath;
    }

    public String getCollectedPath() {
        return collectedPath;
    }

    public void setCollectedPath(String collectedPath) {
        this.collectedPath = collectedPath;
    }

    public String getRegeneratedPath() {
        return regeneratedPath;
    }

    public void setRegeneratedPath(String regeneratedPath) {
        this.regeneratedPath = regeneratedPath;
    }

    public String getCheckedPath() {
        return checkedPath;
    }

    public void setCheckedPath(String checkedPath) {
        this.checkedPath = checkedPath;
    }

    public RestInvokerConfig getRestInvokerConfigDownload() {
        return RestInvokerConfigDownload;
    }

    public void setRestInvokerConfigDownload(RestInvokerConfig RestInvokerConfigDownload) {
        this.RestInvokerConfigDownload = RestInvokerConfigDownload;
    }

    public RestInvokerConfig getRestInvokerConfigUpload() {
        return RestInvokerConfigUpload;
    }

    public void setRestInvokerConfigUpload(RestInvokerConfig RestInvokerConfigUpload) {
        this.RestInvokerConfigUpload = RestInvokerConfigUpload;
    }

    public RestInvokerConfig getRestInvokerConfigExports() {
        return RestInvokerConfigExports;
    }

    public void setRestInvokerConfigExports(RestInvokerConfig RestInvokerConfigExports) {
        this.RestInvokerConfigExports = RestInvokerConfigExports;
    }

    public Environment(Environment templateEnvironment) {
        setEnvName(templateEnvironment.getEnvName());
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String toString() {
        return envName;
    }

    public String dumpEnvironmentInfo(String strPraefix) {
        StringBuffer infoBuffer = new StringBuffer();
        infoBuffer.append(envName);
        //    infoBuffer.append( "\n" + strPraefix + "Test Crefos Verzeichnis: " + getTestCrefosPath() );
        //    infoBuffer.append( "\n" + strPraefix + "Collected Crefos Verzeichnis: " + getCollectedCrefosPath() );
        if (!envName.equals("Default")) {
            //      infoBuffer.append( "\n" + strPraefix + "Servlet-URL: " + getServletURL() );
            infoBuffer.append("\n" + strPraefix + "}");
        }
        String strInfo = infoBuffer.toString();
        return strInfo;
    }

    @Override
    public boolean equals(Object compareTo) {
        if (compareTo == null) {
            return false;
        }
        Environment compareToEnvironment = (Environment) compareTo;
        Boolean equals = true;
        equals &= envName.equals(compareToEnvironment.getEnvName());
        return equals;
    }

    private String normalize(String strValue) {
        if (strValue != null) {
            return strValue.replaceAll("\\\\", "/");
        }
        return null;
    }

    /******************************************************************************************************************/

    /******************************     ProtectedEnvironment     ******************************************************/
    /******************************************************************************************************************/
    public static class ProtectedEnvironment extends Environment {
        public ProtectedEnvironment(String envName) {
            super(envName);
        }

    }

}
