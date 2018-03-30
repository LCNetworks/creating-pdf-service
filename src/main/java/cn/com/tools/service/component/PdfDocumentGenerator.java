package cn.com.tools.service.component;

import cn.com.tools.service.exception.DocumentGeneratingException;
import cn.com.tools.service.freemarker.HtmlGenerator;
import com.itextpdf.text.pdf.BaseFont;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * pdf 生成
 *
 * @author lpc 修改时间： 2018年1月22日 下午3:27:22 修改内容：新建
 * @ClassName: PdfGenerator
 * @Description:pdf 生成
 */
public class PdfDocumentGenerator {

    private final static Logger logger = Logger.getLogger(PdfDocumentGenerator.class);
    private final static HtmlGenerator htmlGenerator;

    static {
        htmlGenerator = new HtmlGenerator();
    }

    /**
     * 使用模板,模板数据,生成pdf
     *
     * @param template   classpath中路径模板路径
     * @param variables  数据map
     * @param outputFile 生成pdf的路径
     * @throws DocumentGeneratingException
     * @Title: generate
     * @Description: 使用模板, 模板数据, 生成pdf
     */
    public boolean generate(String template, Map<String, Object> variables, String outputFile, String fontPath) throws DocumentGeneratingException {
        try {
            String htmlContent = this.htmlGenerator.generate(template, variables);
            this.generate(htmlContent, outputFile, fontPath);
            logger.info("The document is generated successfully,and stored in [" + outputFile + "]");
        } catch (Exception e) {
            e.printStackTrace();
            String error = "The document is failed to generate";
            logger.error(error);
            throw new DocumentGeneratingException(error, e);
        }
        return true;
    }

    /**
     * Output a pdf to the specified outputstream
     *
     * @param htmlContent the htmlstr
     * @param outputFile  the specified outputstream
     * @throws Exception
     */
    public void generate(String htmlContent, String outputFile, String fontPath) throws Exception {
        OutputStream out = null;
//        ITextRenderer iTextRenderer = null;
        ITextRenderer iTextRenderer = new ITextRenderer();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(htmlContent.getBytes("UTF-8")));
//            File f = new File(outputFile);
//            if (f != null && !f.getParentFile().exists()) {
//                f.getParentFile().mkdir();
//            }
            out = new FileOutputStream(outputFile);
//            iTextRenderer = (ITextRenderer) ITextRendererObjectFactory.getObjectPool().borrowObject();//获取对象池中对象
            ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
            fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            try {
                iTextRenderer.setDocument(doc, null);
                iTextRenderer.layout();
                iTextRenderer.createPDF(out);
            } catch (Exception e) {
//                ITextRendererObjectFactory.getObjectPool().invalidateObject(iTextRenderer);
//                iTextRenderer = null;
                throw e;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
            iTextRenderer = null;
//            if (iTextRenderer != null) {
//                try {
//                    ITextRendererObjectFactory.getObjectPool().returnObject(iTextRenderer);
//                } catch (Exception ex) {
//                    logger.error("Cannot return object from pool.", ex);
//                }
//            }
        }
    }
}
