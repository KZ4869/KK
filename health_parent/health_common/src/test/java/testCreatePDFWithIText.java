import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Description:
 * mvn install:install-file
 * -Dfile=D:\iTextAsian.jar
 * -DgroupId=com.alpha
 * -DartifactId=itextasian -Dversion=1.0.0 -Dpackaging=jar
 * @Author: KZ4869
 * @CreateTime: 2021-01-17 20:26
 */
public class testCreatePDFWithIText {
    @Test
    public void testCreatePDFWithIText() throws Exception {
        // 创建一个文件 jar是lowagie下的
        Document doc = new Document();
        // 创建writer, 参数1：文档，参数2：保存到哪里?
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File("d:/itext.pdf")));
        // 打开文档
        doc.open();
        //字体中文无显示问题
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        // 添加内容
        doc.add(new Paragraph("Hello World! 黑马程序员", new Font(bfChinese)));
        // 关闭文档
        doc.close();
    }
}
