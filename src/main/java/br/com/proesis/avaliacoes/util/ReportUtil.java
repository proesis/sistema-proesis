package br.com.proesis.avaliacoes.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author danilo
 */
public class ReportUtil {

    public static byte[] reportToPDF(List list, InputStream reportInputStream, HashMap<String, Object> map) throws Exception {
        try {
            JasperPrint jasperPrint;
            if(map == null){
                map = new HashMap<String, Object>();
            }
            JasperReport report = compileReport(reportInputStream);
            jasperPrint = JasperFillManager.fillReport(report, map, new JRBeanCollectionDataSource(list));
            byte[] rep = JasperExportManager.exportReportToPdf(jasperPrint);
            return rep;
        } catch (JRException e) {
            throw new Exception("Desculpe! Ocorreu um erro enquanto gerávamos seu Relatório!", e);
        } catch (Exception e) {
            throw new Exception("Desculpe! Ocorreu um erro interno ao gerar um relatório!", e);
        }
    }
    
    public static JasperReport compileReport(InputStream is) throws Exception{
        try {
            return JasperCompileManager.compileReport(is);
        } catch (JRException ex) {
            throw new Exception("Erro ao compilar o relatório!", ex);
        }
    }

}
