package cn.com.tools.service.controller;

import cn.com.tools.service.component.PdfDocumentGenerator;
import cn.com.tools.service.exception.DocumentGeneratingException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;

/**
 * @author lpc
 */
@Slf4j
@Controller
@RequestMapping("api/creatingPdf")
public class CreatingPDFController {
    //html模版
    @Value("${inhand.product.pdfTemplatePath}")
    private String templatePath;
    //pdf生成路径
    @Value("${inhand.product.pdfPath}")
    private String pdfPath;
    //文档ip地址
    @Value("${inhand.product.docIpPath}")
    private String docIpPath;
    //字体地址
    @Value("${inhand.product.fontPath}")
    private String fontPath;

    @RequestMapping(value = "/creatingPdf", method = RequestMethod.POST)
    @ApiOperation(value = "pdf生成", httpMethod = "POST", response = String.class, notes = "pdf生成")
    public
    @ResponseBody
    String creatingPdf(
            @RequestParam(value = "moduleName", required = true) String moduleName,
            @RequestParam(value = "dirName", required = true) String dirName,
            @RequestBody Map map) {
        //获取模版路径
        String template = templatePath + "/" + moduleName + ".html";
        //判断生成pdf文件夹
        String filePath = pdfPath + "/" + moduleName;
        File file1 = new File(filePath);
        if (file1.exists() == false || file1.isDirectory() == false) {
            file1.mkdir();
        }
        //判断生成该条记录专属文件夹
        filePath = filePath + "/" + dirName;
        File file2 = new File(filePath);
        if (file2.exists() == false || file2.isDirectory() == false) {
            file2.mkdir();
        }
        //判断文件夹内是否存在旧pdf记录，存在删除
        File dir = new File(filePath);
        removeDir(dir);
        // 生成pdf路径
        String pdfName = String.valueOf(System.currentTimeMillis()) + ".pdf";
        filePath = filePath + "/" + pdfName;
        try {
            // 生成pdf
            PdfDocumentGenerator pdfGenerator = new PdfDocumentGenerator();
            pdfGenerator.generate(template, map, filePath, fontPath);
            return docIpPath + "/" + moduleName + "/" + dirName + "/" + pdfName;    //此处地址用来访问，分隔符无需更改
        } catch (DocumentGeneratingException e) {
            e.printStackTrace();
            return null;
        } finally {
            map.clear();
        }
    }

    /**
     * 递归删除文件及文件下所有内容
     *
     * @param dir
     */
    public static void removeDir(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            //如果是目录，继续遍历
            if (file.isDirectory()) {
                removeDir(file);
            } else {
                //如果不是目录，就删除文件
                file.delete();
            }
        }
        //删除目录
//        dir.delete();
    }
}
