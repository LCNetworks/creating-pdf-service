package cn.com.tools.service.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class HtmlGenerator {

    /**
     * Generate html string.
     *
     * @param template  the name of freemarker teamlate.
     * @param variables the data of teamlate.
     * @return htmlStr
     * @throws IOException
     * @throws TemplateException
     * @throws Exception
     */
    public String generate(String template, Map<String, Object> variables) throws IOException, TemplateException {
        BufferedWriter writer = null;
        String htmlContent = "";
        try {
            Configuration config = new Configuration();
            if (template != null) {
                System.out.println(template);
                String[] templates = null;
                int s = 0;
                templates = template.split("/");
                s = template.split("/").length;
                config.setDirectoryForTemplateLoading(new File(template).getParentFile());
                Template tp = config.getTemplate(templates[s - 1]);
                StringWriter stringWriter = new StringWriter();
                writer = new BufferedWriter(stringWriter);

                tp.setEncoding("UTF-8");
                tp.process(variables, writer);
                htmlContent = stringWriter.toString();
                writer.flush();
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return htmlContent;
    }
}
